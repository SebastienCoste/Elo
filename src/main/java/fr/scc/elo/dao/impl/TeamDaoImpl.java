package fr.scc.elo.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.scc.elo.bdd.TeamRepository;
import fr.scc.elo.dao.TeamDao;
import fr.scc.elo.model.Team;
import fr.scc.elo.model.player.Player;


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
public class TeamDaoImpl implements TeamDao {


	@Autowired
	private TeamRepository teamRepository;

	@Override
	public List<Team> getAllTeams() {
		return teamRepository.findAll();
	}

	@Override
	public Team createOrUpdateTeam(String tag, String name) {

		Team found = teamRepository.findByTag(tag);
		if (found == null){
			return teamRepository.insert(new Team(name, tag));
		}
		if (!StringUtils.equals(name, found.getName())){
			found.setName(name);
			return teamRepository.save(found);
		}
		return found;
	}

	@Override
	public Team createOrUpdateTeam(String tag, String name, Player teamLeader, List<Player> members) {
		Team found = teamRepository.findByTag(tag);
		if (found != null){
			found.setName(name);
			found.setMembers(members);
			found.setTeamLeader(teamLeader);
			return teamRepository.save(found);
		}
		return teamRepository.insert(new Team(name, tag, teamLeader, members));
	}

	@Override
	public void deleteTeam(String tag) {
		deleteTeam(new Team(tag));
	}
	@Override
	public void deleteTeam(Team team) {
		teamRepository.delete(team);
	}

	@Override
	public Team getTeamByTag(String tag) {
		return teamRepository.findByTag(tag);
	}

	@Override
	public Team save(Team team) {
		return teamRepository.save(team);
	}

}
