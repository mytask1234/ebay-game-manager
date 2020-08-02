package com.ebay.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import com.ebay.exception.GameException;
import com.ebay.model.Game;

@Repository
public class GameRepositoryImpl implements GameRepository {

	private final Map<Integer, Game> gameIdToGameMap;
	private final AtomicInteger nextGameId;
	
	public GameRepositoryImpl() {
		
		gameIdToGameMap = new ConcurrentHashMap<>();
		nextGameId = new AtomicInteger(0);
	}
	
	@Override
	public int save(Game game) {

		int gameId = getNextGameId();
		
		gameIdToGameMap.put(gameId, game);
		
		return gameId;
	}

	@Override
	public Game getById(int id) {

		Game game = gameIdToGameMap.get(id);

		if (game == null) {
			throw new GameException("failed to find Game with id: " + id);
		}

		return game;
	}
	
	@Override
	public void deleteAll() {

		gameIdToGameMap.clear();
		nextGameId.set(0);
	}
	
	private int getNextGameId() {
		
		return nextGameId.incrementAndGet();
	}
}
