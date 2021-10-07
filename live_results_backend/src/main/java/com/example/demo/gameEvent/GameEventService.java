package com.example.demo.gameEvent;

import com.example.demo.game.Game;
import com.example.demo.game.GameService;
import com.example.demo.gameEvent.dto.FullEventRequest;
import com.example.demo.gameEvent.dto.IncompleteEventRequest;
import com.example.demo.gameEvent.dto.UpdateEventRequest;
import com.example.demo.player.Player;
import com.example.demo.player.PlayerService;
import com.example.demo.team.Team;
import com.example.demo.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class GameEventService {

    private final GameEventRepository gameEventRepository;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final GameService gameService;

    public List<GameEvent> findAllByGame(Long gameId){
        Game game = this.gameService.getGame(gameId);

        if(game != null){
            return this.gameEventRepository.findAllByGame(game);
        }else{
            return null;
        }
    }

    @Transactional
    public void deleteById(Long id){
        this.gameEventRepository.deleteById(id);
    }

    public void updateEvent(UpdateEventRequest request){
        if(this.gameEventRepository.findById(request.getEventId()).isEmpty()){
            throw new IllegalStateException("Event doesn't exist");
        }

        GameEvent event = this.gameEventRepository.findById(request.getEventId()).get();

        Player player = this.playerService.getPlayer(request.getPlayerId());
        Team team = this.teamService.getTeam(request.getTeamId());

        event.setEventMinute(request.getMinute());
        event.setGameEventType(this.gameEventTypeFromString(request.getEventType()));
        event.setPlayer(player);
        event.setTeam(team);

        this.gameEventRepository.save(event);
    }

    public void addIncompleteGameEvent(IncompleteEventRequest request){
        GameEventType eventType = this.gameEventTypeFromString(request.getEventType());

        Team team = this.teamService.getTeam(request.getTeamId());
        Game game = this.gameService.getGame(request.getGameId());

        GameEvent newGameEvent = new GameEvent(request.getMinute(), eventType, team, game);

        team.addGameEvent(newGameEvent);
        game.addGameEvent(newGameEvent);

        this.gameEventRepository.save(newGameEvent);
    }

    public void addFullGameEvent(FullEventRequest request){
        GameEventType eventType = this.gameEventTypeFromString(request.getEventType());

        Team team = this.teamService.getTeam(request.getTeamId());
        Game game = this.gameService.getGame(request.getGameId());
        Player player = this.playerService.getPlayer(request.getPlayerId());

        GameEvent newGameEvent = new GameEvent(request.getMinute(), eventType, team, game, player);

        team.addGameEvent(newGameEvent);
        game.addGameEvent(newGameEvent);
        player.addGameEvent(newGameEvent);

        this.gameEventRepository.save(newGameEvent);
    }

    private GameEventType gameEventTypeFromString(String eventType){
        if(eventType.equalsIgnoreCase("goal")){
            return GameEventType.GOAL;
        }else if(eventType.equalsIgnoreCase("substitution_on")){
            return GameEventType.SUBSTITUTION_ON;
        }else if(eventType.equalsIgnoreCase("substitution_of")){
            return GameEventType.SUBSTITUTION_OF;
        }
        else if(eventType.equalsIgnoreCase("yellow_card")){
            return GameEventType.YELLOW_CARD;
        }else{
            return GameEventType.RED_CARD;
        }
    }

}
