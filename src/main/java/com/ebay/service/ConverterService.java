package com.ebay.service;

import com.ebay.dto.GameDto;
import com.ebay.dto.QuestionDto;
import com.ebay.model.Game;
import com.ebay.model.Question;
import com.ebay.resolver.Resolver;

public interface ConverterService {

	Game toModel(GameDto gameDto, Resolver resolver);
	QuestionDto toDto(Question question);
}
