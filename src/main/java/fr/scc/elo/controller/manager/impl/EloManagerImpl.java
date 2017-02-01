package fr.scc.elo.controller.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import fr.scc.elo.controller.manager.EloManager;
import fr.scc.elo.exception.EloException;
import fr.scc.elo.exception.MissingRequestArgumentException;
import fr.scc.elo.exception.PlayerNotFoundException;
import fr.scc.elo.exception.TeamNotFoundException;
import fr.scc.elo.model.Team;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.match.Match;
import fr.scc.elo.model.match.MatchMaking;
import fr.scc.elo.model.match.MatchResponse;
import fr.scc.elo.model.player.Player;
import fr.scc.elo.model.player.PlayerCompletion;
import fr.scc.elo.service.EloCoreService;
import fr.scc.elo.service.EloService;

/**
 * Copyright (C) 2015 CHERRIER-COSTE Sébastien

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * @author Static
 *
 */

@Component
public class EloManagerImpl implements EloManager{ 

	@Autowired
	private EloService eloService;
	@Autowired
	private EloCoreService eloCoreService;

	@Override
	public Player managePlayer(String name, String elo, String eloType, Boolean save, Boolean delete) throws EloException {
		if (!delete){
			return showAndUpdatePlayer(name, elo, eloType, save);
		}
		return deletePlayer(name);
	}

	@Override
	public String manageTeam(String tag, String name, String teamLeader, String members, Boolean createPlayers, Boolean save, Boolean delete)
			throws EloException {
		if (!delete){
			return showAndUpdateTeam(tag, name, teamLeader, members, createPlayers, save);
		}
		return deleteTeam(tag);
	}

	@Override
	public MatchResponse manageMatch(String winnerList, String looserList, String score, String typeMatch, Boolean createPlayers) 
			throws EloException {
		Match match = new Match();
		match.setWinner(fillTeam(winnerList, createPlayers));
		match.setLooser(fillTeam(looserList, createPlayers));
		match.setDate(new Date());
		match.setTypeMatch(EloType.fromName(typeMatch));
		String[] scores = score.split("-");
		if (score != null && scores.length == 2 
				&& NumberUtils.isNumber(scores[0]) && NumberUtils.isNumber(scores[1])){
			match.setScoreL(Math.min(Integer.valueOf(scores[0]), Integer.valueOf(scores[1])));
			match.setScoreW(Math.max(Integer.valueOf(scores[0]), Integer.valueOf(scores[1])));
		}
		List<Player> playersUpdated = eloCoreService.treatOneMatch(match);
		playersUpdated = eloService.saveAllPlayer(playersUpdated);
		return MatchResponse.builder().playersUpdated(playersUpdated).build();
	}

	private Team fillTeam(String playerList, Boolean createPlayers) throws EloException {
		String[] memberList = playerList.split(",");
		Team team = new Team();
		for (String playerName : memberList) {
			Player player = eloService.getPlayer(playerName);
			if (player == null){
				if (createPlayers){
					player = eloService.createPlayer(playerName);
				} else {
					throw new PlayerNotFoundException().withReason(playerName);
				}
			}
			team.addMember(player);
		}
		return team;
	}


	private String deleteTeam(String tag) throws TeamNotFoundException{
		Team  team = eloService.getTeam(tag);
		if (team == null){
			throw new TeamNotFoundException();
		}
		eloService.deleteTeam(team);

		return tag + " #{deleted}";
	}

	private String showAndUpdateTeam(String tag, String name, String teamLeader, String members, 
			Boolean createPlayers, Boolean save) throws EloException {
		boolean modified = false;
		Team  team = eloService.getTeam(tag);
		if (!save){
			if (team == null){
				throw new TeamNotFoundException().withReason(tag);
			} 
			return team.toString();
		}
		if (team == null){
			team = eloService.createTeam(name, tag);
		}
		if (StringUtils.isNotEmpty(name) && !StringUtils.equals(name, team.getName())){
			team.setName(name);
			modified = true;
		}
		if (StringUtils.isNotBlank(teamLeader)){
			Player tl = eloService.getPlayer(teamLeader);
			if (tl == null){
				if (createPlayers){
					eloService.createPlayer(teamLeader);
				} else {
					throw new PlayerNotFoundException().withReason(teamLeader);
				}
			}
			if (!tl.equals(team.getTeamLeader())){
				team = eloService.setTeamLeader(tag, teamLeader);
			}
		}

		if (StringUtils.isNotBlank(members)){
			String[] memberList = members.split(",");
			if (createPlayers){
				team = eloService.addMemberCreatePlayerIfNotExists(tag, memberList);
			} else {
				team = eloService.addMember(tag, memberList);
			}
		}
		if (modified){
			eloService.saveTeam(team);
		}
		return team.toString();
	}


