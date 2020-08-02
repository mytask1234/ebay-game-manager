package com.ebay.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.dto.AnswerQuestionRequestDto;
import com.ebay.dto.AnswerQuestionResponseDto;
import com.ebay.dto.GameDto;
import com.ebay.dto.LeaderboardResponseDto;
import com.ebay.dto.QuestionDto;
import com.ebay.service.GameService;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

	@Autowired
	private GameService gameService;

	@PostMapping("/")
	public ResponseEntity<Void> createGame(@RequestBody GameDto gameDto) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("gameDto={}", gameDto);
		}

		int gameId = gameService.save(gameDto);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("gameId={}", gameId);
		}
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(gameId)
                .toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{gameId}/username/{username}")
	public QuestionDto getQuestionDto(@PathVariable("gameId") final int gameId,
			@PathVariable("username") final String username) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("gameId={}, username={}", gameId, username);
		}

		QuestionDto questionDto = gameService.getQuestionByUsername(gameId, username);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("questionDto={}", questionDto);
		}

		return questionDto;
	}

	@PostMapping("/{gameId}/username/{username}")
	@ResponseStatus(HttpStatus.CREATED)
	public AnswerQuestionResponseDto answerQuestion(@PathVariable("gameId") final int gameId,
			@PathVariable("username") final String username,
			@RequestBody final AnswerQuestionRequestDto answerQuestionRequestDto) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("gameId={}, username={}, answerQuestionRequestDto={}", gameId, username, answerQuestionRequestDto);
		}

		AnswerQuestionResponseDto answerQuestionResponseDto = gameService.userAnswerQuestion(gameId, username, answerQuestionRequestDto);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("answerQuestionResponseDto={}", answerQuestionResponseDto);
		}

		return answerQuestionResponseDto;
	}

	@GetMapping("/{gameId}/leaderboard")
	public LeaderboardResponseDto getLeaderboard(@PathVariable("gameId") final int gameId) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("gameId={}", gameId);
		}

		LeaderboardResponseDto leaderboardResponseDto = gameService.getLeaderboard(gameId);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("leaderboardResponseDto={}", leaderboardResponseDto);
		}

		return leaderboardResponseDto;
	}
}
