package fr.scc.elo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import fr.scc.elo.dao.TeamDao;
import fr.scc.elo.exception.TeamNotFoundException;
import fr.scc.elo.model.Team;
import fr.scc.elo.model.player.Player;
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
@EnableAutoConfiguration
public class TeamServiceImpl implements TeamService{

	@Autowired
	private TeamDao teamDao;

	@Override
	public List<Team> getAllTeams() {
		return teamDao.getAllTeams();
	}

	@Override
	public Team createTeam(String name, String tag) {
		return teamDao.createOrUpdateTeam(tag, name);
	}

	@Override
	public Team setTeamLeaderCreateIfNotExists(String tag, Player teamLeader) {
		Team team = prepareTeam(tag);
		team.setTeamLeader(teamLeader);
		addMemberInTeam(team, teamLeader);
		return teamDao.save(team);

	}

	@Override
	public Team addMembers(String tag, Player... members) throws TeamNotFoundException{
		Team team = getTeam(tag);
		if (team == null){
			throw new TeamNotFoundException();
		}
		if (addMemberInTeam(team, members)){
			return teamDao.save(team);
		}
		return team;
	}
	@Override
	public Team removeMember(String tag, Player member) throws TeamNotFoundException{
		Team team = getTeam(tag);
		if (team == null){
			throw new TeamNotFoundException();
		}
		List<Player> members = team.getMembers();
		if (members != null && members.remove(member)){
			return teamDao.save(team);
		}
		return team;
	}

	@Override
	public Team getTeam(String tag) {
		return teamDao.getTeamByTag(tag);
	}

	private Team prepareTeam(String tag){
		Team team = getTeam(tag);
		if (team == null){
			team = new Team(tag);
		}
		return team;
	}

	/**
	 * 
	 * @param player
	 * @param team
	 * @return true si la liste des membres a change
	 */
	private boolean addMemberInTeam(Team team, Player... playerList) {
		List<Player> members = team.getMembers();
		if (members == null){
			members = new ArrayList<>();
			team.setMembers(members);
		} 
		boolean update = false;
		for (Player player : playerList) {
			if (!members.contains(player)){
				members.add(player);
				update = true;
			}
		}
		return update;
	}

	@Override
	public Team saveTeam(Team team) {
		return teamDao.save(team);
	}

	@Override
	public void deleteTeam(Team team) {
		teamDao.deleteTeam(team);
	}
}