	private Player deletePlayer(String name) throws EloException{
		Player player = eloService.getPlayer(name);
		if (player == null){
			throw new PlayerNotFoundException().withReason(name);
		}
		eloService.deletePlayer(player);
		return Player.builder().name(name + " #{deleted}").build();
	}

	@Override
	public Player setEloToPlayer(String name, String elo, String eloType, Boolean save) throws EloException {

		Player player = eloService.getPlayer(name);
		if (player == null){
			if (!save){
				throw new PlayerNotFoundException();
			}
			player = new Player(name);
		}
		if (NumberUtils.isNumber(elo)){
			player.setEloValue(EloType.fromName(eloType), Integer.valueOf(elo));
			eloService.savePlayer(player);
		}

		return player;
	}

	private Player showAndUpdatePlayer(String name, String elo, String eloType, Boolean save)
			throws EloException {
		boolean modified = false;
		Player player = eloService.getPlayer(name);
		if (player == null){
			if (!save){
				throw new PlayerNotFoundException();
			}
			player = new Player(name);
			modified = true;
		}
		if (elo != null && NumberUtils.isNumber(elo)){
			Integer scoreElo = player.getElo(EloType.fromName(eloType));
			Integer newElo = Integer.valueOf(elo);
			if (!scoreElo.equals(newElo)){
				player.getElo().setEloValue(EloType.E5V5, newElo);
				modified = true;
			}
		} else if (eloType != null){
			throw new MissingRequestArgumentException();
		}
		if (save && modified){
			eloService.savePlayer(player);
		}

		return player;
	}

	@Override
	public MatchMaking matchMaker(String playersString, String eloType, Boolean createPlayers) throws EloException {

		if (StringUtils.isBlank(playersString)){
			return null;
		}
		List<String> playerList = Arrays.asList(StringUtils.lowerCase(playersString).split(","));
		List<Player> players = new ArrayList<>();
		playerList.forEach(p -> players.add(eloService.createPlayer(p)));

		Team teamBlue = Team.builder().members(new ArrayList<>()).build();
		Team teamRed = Team.builder().members(new ArrayList<>()).build();
		players.forEach(p -> addPlayer(p, teamBlue, teamRed));

		return equilibrage(teamBlue, teamRed, EloType.fromName(eloType));


	}

	private MatchMaking equilibrage(Team teamBlue, Team teamRed, EloType eloType) {


		List<Player> mb = teamBlue.getMembers();
		List<Player> mr = teamRed.getMembers();
		int nbBlue = mb.size();
		int nbRed = mr.size();
		int itB = 0;

		while (itB < nbBlue){
			int itR = 0;
			boolean restart = false;
			while (!restart && itR < nbRed){
				List<Player> copyBlue = new ArrayList<>(mb);
				List<Player> copyRed = new ArrayList<>(mr);
				Player tampon = copyBlue.get(itB);
				copyBlue.set(itB, copyRed.get(itR));
				copyRed.set(itR, tampon);
				if (isNewTeamBetter(mb, mr, copyBlue, copyRed, eloType)){
					mb = copyBlue;
					mr = copyRed;
					itB = 0;
					restart = true;
				}
				itR++;
			}
			if (!restart){
				itB++;
			} else {
				itB = 0;
			}
		}
		teamBlue.setMembers(mb);
		teamRed.setMembers(mr);

		return MatchMaking.builder().type(eloType).teamBlue(teamBlue).teamRed(teamRed).build();
	}

	private boolean isNewTeamBetter(List<Player> mb, List<Player> mr
			, List<Player> copyBlue, List<Player> copyRed, EloType eloType) {
		Integer elocb = computeElo(copyBlue, eloType);
		Integer elocr = computeElo(copyRed, eloType);
		Integer elomb = computeElo(mb, eloType);
		Integer elomr = computeElo(mr, eloType);


		Double rapportNew = Math.abs(1.0 -((double)elocb/(double)elocr));
		Double rapportOld = Math.abs(1.0 - ((double)elomb/(double)elomr));

		return rapportNew < rapportOld;
	}

	private Integer computeElo(List<Player> players, EloType eloType) {
		return Integer.valueOf(players.stream().mapToInt(p -> p.getEloMoyenne(eloType)).sum() / players.size());
	}

	private Object addPlayer(Player p, Team teamBlue, Team teamRed) {

		if (teamBlue.getMembers().size() < teamRed.getMembers().size()){
			teamBlue.addMember(p);
		} else {
			teamRed.addMember(p);
		}
		return null;
	}

	@Override
	public List<PlayerCompletion> getAllPlayersForCompletion(String term) {

		List<Player> allPlayers = eloService.getAllPlayers();

		return allPlayers.stream().filter(p -> contains(term, p))
							.map(p -> PlayerCompletion.builder()
											.id(p.getName())
											.label(p.getName())
											.value(p.getName())
											.build())
							.collect(Collectors.toList());
	}

	private boolean contains(String term, Player p) {
		return StringUtils.containsIgnoreCase(p.getName(),term);
	}




}
