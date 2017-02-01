package fr.scc.elo.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import fr.scc.elo.model.filter.ConnectionData;
import fr.scc.elo.service.ConnectionService;
import lombok.Setter;

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

public class ConnectionFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Setter
	private static ConnectionService connectionService;

	private static Map<String, ConnectionData> connectionDataMap;
	public static Map<String, ConnectionData> getConnectionDataMap(){
		if (connectionDataMap == null){
			connectionDataMap = new HashMap<>();
		} 
		return connectionDataMap;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String ip = request.getRemoteAddr();
		if (!connectionService.acceptIPUpdate(ip) && !connectionService.acceptIPDelete(ip)){

			ConnectionData coData = getConnectionDataMap().get(ip);
			Long now = new Date().getTime();
			if (coData == null){
				Set<Long> coList = new HashSet<>();
				coList.add(now);
				coData = ConnectionData.builder()
						.ip(ip)
						.lastConnectionDate(now)
						.connections(coList)
						.occurences(1)
						.totalOccurences(1)
						.build();
				connectionDataMap.put(ip, coData);
			} else {
				Long last = coData.getLastConnectionDate();
				boolean alreadyContains = now - last < 100;
				System.out.println(now);
				System.out.println(alreadyContains);
				coData.getConnections().add(now);
				coData.setLastConnectionDate(now);
				connectionDataMap.put(ip, coData);
				if (now - last < 30*1000){
					if (!alreadyContains){
						coData.incOccurences();
					}
					if (coData.getOccurences() > 10){ 
						if (!alreadyContains){
							coData.setSpamAlert(coData.getSpamAlert() +1);
						}
						try (ServletOutputStream os = response.getOutputStream()){
							os.print("ERREUR: Requete web trop rapprochez de la precedente, patientez avant de recommencer");
							os.flush();
							return;
						}
					}
				} else {
					coData.setOccurences(1);
					coData.setTotalOccurences(coData.getTotalOccurences() +1);
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}



}
