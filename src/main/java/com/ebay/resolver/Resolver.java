package com.ebay.resolver;

import com.ebay.model.Question;

public interface Resolver {

	void resolveAndSetStatus(Question question);
}
