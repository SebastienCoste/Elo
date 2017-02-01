package fr.scc.elo.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import fr.scc.elo.filter.ConnectionFilter;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.filter.ConnectionData;
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
@Api(value = "/admin", description = "WS pour l'admin", tags = "elo")
@Path("/service")
public class ServiceController {

	@Autowired
	private ConnectionService connectionService;


	@GET
	@Path("/ip/match/write")
	@ApiOperation(value = "pour savoir si l'ip source peut enregistrer un match",
	response = MatchResponse.class)
	public Response manageMatch(
			@Context HttpServletRequest http
			)throws EloException {

		return Response.ok().entity(
				"your IP " + http.getRemoteAddr() + " can write match result ? " + 
						connectionService.acceptIPUpdate(http.getRemoteAddr())).build();
	}

	@GET
	@Path("/connections")
	@Produces({MediaType.TEXT_HTML})
	@ApiOperation(value = "pour savoir si l'ip source peut enregistrer un match",
	response = MatchResponse.class)
	public Response checkConnections(
			@Context HttpServletRequest http
			)throws EloException {
		Map<String, ConnectionData> connectionDataMap = null;
		if (connectionService.acceptIPUpdate(http.getRemoteAddr())){
			connectionDataMap = ConnectionFilter.getConnectionDataMap();
		} else {
			connectionDataMap = new HashMap<>();
		}

		Map<String, Object> map = new HashMap<>();
		map.put("coData", connectionDataMap.values());
		return Response.ok(new Viewable("/allConnections", map)).build();
	}

	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/connections/sort/{sort}/{previous}/{updown}")
	public Response checkConnectionsSorted(@Context HttpServletRequest http
			, @PathParam("sort") String sort
			, @PathParam("previous") String previous
			, @PathParam("updown") String prevUpdown) {

		Map<String, Object> map = new HashMap<>();
		List<ConnectionData> values = null;

		String updown = "up";
		if (StringUtils.equals(sort, previous) && StringUtils.equals(prevUpdown, "up")){
			updown = "down";
		}
		if (connectionService.acceptIPUpdate(http.getRemoteAddr())){
			Map<String, ConnectionData> connectionDataMap = ConnectionFilter.getConnectionDataMap();
			values = new ArrayList<>(connectionDataMap.values());
			Collections.sort(values, getComparator(sort, updown));
		} else {
			values = new ArrayList<>();
		}
		map.put("coData", values);		
		map.put("previous", sort);
		map.put("updown", updown);
		return Response.ok(new Viewable("/allConnections", map)).build();
	}

	private Comparator<ConnectionData> getComparator(String sort, String updown) {
		return new Comparator<ConnectionData>() {
			@Override
			public int compare(ConnectionData  p1, ConnectionData  p2)
			{
				if (StringUtils.equals(updown, "up")){
					if (StringUtils.equals(sort, "ip"))
						return  p1.getIp().compareTo(p2.getIp());
					if (StringUtils.equals(sort, "lastco"))
						return  p1.getLastConnectionDate().compareTo(p2.getLastConnectionDate());
					if (StringUtils.equals(sort, "occ"))
						return  Integer.valueOf(p1.getOccurences()).compareTo(p2.getOccurences());
					if (StringUtils.equals(sort, "occall"))
						return  Integer.valueOf(p1.getTotalOccurences()).compareTo(p2.getTotalOccurences());
					if (StringUtils.equals(sort, "spam"))
						return  Integer.valueOf(p1.getSpamAlert()).compareTo(p2.getSpamAlert());
				} 
				if (StringUtils.equals(sort, "ip"))
					return  p2.getIp().compareTo(p1.getIp());
				if (StringUtils.equals(sort, "lastco"))
					return  p2.getLastConnectionDate().compareTo(p1.getLastConnectionDate());
				if (StringUtils.equals(sort, "occ"))
					return  Integer.valueOf(p2.getOccurences()).compareTo(p1.getOccurences());
				if (StringUtils.equals(sort, "occall"))
					return  Integer.valueOf(p2.getTotalOccurences()).compareTo(p1.getTotalOccurences());
				if (StringUtils.equals(sort, "spam"))
					return  Integer.valueOf(p1.getSpamAlert()).compareTo(p2.getSpamAlert());
				return  0;

			}
		};
	}

}
