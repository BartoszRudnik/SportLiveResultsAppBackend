package com.example.demo.team;

import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.game.GameStatus;
import com.example.demo.league.League;
import com.example.demo.league.LeagueRepository;
import com.example.demo.league.LeagueService;
import com.example.demo.league.dto.GetGamesResponse;
import com.example.demo.leagueTable.LeagueTable;
import com.example.demo.leagueTable.LeagueTableRepository;
import com.example.demo.player.Player;
import com.example.demo.player.PlayerRepository;
import com.example.demo.socialMedia.SocialMedia;
import com.example.demo.socialMedia.SocialMediaRepository;
import com.example.demo.team.dto.AddTeamRequest;
import com.example.demo.team.dto.AddTeamSocialMedia;
import com.example.demo.team.dto.SingleTeamResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final LeagueRepository leagueRepository;
    private final LeagueTableRepository leagueTableRepository;
    private final LeagueService leagueService;
    private final SocialMediaRepository socialMediaRepository;

    public void addSocialMedia(AddTeamSocialMedia request){
        if(this.teamRepository.findById(request.getTeamId()).isPresent()){
            Team team = this.teamRepository.findById(request.getTeamId()).get();
            SocialMedia socialMedia = new SocialMedia(team, request.getFacebookUrl(), request.getTwitterUrl(), request.getInstagramUrl());

            team.setSocialMedia(socialMedia);

            this.socialMediaRepository.save(socialMedia);
            this.teamRepository.save(team);
        }
    }

    public Team getTeam(Long teamId){
        if(this.teamRepository.findById(teamId).isPresent()){
            return this.teamRepository.findById(teamId).get();
        }else{
            return null;
        }
    }

    public List<Player> getTeamPlayers(Long teamId){
        if(this.checkIfTeamNotExist(teamId)){
            throw new IllegalStateException("Team doesn't exist");
        }

        return this.teamRepository.findById(teamId).get().getPlayers();
    }

    public List<Game> getAllTeamGames(Long teamId){
        if(this.checkIfTeamNotExist(teamId)){
            throw new IllegalStateException("Team doesn't exist");
        }

        Team team = this.teamRepository.findById(teamId).get();

        return this.gameRepository.findAllByLeagueAndTeamAOrTeamB(team.getLeague(), team, team);
    }

    public List<GetGamesResponse> getAllTeamFinishedGames(Long teamId){
        if(this.checkIfTeamNotExist(teamId)){
            throw new IllegalStateException("Team doesn't exist");
        }

        Team team = this.teamRepository.findById(teamId).get();
        List<Game> games = this.gameRepository.findTeamFinishedGames(team, GameStatus.FINISHED);

        return this.leagueService.getGames(games);
    }

    public List<GetGamesResponse> getAllTeamScheduledGame(Long teamId){
        if(this.checkIfTeamNotExist(teamId)){
            throw new IllegalStateException("Team doesn't exist");
        }

        Team team = this.teamRepository.findById(teamId).get();
        List<Game> games = this.gameRepository.findTeamScheduledGames(team, GameStatus.SCHEDULED);

        return this.leagueService.getGames(games);
    }

    private boolean checkIfTeamNotExist(Long teamId){
        return this.teamRepository.findById(teamId).isEmpty();
    }

    public Long addTeam(AddTeamRequest request) {
        List<Player> teamPlayers = new ArrayList<>();
        League league = null;

        for(Long playerId : request.getPlayers()){
            if(this.playerRepository.findById(playerId).isPresent()){
                Player newPlayer = this.playerRepository.findById(playerId).get();

                teamPlayers.add(newPlayer);
            }
        }

        if(this.leagueRepository.findById(request.getLeagueId()).isPresent()){
            league = this.leagueRepository.findById(request.getLeagueId()).get();
        }

        Team newTeam = new Team(request.getTeamName(), request.getCity(), request.getStadiumName(), teamPlayers, league);

        this.teamRepository.save(newTeam);

        LeagueTable newLeagueTable = new LeagueTable(newTeam);
        newTeam.setLeagueTable(newLeagueTable);

        this.leagueTableRepository.save(newLeagueTable);
        this.teamRepository.save(newTeam);

        return newTeam.getId();
    }

    public List<SingleTeamResponse> getTeamsFromLeague(Long leagueId) {
        if(this.leagueRepository.findById(leagueId).isPresent()) {
            League league = this.leagueRepository.findById(leagueId).get();

            List<Team> teams = this.teamRepository.findAllByLeague(league);

            List<SingleTeamResponse> resultList = new ArrayList<>();

            for(Team team : teams){
                String facebookUrl = "";
                String twitterUrl = "";
                String instagramUrl = "";

                if(team.getSocialMedia() != null){
                    facebookUrl = team.getSocialMedia().getFacebookUrl();
                    twitterUrl = team.getSocialMedia().getTwitterUrl();
                    instagramUrl = team.getSocialMedia().getInstagramUrl();
                }

                resultList.add(new SingleTeamResponse(team.getId(), team.getTeamName(), team.getCity(), team.getStadiumName(), facebookUrl, twitterUrl, instagramUrl));
            }

            return resultList;
        }else{
            return new ArrayList<>();
        }
    }

    public SingleTeamResponse getSingleTeam(Long teamId) {
        if(this.teamRepository.findById(teamId).isPresent()){
            Team team = this.teamRepository.findById(teamId).get();

            String facebookUrl = "";
            String twitterUrl = "";
            String instagramUrl = "";

            if(team.getSocialMedia() != null){
                facebookUrl = team.getSocialMedia().getFacebookUrl();
                twitterUrl = team.getSocialMedia().getTwitterUrl();
                instagramUrl = team.getSocialMedia().getInstagramUrl();
            }

            return new SingleTeamResponse(team.getId(), team.getTeamName(), team.getCity(), team.getStadiumName(), facebookUrl, twitterUrl, instagramUrl);
        }else{
            return new SingleTeamResponse();
        }
    }
}
