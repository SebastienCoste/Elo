package fr.scc.elo.service;

import java.util.Collection;
import java.util.List;

import fr.scc.elo.exception.EloException;
import fr.scc.elo.exception.TeamNotFoundException;
import fr.scc.elo.model.Team;
import fr.scc.elo.model.elo.EloType;
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

public interface EloService {
	Team createTeam(String name, String tag);
	Team setTeamLeader(String tag, String teamLeaderName) throws EloException;
	Team addMemberCreatePlayerIfNotExists(String tag, String... memberName) throws TeamNotFoundException;
	Team addMember(String tag, String... memberName) throws EloException;
	Team removeMember(String tag, String memberName) throws TeamNotFoundException;
	Integer getAverageTeamElo(String tag, EloType eloType) throws TeamNotFoundException;
	List<Team> getAllTeams();
	Team getTeam(String tag);
	Team saveTeam(Team team);
	void deleteTeam(Team team);

	List<Player> getAllPlayers();
	List<Player> removeTest();
	Player getPlayer(String name);
	Player createPlayer(String name);
	Player savePlayer(Player player);
	List<Player> saveAllPlayer(Collection<Player> player);
	void deletePlayer(Player player);
}
