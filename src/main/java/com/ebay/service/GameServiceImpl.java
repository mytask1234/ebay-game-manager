package com.ebay.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ebay.dto.AnswerQuestionRequestDto;
import com.ebay.dto.AnswerQuestionResponseDto;
import com.ebay.dto.GameDto;
import com.ebay.dto.LeaderboardResponseDto;
import com.ebay.dto.QuestionDto;
import com.ebay.exception.GameException;
import com.ebay.model.Game;
import com.ebay.model.Question;
import com.ebay.repository.GameRepository;
import com.ebay.resolver.Resolver;

@Service
public class GameServiceImpl implements GameService {

	private final GameRepository gameRepository;
	private final ConverterService converterService;
	private final Resolver resolver;

	public GameServiceImpl(GameRepository gameRepository, ConverterService converterService, Resolver resolver) {
		super();
		this.gameRepository = gameRepository;
		this.converterService = converterService;
		this.resolver = resolver;
	}

	@Override
	public int save(GameDto gameDto) {

		Game game = converterService.toModel(gameDto, resolver);

		return gameRepository.save(game);
	}

	@Override
	public QuestionDto getQuestionByUsername(int gameId, String username) {

		Game game = gameRepository.getById(gameId);

		Optional<Question> opt = game.getQuestionByUsername(username);

		if (opt.isPresent()) {

			return converterService.toDto(opt.get());
		}

		throw new GameException("No available questions for user " + username + " in game id " + gameId,
								"No available questions for this game. Please try another game.");
	}

	@Override
	public AnswerQuestionResponseDto userAnswerQuestion(int gameId, String username,
			AnswerQuestionRequestDto answerQuestionRequestDto) {

		Integer questionId = answerQuestionRequestDto.getQuestionId();

		if (questionId == null) {
			throw new GameException("field questionId can't be null");
		}

		Integer userChoosedAnswerId = answerQuestionRequestDto.getUserChoosedAnswerId();

		if (userChoosedAnswerId == null) {
			throw new GameException("field userChoosedAnswerId can't be null");
		}

		Game game = gameRepository.getById(gameId);

		return game.userAnswerQuestion(username, questionId, userChoosedAnswerId);
	}

	@Override
	public LeaderboardResponseDto getLeaderboard(int gameId) {

		Game game = gameRepository.getById(gameId);

		return game.getGameLeaderboard();
	}
}
