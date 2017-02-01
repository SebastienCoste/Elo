package fr.scc.elo.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;

import fr.scc.elo.controller.manager.EloManager;
import fr.scc.elo.exception.EloException;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.match.MatchResponse;
import fr.scc.elo.model.player.Player;
import fr.scc.elo.model.player.PlayerCompletion;
import fr.scc.elo.model.request.PlayerRequest;
import fr.scc.elo.service.ConnectionService;
import fr.scc.elo.service.EloService;
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
@Api(value = "/player", description = "Gestion des donnes relative au joueurs ELO", tags = "player")
@Path("/player")
public class PlayerController {



	@Autowired
	private EloManager eloManager;

	/**
	 * C'est la pour les exemples, mais mettre le code metier ici prend trop de place
	 * Donc on deporte tout dans EloManager
	 * A terme le EloService disparaitra
	 */
	@Autowired
	private EloService eloService;

	@Autowired
	private ConnectionService connectionService;


	@GET
	@Path("/showall")
	public Response showPlayers() {
		return Response.ok().entity(
				eloService.getAllPlayers().toString()).build();
	}

	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/showall/html")
	public Response getAllPlayers(@Context HttpServletRequest http) {
		Map<String, Object> map = showAllPlayersHtml(http, "name", null, "up");
		return Response.ok(new Viewable("/allPlayers", map)).build();
	}

	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/delete/{name}")
	public void deletePlayer(@Context HttpServletRequest http,
			@Context HttpServletResponse redirect,
			@PathParam("name") String name) throws IOException {

		if (connectionService.acceptIPDelete(http.getRemoteAddr())){
			eloService.deletePlayer(Player.builder().name(name).build());
		}
		redirect.sendRedirect("/rest/v1/player/showall/html");
	}
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/create/{name}")
	public void createPlayer(@Context HttpServletRequest http,
			@Context HttpServletResponse redirect,
			@PathParam("name") String name) throws IOException {

		if (connectionService.acceptIPDelete(http.getRemoteAddr())){
			eloService.createPlayer(name);
		}
		redirect.sendRedirect("/rest/v1/player/showall/html");
	}
	

	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/showall/html/sort/{sort}/{previous}/{updown}")
	public Response getAllPlayersSorted(@Context HttpServletRequest http
			, @PathParam("sort") String sort
			, @PathParam("previous") String previous
			, @PathParam("updown") String updown) {
		Map<String, Object> map = showAllPlayersHtml(http, sort, previous, updown);
		return Response.ok(new Viewable("/allPlayers", map)).build();
	}

	private Map<String, Object> showAllPlayersHtml(HttpServletRequest http, String sort, String previous, String prevUpdown) {
		List<Player> players = eloService.getAllPlayers();
		EloType type = EloType.fromNameNoThrow(sort);
		String updown = "up";
		if (StringUtils.equals(sort, previous) && StringUtils.equals(prevUpdown, "up")){
			updown = "down";
		}
		Collections.sort(players, getComparator(type, sort, updown));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("players", players);
		map.put("elotypes", EloType.values());
		map.put("previous", sort);
		map.put("updown", updown);
		map.put("modif", connectionService.acceptIPUpdate(http.getRemoteAddr()));
		map.put("delete", connectionService.acceptIPDelete(http.getRemoteAddr()));
		return map;
	}

	private Comparator<Player> getComparator(EloType type, String sort, String updown) {
		return new Comparator<Player>() {
			@Override
			public int compare(Player  p1, Player  p2)
			{
				if (type == null){
					if (StringUtils.equalsIgnoreCase(sort, "moyenne")){
						if (StringUtils.equals(updown, "up")){
							return  p1.getEloComparator().compareTo(p2.getEloComparator());
						} else {
							return  p2.getEloComparator().compareTo(p1.getEloComparator());
						}
					} 
					if (StringUtils.equals(updown, "up")){
						return  p1.getName().compareTo(p2.getName());
					} else {
						return  p2.getName().compareTo(p1.getName());
					}
				}
				if (StringUtils.equals(updown, "up")){
					return  p1.getElo(type).compareTo(p2.getElo(type));
				} else {
					return  p2.getElo(type).compareTo(p1.getElo(type));
				}
			}
		};
	}

	@GET
	@Path("/removefake")
	public Response removeTest() {
		return Response.ok().entity(
				eloService.removeTest().toString()).build();
	}

	@GET
	@Path("/show/name/{n}/elo/{e}/type/{t}/save/{s}/delete/{d}")
	public Response managePlayer(@BeanParam PlayerRequest request)
			throws EloException{
		return Response.ok().entity(
				eloManager.managePlayer(
						StringUtils.lowerCase(request.getName()), 
						request.getElo(), 
						request.getTypeMatch(), 
						request.getCreatePlayers(),
						request.getDeletePlayers()).toString()
				).build();
	}


