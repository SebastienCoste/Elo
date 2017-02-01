package fr.scc.elo.dao;

import java.util.List;

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

public interface TeamDao {
	
	Team getTeamByTag(String tag);
	Team save(Team team);
	List<Team> getAllTeams();
	Team createOrUpdateTeam(String tag, String name);
	Team createOrUpdateTeam(String tag, String name, Player teamLeader, List<Player> members);
	void deleteTeam(String tag);
	void deleteTeam(Team team);
}
