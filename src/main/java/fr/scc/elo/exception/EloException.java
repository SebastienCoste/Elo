package fr.scc.elo.exception;

import lombok.Getter;
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

@Getter
@Setter
public class EloException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String reason;
	
	public EloException (String reason){
		this.reason = reason;
	}

	public EloException (){
	}
	public EloException withReason(String reason){
		this.reason = reason;
		return this;
	}
}
