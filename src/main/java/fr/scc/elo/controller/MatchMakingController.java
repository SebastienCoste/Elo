package fr.scc.elo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import fr.scc.elo.controller.manager.EloManager;
import fr.scc.elo.exception.EloException;
import fr.scc.elo.exception.EloTypeNotFoundException;
import fr.scc.elo.model.Team;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.match.MatchMaking;
import fr.scc.elo.model.request.MatchMakingRequest;
import fr.scc.elo.model.request.PlayerRequest;
import fr.scc.elo.service.ConnectionService;
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
@Api(value = "/team", description = "Gestion du team making", tags = "teammaking")
@Path("/team")
public class MatchMakingController {



	@Autowired
	private ConnectionService connectionService;
	
	@Autowired
	private EloManager eloManager;

	/**
	 * C'est la pour les exemples, mais mettre le code metier ici prend trop de place
	 * Donc on deporte tout dans EloManager
	 * A terme le EloService disparaitra
	 */

	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/makehtml/{taille}")
	public Response makeTeamResult(
			@PathParam("taille") Integer taille){
		Map<String, Object> map = new HashMap<>();
		map.put("elotypes", EloType.values());
		map.put("taille", taille);
		return Response.ok(new Viewable("/teammaking", map)).build();
	}	
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/makehtml")
	public void makeTeamResultDefault(
			@Context HttpServletResponse redirect) throws IOException{
		redirect.sendRedirect("/rest/v1/team/makehtml/10");
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/makehtml/players/{p}/type/{t}/create/{c}")
	public Response makeTeamCall(@Context HttpServletRequest http,
			@BeanParam MatchMakingRequest request,
			@Context HttpServletResponse redirect
			) throws EloException, IOException{

		MatchMaking tm = eloManager.matchMaker(request.getPlayers(),  request.getTypeMatch(), request.getCreatePlayers());
		Map<String, Object> map = new HashMap<>();
		map.put("tm", tm);
		map.put("elotypes", EloType.values());
		map.put("taille", Math.max(tm.getTeamBlue().getMembers().size(), tm.getTeamRed().getMembers().size()));
		map.put("modif", connectionService.acceptIPUpdate(http.getRemoteAddr()));
		return Response.ok(new Viewable("/match", map)).build();
	}	
	
	@GET
	@Path("/makelight/players/{p}/type/{t}/create/{c}")
	public Response matchMaker(
			@BeanParam MatchMakingRequest request
			)throws EloException {

		EloType typeElo = EloType.fromName(request.getTypeMatch());
		MatchMaking mm = eloManager.matchMaker(request.getPlayers(),  request.getTypeMatch(), request.getCreatePlayers());
		StringBuilder sb = new StringBuilder();
		mm.getTeamBlue().getMembers().stream().forEach(p -> sb.append(p.getName() + "(" + p.getElo(typeElo) + ")"));
		sb.append(" <- BLUE ; RED -> ");
		mm.getTeamRed().getMembers().stream().forEach(p -> sb.append(p.getName() + "(" + p.getElo(typeElo) + ")"));
		sb.append("\n");
		
		return Response.ok().entity(sb.toString()).build();
	}
	
	@GET
	@Path("/make/players/{p}/type/{t}/create/{c}")
	public Response matchMakerLight(
			@BeanParam MatchMakingRequest request
			)throws EloException {

		MatchMaking mm = eloManager.matchMaker(request.getPlayers(), request.getTypeMatch(), request.getCreatePlayers());

		return Response.ok().entity(mm.toString()).build();
	}

	@GET
	@Path("/mock/players/{p}/type/{t}/create/{c}/iter/{i}")
	public Response matchMakerTest(
			@BeanParam MatchMakingRequest request
			)throws EloException {

		StringBuilder sb = new StringBuilder();
		EloType typeElo = EloType.fromName(request.getTypeMatch());
		MatchMaking mm = eloManager.matchMaker(request.getPlayers(),  request.getTypeMatch(), request.getCreatePlayers());
		for (int i = 0; i < request.getIter(); i++) {
			eloManager.manageMatch(getPlayersOfTeam(mm.getTeamBlue()), getPlayersOfTeam(mm.getTeamRed()), "500-100", request.getTypeMatch(), 
					request.getCreatePlayers());
			mm = eloManager.matchMaker(request.getPlayers(), request.getTypeMatch(), Boolean.valueOf(request.getCreatePlayers()));
			sb.append(mm.getEloBlue() + "/" + mm.getEloRed() + "##");
			mm.getTeamBlue().getMembers().stream().forEach(p -> sb.append(" " + p.getEloMoyenne(typeElo)));
			sb.append(" <- BLUE ; RED -> ");
			mm.getTeamRed().getMembers().stream().forEach(p -> sb.append(" " + p.getEloMoyenne(typeElo)));
			sb.append("\n");
		}

		return Response.ok().entity(sb.toString()).build();
	}

	private String getPlayersOfTeam(Team team) {
		StringBuilder sb = new StringBuilder();
		team.getMembers().stream().forEach(p -> sb.append(p.getName() + ","));
		String string = sb.toString();
		return string.substring(0, string.length()-2);
	}








}
