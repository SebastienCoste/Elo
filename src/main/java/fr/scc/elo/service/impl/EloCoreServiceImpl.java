package fr.scc.elo.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import fr.scc.elo.core.EloCore;
import fr.scc.elo.model.elo.EloEvolution;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.match.Match;
import fr.scc.elo.model.player.Player;
import fr.scc.elo.service.ConnectionService;
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
public class EloCoreServiceImpl implements EloCoreService{

	@Autowired
	private EloCore eloCore;  

	@Autowired
	private EloService eloService;
	
	@Override
	public List<Player> treatAllMatches(InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> treatOneMatch(Match match) {

		List<Player> result = new ArrayList<>();
		EloEvolution eloEvol = eloCore.calculeEloEvolution(match);
		List<String> participants = match.getIdParticipants();
		//Si on ne restreint a aucun participant c'est que tous participent
		boolean allPlayerInvolved = CollectionUtils.isEmpty(participants);
		
		EloType type = match.getTypeMatch();
		for (Player player : match.getWinner().getMembers()) {
			if (allPlayerInvolved || participants.contains(player.getName())){
				Player psave = eloService.getPlayer(player.getName());
				int elo = psave.getEloMoyenne(type) + eloEvol.getVariationEloWinner();
				if (elo == type.getDefaultValue()){
					elo++;
				}
				psave.setEloValue(type, elo);
				
				result.add(psave);
			}
		}

		for (Player player : match.getLooser().getMembers()) {
			if (allPlayerInvolved || participants.contains(player.getName())){
				Player psave = eloService.getPlayer(player.getName());
				int elo = psave.getEloMoyenne(type) + eloEvol.getVariationEloLooser();
				if (elo == type.getDefaultValue()){
					elo++;
				}
				psave.setEloValue(type, elo);
			}
		}
		return result;
	}


	
}
