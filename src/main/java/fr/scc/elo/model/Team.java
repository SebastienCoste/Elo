package fr.scc.elo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.util.CollectionUtils;

import fr.scc.elo.model.meta.MetaModel;
import fr.scc.elo.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
public class Team extends MetaModel implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String tag;
	private String name;
	private List<Player> members;
	private Player teamLeader;
	
	/**
	 * Dedie au dao, ne pas utiliser manuellement
	 */
	public Team (){}
	
	public Team (String tag){
		this.tag = tag;
	}

	public Team (String name, String tag){
		this.name = name;
		this.tag = tag;
	}

	public Team(String name, String tag, Player teamLeader){
		this.name = name;
		this.tag = tag;
		this.teamLeader = teamLeader;
	}
	

	public Team(String name, String tag, Player teamLeader, List<Player> members){
		this.name = name;
		this.tag = tag;
		this.teamLeader = teamLeader;
		this.members = members;
	}
	
	public void addMember(Player player){
		if (members == null){
			members = new ArrayList<>();
		}
		if (!CollectionUtils.contains(members.iterator(), player)){
			members.add(player);
		}
	}

}
