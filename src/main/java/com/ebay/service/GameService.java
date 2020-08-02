package com.ebay.service;

import com.ebay.dto.AnswerQuestionRequestDto;
import com.ebay.dto.AnswerQuestionResponseDto;
import com.ebay.dto.GameDto;
import com.ebay.dto.LeaderboardResponseDto;
import com.ebay.dto.QuestionDto;

public interface GameService {

	int save(GameDto gameDto);
	QuestionDto getQuestionByUsername(int gameId, String username);
	AnswerQuestionResponseDto userAnswerQuestion(int gameId, String username, AnswerQuestionRequestDto answerQuestionRequestDto);
	LeaderboardResponseDto getLeaderboard(int gameId);
}
