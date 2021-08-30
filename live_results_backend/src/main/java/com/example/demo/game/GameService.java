package com.example.demo.game;

import com.example.demo.gameEvent.GameEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;

    public List<GameEvent> getGameEvents(Long gameId){
        if(this.checkIfGameNotExist(gameId)){
            throw new IllegalStateException("Game doesn't exist");
        }

        return this.gameRepository.findById(gameId).get().getGameEvents();
    }

    public void addGameEvent(Long gameId, GameEvent newGameEvent){
        if(this.checkIfGameNotExist(gameId)){
            throw new IllegalStateException("Game doesn't exist");
        }

        Game game = this.gameRepository.findById(gameId).get();

        game.addGameEvent(newGameEvent);

        this.gameRepository.save(game);
    }

    private boolean checkIfGameNotExist(Long gameId){
        return this.gameRepository.findById(gameId).isEmpty();
    }

}
