package com.ebay.resolver;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.ebay.model.Question;
import com.ebay.model.QuestionPossibleAnswer;
import com.ebay.model.QuestionStatus;

@Component
public class MajorityVoteResolverImpl implements Resolver {

	private static final int MIN_USERS_ANSWERED = 6;
	private static final int MAX_USERS_ANSWERED = 11;
	private static final double MIN_PERCENTAGE_USER_VOTE_FOR_CORRECT_ANSWER = 75.0;
	
	@Override
	public void resolveAndSetStatus(Question question) {

		if (question.getTotalUserAnswered() < MIN_USERS_ANSWERED) {
			
			return;
			
		} else if (question.getTotalUserAnswered() > MAX_USERS_ANSWERED) {
			
			question.setQuestionStatus(QuestionStatus.UNRESOLVED);
			
		} else {
			
			List<QuestionPossibleAnswer> questionPossibleAnswerList = question.getQuestionPossibleAnswerList();
			
			OptionalInt correctAnswerIdOptionalInt = IntStream.range(0, questionPossibleAnswerList.size())
				// If an answer got more than 75% of the users, it will be determined as the correct answer
				.filter(
					i -> questionPossibleAnswerList.get(i).getTotalUserAnswered() * (double)100 / question.getTotalUserAnswered() >= MIN_PERCENTAGE_USER_VOTE_FOR_CORRECT_ANSWER
				).findFirst();
			
			if (correctAnswerIdOptionalInt.isPresent()) {

				Optional<Integer> correctAnswerIdOptional = Optional.of(correctAnswerIdOptionalInt.getAsInt());
				
				question.setQuestionStatus(QuestionStatus.RESOLVED);
				question.setCorrectAnswerIdOptional(correctAnswerIdOptional);
			}
		}
	}
}
