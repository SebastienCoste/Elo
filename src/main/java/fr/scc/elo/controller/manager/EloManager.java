package fr.scc.elo.controller.manager;

import java.util.List;

import fr.scc.elo.exception.EloException;
import fr.scc.elo.model.match.MatchMaking;
import fr.scc.elo.model.match.MatchResponse;
import fr.scc.elo.model.player.Player;
import fr.scc.elo.model.player.PlayerCompletion;

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

public interface EloManager {


	Player managePlayer(String name, String elo, String eloType, Boolean save, Boolean delete)throws EloException;
	Player setEloToPlayer(String name, String elo, String eloType, Boolean save)throws EloException;
	String manageTeam(String tag, String name, String teamLeader, String members, Boolean createPlayers, Boolean save, Boolean delete)throws EloException;
	MatchResponse manageMatch(String winnerList, String looserList, String score, String typeMatch, Boolean createPlayers)  throws EloException;
	MatchMaking matchMaker(String players, String eloType, Boolean createPlayers)throws EloException;
	List<PlayerCompletion> getAllPlayersForCompletion(String term);
}
