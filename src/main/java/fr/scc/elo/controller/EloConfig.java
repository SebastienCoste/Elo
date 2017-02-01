package fr.scc.elo.controller;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringComponentProvider;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

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
@ComponentScan({"fr.scc.elo"} )  
@ApplicationPath("/rest/v1")
public class EloConfig extends ResourceConfig {
	 public EloConfig() {
	        register(RequestContextFilter.class);
	        register(LoggingFilter.class);
	        register(SpringComponentProvider.class);
	        register(ServiceController.class);
	        register(EloController.class);
	        register(PlayerController.class);
//	        register(TeamController.class);
	        register(MatchMakingController.class);
	        // configuration swagger
	        packages("io.swagger.jaxrs.listing");
	    }
}
