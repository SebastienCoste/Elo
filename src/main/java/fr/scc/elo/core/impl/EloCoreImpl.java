package fr.scc.elo.core.impl;

import java.util.Arrays;
import java.util.OptionalDouble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.scc.elo.core.EloCore;
import fr.scc.elo.model.Team;
import fr.scc.elo.model.elo.EloEvolution;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.match.Match;
import fr.scc.elo.model.player.Player;
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

//http://bzstats.strayer.de/bzinfo/selo/?lang=en

@Component
public class EloCoreImpl implements EloCore {


	@Autowired
	private EloService eloService;

	@Override
	public EloEvolution calculeEloEvolution(Match match) {
		EloEvolution evol = new EloEvolution();
		Team wTeam = match.getWinner();
		Team lTeam = match.getLooser();
		//		Integer nbW = wTeam.getMembers().stream().filter(p -> match.getIdParticipants() == null || match.getIdParticipants().contains(p.getName()))
		//				.mapToInt(p -> 1).sum();
		double eloW = wTeam.getMembers().stream().filter(p -> match.getIdParticipants() == null || match.getIdParticipants().contains(p.getName()))
				.mapToInt(p -> getEloForPlayer(match.getTypeMatch(), p)).average().getAsDouble();


		//		Integer nbL = lTeam.getMembers().stream().filter(p -> match.getIdParticipants() == null || match.getIdParticipants().contains(p.getName()))
		//				.mapToInt(p -> 1).sum();
		double eloL = lTeam.getMembers().stream().filter(p -> match.getIdParticipants() == null || match.getIdParticipants().contains(p.getName()))
				.mapToInt(p -> getEloForPlayer(match.getTypeMatch(), p)).average().getAsDouble();


		Double expW = 1 / ( 1 + Math.pow( 10, ( ( eloL - eloW ) / (float) 400 ) ) );
		Double expL = 1 / ( 1 + Math.pow( 10, ( ( eloW - eloL ) / (float) 400 ) ) );
		int K = 50;
		double S_R = 0.25;
		float comparaisonScore = ( match.getScoreW() - match.getScoreL() ) / (float) match.getScoreW();
		double computedVariationFromScore = ( 1 - S_R ) + S_R * comparaisonScore;
		Long varW = Math.round( K * computedVariationFromScore * ( 1 - expW ) );
		Long varL = Math.round( K * computedVariationFromScore * ( -1 * expL ) );

		//		int k = 15;
		//		int a = (eloL/nbL-eloW/nbW)/((nbL + nbW) /2);  /*Ouais, bon, a, b, c et d mais c'est des temp ^^*/
		//		double b = Math.pow(a, 10);
		//		int c = (eloW/nbW-eloL/nbL)/((nbL + nbW) /2);
		//		double d = Math.pow(c, 10); 
		//		int Var_Winner = (int) Math.round(k*(1-(1/(1+b))));
		//		int Var_Looser = (int) Math.round(k*(0-(1/(1+d))));


		evol.setVariationEloLooser(Integer.valueOf(varL.toString()));
		evol.setVariationEloWinner(Integer.valueOf(varW.toString()));
		return evol;
	}

	private Integer getEloForPlayer(EloType type, Player p) {
		Player playerEnBase = eloService.getPlayer(p.getName());
		Integer elo = playerEnBase.getElo(type);
		if (elo == type.getDefaultValue()){
			elo = playerEnBase.getEloMoyenne(type);
			if (elo != type.getDefaultValue()){
				playerEnBase.setEloValue(type, elo);
				eloService.savePlayer(playerEnBase);
			}
		}

		return elo;
	}
}
