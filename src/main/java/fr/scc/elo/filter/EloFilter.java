package fr.scc.elo.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import fr.scc.elo.model.ErrorBean;

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

public class EloFilter implements Filter{

	@Override
	public void destroy() {
	}
	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try{
			chain.doFilter(request, response);
		} catch (Exception e){
			try (ServletOutputStream os = response.getOutputStream()){
				Throwable cause = e.getCause();
				if (cause != null){
					os.print(new ErrorBean().addErrorAndReturn(cause).toString());	
				}else {
					os.print(new ErrorBean().addErrorAndReturn(e).toString());
				}
				os.flush();
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}



}
