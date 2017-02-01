package fr.scc.elo.controller;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.scc.elo.controller.manager.EloManager;
import fr.scc.elo.exception.EloException;
import fr.scc.elo.model.Team;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.match.MatchMaking;
import fr.scc.elo.service.EloService;
import io.swagger.annotations.Api;

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
@Produces({MediaType.APPLICATION_JSON})
@Api(value = "/elo", description = "Gestion des donnes relative au ELO", tags = "elo")
@Path("/elo")
public class TeamController {



	@Autowired
	private EloManager eloManager;

	/**
	 * C'est la pour les exemples, mais mettre le code metier ici prend trop de place
	 * Donc on deporte tout dans EloManager
	 * A terme le EloService disparaitra
	 */
	@Autowired
	private EloService eloService;

	@GET
	@Path("/showall")
	public Response showTeams() {
		return  Response.ok().entity(
				String.valueOf(eloService.getAllTeams())
				).build();
	}


	@GET
	@Path("/show")
	public Response manageTeam(
//			@RequestParam(value="name", required=false, defaultValue="") String name, 
//			@RequestParam(value="tag", required=true) String tag, 
//			@RequestParam(value="teamleader", required=false) String teamLeader,
//			@RequestParam(value="members", required=false) String members,
//			@RequestParam(value="createplayers", required=false, defaultValue="false") String createPlayers, 
//			@RequestParam(value="save", required=false, defaultValue="false") String save, 
//			@RequestParam(value="delete", required=false, defaultValue="false") String delete
			)throws EloException {

		return Response.ok().entity("OK"
//				eloManager.manageTeam(tag, name, StringUtils.lowerCase(teamLeader), 
//				StringUtils.lowerCase(members), 
//				Boolean.valueOf(createPlayers), Boolean.valueOf(save), Boolean.valueOf(delete))
				).build();
	} 









}
