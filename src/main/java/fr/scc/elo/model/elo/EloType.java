package fr.scc.elo.model.elo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import fr.scc.elo.exception.EloTypeNotFoundException;
import lombok.Getter;

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

public enum EloType implements Serializable{

	EBG(1500),
	E5V5(1500),
	E2V2(1500);
	
	@Getter
	private Integer defaultValue;
	
	EloType(Integer defaultValue){
		this.defaultValue = defaultValue;
	}
	
	public static EloType fromName(String eloType)throws EloTypeNotFoundException{
		for (EloType elo : EloType.values()) {
			if (StringUtils.equalsIgnoreCase(eloType, elo.toString())){
				return elo;
			}
		}
		throw new EloTypeNotFoundException();
	}
	
	public static EloType fromNameNoThrow(String eloType){
		for (EloType elo : EloType.values()) {
			if (StringUtils.equalsIgnoreCase(eloType, elo.toString())){
				return elo;
			}
		}
		return null;
	}

}
