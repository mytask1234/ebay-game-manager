package com.ebay.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ebay.dto.AnswerQuestionRequestDto;
import com.ebay.dto.AnswerStatus;
import com.ebay.dto.GameDto;
import com.ebay.dto.QuestionDto;
import com.ebay.dto.QuestionPossibleAnswerDto;
import com.ebay.exception.GameException;
import com.ebay.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameControllerIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameControllerIntegrationTest.class);

	private static final String BASE_ENDPOINT = "/api/v1/games/";
	
	private static final int POINTS_FOR_CORRECT_ANSWER = 10;
	
	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Before
	public void deleteAll() {

		LOGGER.debug("delete all games before each test");
		
		gameRepository.deleteAll();
	}

	@Test
	public void whenSaveGame_thenGetOnRetrieval() throws Exception {

		int gameId = saveGameWithOneQuestion();
		
		assertEquals(1, gameId);
	}
	
	@Test
	public void whenSaveGame_thenGetQuestion() throws Exception {

		int gameId = saveGameWithOneQuestion();
		
		//-----------------------------------------------------------------
		
		String username = "user1";
		
		MvcResult mvcResult = mockMvc.perform(get(BASE_ENDPOINT+gameId+"/username/"+username)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(0))
				.andExpect(jsonPath("$.questionText").value("Question 1 ?"))
				.andExpect(jsonPath("$.questionPossibleAnswers").isArray())
				.andExpect(jsonPath("$.questionPossibleAnswers", hasSize(3)))
				.andDo(print())
				.andReturn();

		String expectedResponseBody = "{\"id\":0,\"questionText\":\"Question 1 ?\",\"questionPossibleAnswers\":[{\"id\":0,\"possibleAnswer\":\"Answer 1.\"},{\"id\":1,\"possibleAnswer\":\"Answer 2.\"},{\"id\":2,\"possibleAnswer\":\"Answer 3.\"}]}";

		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, true);
		
		//-----------------------------------------------------------------
		
		int questionId = 0;
		int userChoosedAnswerId = 1;
		AnswerStatus expectedAnswerStatus = AnswerStatus.PENDING;
		int expectedPointsEarned = 0;
		
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		//-----------------------------------------------------------------
		
		// when the user ask for another question he get error response, because he already answered it and this game has only 1 question (so no more questions to return).
		
		mockMvc.perform(get(BASE_ENDPOINT+gameId+"/username/"+username)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("No available questions for this game. Please try another game."))
				.andExpect(jsonPath("$.id").doesNotExist())
				.andExpect(jsonPath("$.questionText").doesNotExist())
				.andExpect(jsonPath("$.questionPossibleAnswers").doesNotExist())
				.andDo(print());
	}
	
	@Test
	public void whenUsersAnswerQuestion_thenGetExpectedAnswerQuestionResponseDto() throws Exception {

		int gameId = saveGameWithOneQuestion();
		
		//-----------------------------------------------------------------
		
		int questionId = 0;
		int userChoosedAnswerId = 1;
		AnswerStatus expectedAnswerStatus = AnswerStatus.PENDING;
		int expectedPointsEarned = 0;
		
		String username = "user1";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		username = "user2";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		//-----------------------------------------------------------------
		
		userChoosedAnswerId = 2;
		
		username = "user3";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		username = "user4";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		username = "user5";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		username = "user6";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		username = "user7";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		//-----------------------------------------------------------------
		
		expectedAnswerStatus = AnswerStatus.CORRECT;
		expectedPointsEarned = POINTS_FOR_CORRECT_ANSWER;
		
		username = "user8";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		//-----------------------------------------------------------------
		
		expectedAnswerStatus = AnswerStatus.ALREADY_ANSWERED;
		expectedPointsEarned = 0;
		
		username = "user5";
		getQuestionDto(gameId, username, HttpStatus.INTERNAL_SERVER_ERROR);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		username = "user8";
		getQuestionDto(gameId, username, HttpStatus.INTERNAL_SERVER_ERROR);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		//-----------------------------------------------------------------
		
		expectedAnswerStatus = AnswerStatus.CORRECT;
		expectedPointsEarned = POINTS_FOR_CORRECT_ANSWER;
		
		username = "user9";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		//-----------------------------------------------------------------
		
		userChoosedAnswerId = 1;
		expectedAnswerStatus = AnswerStatus.WRONG;
		expectedPointsEarned = 0;
		
		username = "user10";
		getQuestionDto(gameId, username, HttpStatus.OK);
		answerQuestion(gameId, username, questionId, userChoosedAnswerId,
					   expectedAnswerStatus, expectedPointsEarned);
		
		//-----------------------------------------------------------------
		
		MvcResult mvcResult = mockMvc.perform(get(BASE_ENDPOINT+gameId+"/leaderboard")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userAndPointsPairs").isArray())
				.andExpect(jsonPath("$.userAndPointsPairs", hasSize(10)))
				.andDo(print())
				.andReturn();

		String expectedResponseBody = "{\"userAndPointsPairs\":[{\"username\":\"user9\",\"points\":10},{\"username\":\"user7\",\"points\":10},{\"username\":\"user8\",\"points\":10},{\"username\":\"user5\",\"points\":10},{\"username\":\"user6\",\"points\":10},{\"username\":\"user3\",\"points\":10},{\"username\":\"user4\",\"points\":10},{\"username\":\"user1\",\"points\":0},{\"username\":\"user2\",\"points\":0},{\"username\":\"user10\",\"points\":0}]}";

		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, true);
	}
	
	@Test(expected = GameException.class)
	public void whenGetGameThatNotExists_thenGameExceptionIsThrown() {
	    
		int gameId = 1;
		
		gameRepository.getById(gameId);
	}
	
	private void getQuestionDto(int gameId, String username, HttpStatus httpStatus) throws Exception {
		
		mockMvc.perform(get(BASE_ENDPOINT+gameId+"/username/"+username)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(httpStatus.value())) //.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	private void answerQuestion(int gameId, String username, int questionId, int userChoosedAnswerId,
								AnswerStatus expectedAnswerStatus, int expectedPointsEarned) throws Exception {

		mockMvc.perform(post(BASE_ENDPOINT+gameId+"/username/"+username)
				.content(objectMapper.writeValueAsString(new AnswerQuestionRequestDto(questionId, userChoosedAnswerId)))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.answerStatus").value(expectedAnswerStatus.toString()))
				.andExpect(jsonPath("$.pointsEarned").value(expectedPointsEarned))
				.andDo(print());
	}

	private int saveGameWithOneQuestion() throws Exception {
		
		int gameId = 1;
		
		GameDto gameDto = getGameWithOneQuestion();

		saveGame(gameDto, gameId);
		
		assertNotNull(gameRepository.getById(gameId));
		
		return gameId;
	}
	
	private void saveGame(GameDto gameDto, int id) throws Exception {

		MvcResult result = mockMvc.perform(post(BASE_ENDPOINT)
				.content(objectMapper.writeValueAsString(gameDto))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print())
				.andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals("http://localhost"+BASE_ENDPOINT+id, response.getHeader(HttpHeaders.LOCATION));
	}

	private GameDto getGameWithOneQuestion() {

		List<QuestionDto> questions = new ArrayList<QuestionDto>();
		
		questions.add(
				getQuestion("Question 1 ?", Arrays.asList("Answer 1.", "Answer 2.", "Answer 3."))
			);
		
		return new GameDto(POINTS_FOR_CORRECT_ANSWER, questions);
	}
	
	private QuestionDto getQuestion(String questionText, List<String> questionPossibleAnswersList) {
		
		List<QuestionPossibleAnswerDto> questionPossibleAnswers = questionPossibleAnswersList.stream()
				.map(possibleAnswer -> new QuestionPossibleAnswerDto(possibleAnswer)).collect(Collectors.toList());
		
		return new QuestionDto(questionText, questionPossibleAnswers);
	}
}
