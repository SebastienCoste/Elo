package fr.scc.elo.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.server.mvc.Viewable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.scc.elo.controller.manager.EloManager;
import fr.scc.elo.exception.EloException;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.match.MatchResponse;
import fr.scc.elo.model.player.Player;
import fr.scc.elo.model.request.MatchRequest;
import fr.scc.elo.service.ConnectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
public class EloController {



	@Autowired
	private EloManager eloManager;


	@Autowired
	private ConnectionService connectionService;


	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/match/manage")
	public Response getMatchDefault(@Context HttpServletRequest http
			, @PathParam("taille") Integer taille) {
		Map<String, Object> map = getMatchMap(http, 5);

		return Response.ok(new Viewable("/match", map)).build();
	}

	
	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/welcome")
	public Response welcome(@Context HttpServletRequest http
			, @PathParam("taille") Integer taille) {
		Map<String, Object> map = new HashMap<>();
		map.put("ip", http.getLocalAddr() + ":" + http.getLocalPort());
		return Response.ok(new Viewable("/welcome", map)).build();
	}
	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/match/manage/{taille}")
	public Response getMatch(@Context HttpServletRequest http, 
			@BeanParam MatchRequest request, 
			@PathParam("taille") Integer taille) {

		Map<String, Object> map = getMatchMap(http, taille);
		return Response.ok(new Viewable("/match", map)).build();
	}

	private Map<String, Object> getMatchMap(HttpServletRequest http, Integer taille) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("elotypes", EloType.values());
		map.put("taille", taille);
		String allTags = StringUtils.join(
				IntStream.range(1, taille+1)
					.mapToObj(i -> "#b" + i + ",#r" + i)
					.collect(Collectors.toList()), 
				',');
		
		map.put("allTags", allTags);
		map.put("modif", connectionService.acceptIPUpdate(http.getRemoteAddr()));
		return map;
	}

	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/match/manage/{taille}/winners/{w}/loosers/{l}/score/{s}/type/{t}/create/{c}")
	public void saveMatchAndRestart(@Context HttpServletRequest http,
			@Context HttpServletResponse redirect,
			@BeanParam MatchRequest request, 
			@PathParam("taille") Integer taille) throws IOException {
		Map<String, Object> map = getMatchMap(http, taille);
		try{
			if (connectionService.acceptIPUpdate(http.getRemoteAddr())){
				MatchResponse response = eloManager.manageMatch(
						StringUtils.lowerCase(request.getWinnerList()), 
						StringUtils.lowerCase(request.getLooserList()), 
						request.getScore(), 
						request.getTypeMatch(), 
						request.getCreatePlayers());
				map.put("status", response.getPlayersUpdated().size() + " players updated");
			} else {
				map.put("status", "Not allowed to save match");
			}
		}catch(Exception e){
			map.put("status", e.getMessage());
		}
		redirect.sendRedirect("/rest/v1/elo/match/manage/" + taille);
	}




	@GET
	@Path("/matchtypes")
	@ApiOperation(value = "liste des types de ELO",
	notes = "remonte tous les types de elo possibles",
	response = String.class)
	public Response manageMatch() {
		return Response.ok().entity(EloType.values()).build();
	}



	@GET
	@Path("/match/winners/{w}/loosers/{l}/score/{s}/type/{t}/create/{c}")
	@ApiOperation(value = "gerer le score d'un match",
	notes = "calcule l'evolution du elo de chaque joueur suite a un match",
	response = MatchResponse.class)
	public Response manageMatch(
			@BeanParam MatchRequest request,
			@Context HttpServletRequest http
			)throws EloException {
		try{
			if (!connectionService.acceptIPUpdate(http.getRemoteAddr())){
				throw new EloException("Unauthorized IP: " + http.getRemoteAddr());
			}
			MatchResponse response = eloManager.manageMatch(
					StringUtils.lowerCase(request.getWinnerList()), 
					StringUtils.lowerCase(request.getLooserList()), 
					request.getScore(), 
					request.getTypeMatch(), 
					request.getCreatePlayers());
			return Response.ok().entity(response.toString()).build();
		} catch (Exception e){
			return Response.ok().entity(
					e.getMessage()
					)
					.build();
		}
	}



}
