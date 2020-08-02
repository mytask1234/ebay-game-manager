package com.ebay.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ebay.dto.AnswerQuestionResponseDto;
import com.ebay.dto.AnswerStatus;
import com.ebay.dto.LeaderboardResponseDto;
import com.ebay.dto.UserAndPointsResponseDto;
import com.ebay.exception.GameException;
import com.ebay.resolver.Resolver;

public class Game {

	private final int POINTS_FOR_CORRECT_ANSWER;
	private final List<Question> questionList;
	private final Resolver resolver;
	private final Map<String, UserInGame> usernameToUserInGameMap;
	private final Object lock;

	public Game(int pointsForCorrectAnswer, List<Question> questionList, Resolver resolver) {
		super();
		POINTS_FOR_CORRECT_ANSWER = pointsForCorrectAnswer;
		this.questionList = questionList;
		this.resolver = resolver;
		usernameToUserInGameMap = new ConcurrentHashMap<>();
		lock = new Object();
	}
	
	public Optional<Question> getQuestionByUsername(String username) {

		final UserInGame userInGame = usernameToUserInGameMap.computeIfAbsent(username, k -> new UserInGame());
		
		return IntStream.range(0, questionList.size())
				 // if the user didn't answer the question and the question status is not unresolved
				 .filter(questionId ->  userInGame.getUserChoosedAnswerId(questionId) == null && 
				 						questionList.get(questionId).getQuestionStatus() != QuestionStatus.UNRESOLVED)
				 .mapToObj(questionId -> questionList.get(questionId))
				 .findFirst();
	}

	public AnswerQuestionResponseDto userAnswerQuestion(String username, int questionId, int userChoosedAnswerId) {

		Question question;
		
		try {
			question = questionList.get(questionId);
		} catch (Throwable t) {
			throw new GameException("illegal questionId: " + questionId, t);
		}
		
		if (question.getQuestionStatus() == QuestionStatus.UNRESOLVED) {
			return new AnswerQuestionResponseDto(AnswerStatus.UNRESOLVED);
		}
		
		UserInGame userInGame = usernameToUserInGameMap.get(username);
		
		if (userInGame == null) {
			throw new GameException("tried to answer a question without actually request it");
		}

		try {
			
			userInGame.getQuestionIdToUserChoosedAnswerIdMap().merge(questionId, userChoosedAnswerId, (v1, v2) -> { 
					// if the user already answered that questionId (a mapping for the key already exists)
					throw new GameException();
				}
			);
			
		} catch (GameException ge) {

			return new AnswerQuestionResponseDto(AnswerStatus.ALREADY_ANSWERED);
		}

		if (question.getQuestionStatus() == QuestionStatus.RESOLVED) {
			
			Optional<Integer> correctAnswerIdOptional = question.getCorrectAnswerIdOptional();

			if (correctAnswerIdOptional.isPresent()) {
				
				if (userInGame.addPointsIfAnsweredCorrect(questionId, correctAnswerIdOptional.get(), POINTS_FOR_CORRECT_ANSWER)) {
					
					return new AnswerQuestionResponseDto(AnswerStatus.CORRECT, POINTS_FOR_CORRECT_ANSWER);
					
				} else {
					
					return new AnswerQuestionResponseDto(AnswerStatus.WRONG);
				}
			} else {
				// we should not arrive here
				throw new GameException("couldn't find the correct answer for a Resolved questionId: " + questionId);
			}
		} else if (question.getQuestionStatus() == QuestionStatus.PENDING) {
			
			synchronized (lock) {
				
				if (question.getQuestionStatus() == QuestionStatus.PENDING) {
					
					question.incTotalUserAnswered(userChoosedAnswerId);
					
					resolver.resolveAndSetStatus(question);
					
					if (question.getQuestionStatus() == QuestionStatus.RESOLVED) {
						
						Optional<Integer> correctAnswerIdOptional = question.getCorrectAnswerIdOptional();

						if (correctAnswerIdOptional.isPresent()) {
							// update points for users who previously answered correctly that questionId
							usernameToUserInGameMap.values().stream().forEach(
									userInGame2 -> userInGame2.addPointsIfAnsweredCorrect(questionId,
																correctAnswerIdOptional.get(), POINTS_FOR_CORRECT_ANSWER)
							);
						}
					}
				}
			}
			
			if (question.getQuestionStatus() == QuestionStatus.RESOLVED) {
				
				Optional<Integer> correctAnswerIdOptional = question.getCorrectAnswerIdOptional();
				
				if (correctAnswerIdOptional.isPresent()) {
					
					if (userChoosedAnswerId == correctAnswerIdOptional.get()) {
						return new AnswerQuestionResponseDto(AnswerStatus.CORRECT, POINTS_FOR_CORRECT_ANSWER);
					} else {
						return new AnswerQuestionResponseDto(AnswerStatus.WRONG);
					}
				} else {
					// we should not arrive here
					throw new GameException("couldn't find the correct answer for a Resolved questionId: " + questionId);
				}
			} else if (question.getQuestionStatus() == QuestionStatus.PENDING) {
				
				return new AnswerQuestionResponseDto(AnswerStatus.PENDING);
				
			} else if (question.getQuestionStatus() == QuestionStatus.UNRESOLVED) {
				
				return new AnswerQuestionResponseDto(AnswerStatus.UNRESOLVED);
			}
		}
		
		// we should not arrive here
		return new AnswerQuestionResponseDto(AnswerStatus.UNKNOWN);
	}
	
	public LeaderboardResponseDto getGameLeaderboard() {
		
		List<UserAndPointsResponseDto> userAndPointsList = usernameToUserInGameMap.entrySet().stream()
				.map(entry -> new UserAndPointsResponseDto(entry.getKey(), entry.getValue().getPoints()))
				.sorted(Comparator.comparingInt(UserAndPointsResponseDto::getPoints).reversed())
				.collect(Collectors.toList());
		
		return new LeaderboardResponseDto(userAndPointsList);
	}

	@Override
	public String toString() {
		return "Game [POINTS_FOR_CORRECT_ANSWER=" + POINTS_FOR_CORRECT_ANSWER + ", questionList="
				+ questionList + ", resolver=" + resolver + ", usernameToUserInGameMap=" + usernameToUserInGameMap
				+ "]";
	}

	private static class UserInGame {
		
		private AtomicInteger points;
		private Map<Integer, Integer> questionIdToUserChoosedAnswerIdMap;
		
		private UserInGame() {
			
			points = new AtomicInteger(0);
			questionIdToUserChoosedAnswerIdMap = new ConcurrentHashMap<>();
		}

		private int getPoints() {
			
			return points.get();
		}
		
		private boolean addPointsIfAnsweredCorrect(int questionId, int correctAnswerId, int pointsForCorrectAnswer) {
			
			int userChoosedAnswerId = questionIdToUserChoosedAnswerIdMap.get(questionId);
			
			if (userChoosedAnswerId == correctAnswerId) {
				
				points.addAndGet(pointsForCorrectAnswer);
				
				return true;
			}
			
			return false;
		}
		
		private Integer getUserChoosedAnswerId(int questionId) {
			
			return questionIdToUserChoosedAnswerIdMap.get(questionId);
		}

		public Map<Integer, Integer> getQuestionIdToUserChoosedAnswerIdMap() {
			return questionIdToUserChoosedAnswerIdMap;
		}
	}
}
