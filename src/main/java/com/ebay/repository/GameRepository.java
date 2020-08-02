package com.ebay.repository;

import com.ebay.model.Game;

public interface GameRepository {

	int save(Game game);
	Game getById(int id);
	void deleteAll();
}