	@GET
	@Path("/setelo/name/{n}/elo/{e}/type/{t}/save/{s}")
	@ApiOperation(value = "affecte un elo a un joueur",
	notes = "save=true -> cree le joueur au besoin",
	response = Player.class)
	public Response setEloToPlayer(@BeanParam PlayerRequest request,
			@Context HttpServletRequest http)
					throws EloException{
		try{
			if (!connectionService.acceptIPUpdate(http.getRemoteAddr())){
				throw new EloException("Unauthorized IP: " + http.getRemoteAddr());
			}
			return Response.ok().entity(
					eloManager.setEloToPlayer(
							StringUtils.lowerCase(request.getName()), 
							request.getElo(), 
							request.getTypeMatch(), 
							request.getCreatePlayers()).toString()
					).build();
		} catch (Exception e){
			return Response.serverError().entity(
					e.getMessage()
					)
					.build();
		}
	}

	@GET
	@Path("/setelo/name/{n}/elo/{e}/type/{t}")
	@ApiOperation(value = "affecte un elo a un joueur",
	notes = "save=false si le joueur n'existe pas il y a une erreur",
	response = Player.class)
	public Response setEloToPlayerNoSave(@BeanParam PlayerRequest request,
			@Context HttpServletRequest http)
					throws EloException{
		try{
			if (!connectionService.acceptIPUpdate(http.getRemoteAddr())){
				throw new EloException("Unauthorized IP: " + http.getRemoteAddr());
			}
			Player player = eloManager.setEloToPlayer(
					StringUtils.lowerCase(request.getName()), 
					request.getElo(), 
					request.getTypeMatch(), 
					request.getCreatePlayers());

			return Response.ok().entity(
					player
					)
					.build();
		} catch (Exception e){
			return Response.serverError().entity(
					e.getMessage()
					)
					.build();
		}
	}

	@GET
	@Path("/show/name/{n}/type/{t}")
	@ApiOperation(value = "affiche le elo d' joueur",
	notes = "",
	response = Player.class)
	public Response showElo(@BeanParam PlayerRequest request)
			throws EloException{
		try{

			Integer elo = eloManager.setEloToPlayer(
					StringUtils.lowerCase(request.getName()), 
					request.getElo(), 
					request.getTypeMatch(), 
					request.getCreatePlayers())
					.getElo(EloType.fromName(request.getTypeMatch()));
			return Response.ok().entity(
					elo
					)
					.build();
		} catch (Exception e){
			return Response.serverError().entity(
					e.getMessage()
					)
					.build();
		}
	}

	@GET
	@Path("/completion/term/{t}")
	public Response completion(
			@Context HttpServletRequest http,
			@PathParam("t") String term
			)throws EloException {
		
		List<PlayerCompletion> allPlayersForCompletion = eloManager.getAllPlayersForCompletion(term);
		return Response.ok().entity(allPlayersForCompletion).build();
	}
	
	

	private String test = "[{\"id\":\"Caprimulgus europaeus\",\"label\":\"European Nightjar\",\"value\":\"European Nightjar\"},"
			+ "{\"id\":\"Picus viridis\",\"label\":\"European Green Woodpecker\",\"value\":\"European Green Woodpecker\"},"
			+ "{\"id\":\"Saxicola rubicola\",\"label\":\"European Stonechat\",\"value\":\"European Stonechat\"},"
			+ "{\"id\":\"Pluvialis apricaria\",\"label\":\"European Golden Plover\",\"value\":\"European Golden Plover\"},"
			+ "{\"id\":\"Carduelis chloris\",\"label\":\"European Greenfinch\",\"value\":\"European Greenfinch\"},"
			+ "{\"id\":\"Acrocephalus scirpaceus\",\"label\":\"European Reed Warbler\",\"value\":\"European Reed Warbler\"},"
			+ "{\"id\":\"Carduelis carduelis\",\"label\":\"European Goldfinch\",\"value\":\"European Goldfinch\"},"
			+ "{\"id\":\"Erithacus rubecula\",\"label\":\"European Robin\",\"value\":\"European Robin\"},"
			+ "{\"id\":\"Larus argentatus\",\"label\":\"European Herring Gull\",\"value\":\"European Herring Gull\"},"
			+ "{\"id\":\"Serinus serinus\",\"label\":\"European Serin\",\"value\":\"European Serin\"},"
			+ "{\"id\":\"Phalacrocorax aristotelis\",\"label\":\"European Shag\",\"value\":\"European Shag\"},"
			+ "{\"id\":\"Coracias garrulus\",\"label\":\"European Roller\",\"value\":\"European Roller\"}]";

}
