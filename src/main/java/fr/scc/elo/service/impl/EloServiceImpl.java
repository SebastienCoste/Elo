package fr.scc.elo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.scc.elo.exception.EloException;
import fr.scc.elo.exception.PlayerNotFoundException;
import fr.scc.elo.exception.TeamNotFoundException;
import fr.scc.elo.model.Team;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.player.Player;
import fr.scc.elo.service.EloService;
import fr.scc.elo.service.PlayerService;
import fr.scc.elo.service.TeamService;

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
public class EloServiceImpl implements EloService {


	@Autowired
	private PlayerService playerService;
	@Autowired
	private TeamService teamService;
	
	
	@Override
	public Team createTeam(String name, String tag) {
		return teamService.createTeam(name, tag);
	}
	@Override
	public Team setTeamLeader(String tag, String teamLeaderName) throws EloException {
		Player tl = getPlayer(teamLeaderName);
		if (tl != null){
			return teamService.setTeamLeaderCreateIfNotExists(tag, tl);
		} else {
			throw new PlayerNotFoundException();
		}
	}
	@Override
	public Team addMemberCreatePlayerIfNotExists(String tag, String... memberList) throws TeamNotFoundException {
		if (memberList != null && memberList.length > 0){
			List<Player> members = new ArrayList<>();
			for (String memberName : memberList) {
				Player member = getPlayer(memberName);
				if (member == null){
					member = playerService.createPlayer(memberName);
				}
				members.add(member);
			}
			return teamService.addMembers(tag, members.toArray(new Player[memberList.length]));
		}
		return null;
	}
	
	@Override
	public Team addMember(String tag, String... memberList) throws EloException {
		if (memberList != null && memberList.length > 0){
			List<Player> members = new ArrayList<>();
			for (String memberName : memberList) {
				Player member = getPlayer(memberName);
				if (member == null){
					throw new PlayerNotFoundException();
				}
				members.add(member);
			}
			return teamService.addMembers(tag, members.toArray(new Player[memberList.length]));
		}
		return null;
	}
	
	@Override
	public Integer getAverageTeamElo(String tag, EloType eloType) throws TeamNotFoundException{

		Team team = getTeam(tag);
		if (team != null){
			List<Player> members = team.getMembers();
			if (members != null && members.size() > 0){
				Integer totalElo = members.stream().mapToInt(e -> e.getEloMoyenne(eloType)).sum();
				return totalElo / members.size();
			}
			return eloType.getDefaultValue();
		}
		throw new TeamNotFoundException();
	}

	@Override
	public List<Player> getAllPlayers() {
		return playerService.getAllPlayers();
	}
	
	@Override
	public List<Player> removeTest() {
		playerService.getAllPlayers().stream()
		.filter(p -> !StringUtils.contains(p.getName(), ".")
				|| !StringUtils.equals(p.getName(), StringUtils.lowerCase(p.getName())))
		.forEach(player -> playerService.deletePlayer(player));
		return playerService.getAllPlayers();
	}
	
	@Override
	public List<Team> getAllTeams() {
		return teamService.getAllTeams();
	}
	@Override
	public Team removeMember(String tag, String memberName) throws TeamNotFoundException{
		return teamService.removeMember(tag, new Player(memberName));
	}

	@Override
	public Team getTeam(String tag) {
		return teamService.getTeam(tag);
	}
	@Override
	public Player getPlayer(String name) {
		return playerService.getPlayer(name);
	}
	@Override
	public Player createPlayer(String name) {
		return playerService.createPlayer(name);
	}
	@Override
	public Player savePlayer(Player player) {
		return playerService.savePlayer(player);
	}

	@Override
	public void deletePlayer(Player player) {
		playerService.deletePlayer(player);
	}
	@Override
	public Team saveTeam(Team team) {
		return teamService.saveTeam(team);
	}
	@Override
	public void deleteTeam(Team team) {
		teamService.deleteTeam(team);
	}
	@Override
	public List<Player> saveAllPlayer(Collection<Player> player) {
		return playerService.saveAllPlayer(player);
	}


}
