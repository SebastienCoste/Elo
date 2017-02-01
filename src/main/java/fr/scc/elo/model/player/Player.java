package fr.scc.elo.model.player;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.OptionalDouble;

import org.springframework.data.annotation.Id;

import fr.scc.elo.model.DataUpdate;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.elo.ScoreElo;
import fr.scc.elo.model.meta.MetaModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Copyright (C) 2015 CHERRIER-COSTE Sebastien

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
@NoArgsConstructor
public class Player extends MetaModel implements Serializable{



	private static final long serialVersionUID = 3L;
	@Id
	private String name;
	private ScoreElo elo;
	private Map <Long, DataUpdate> historiqueElo;


	public Player(String name) {
		this.name = name;
		this.elo = new ScoreElo();
	}

	public String getNameNoPoint(){
		return this.name.replaceAll("\\.", "");
	}
	public Player (String name, ScoreElo elo){
		this.name = name;
		this.elo = elo;
	}

	public Integer getElo(EloType eloType){
		if (elo == null){
			elo = new ScoreElo();
		}
		return elo.getElo(eloType);
	}

	public Integer getEloMoyenne(EloType type) {
		Integer elo = getElo(type);
		if (elo == type.getDefaultValue()){
			OptionalDouble averageElo = Arrays.stream(EloType.values()).filter(t -> getElo(t) != t.getDefaultValue())
					.mapToInt(t -> getElo(t)).average();
			if (averageElo.isPresent()){
				return Double.valueOf(averageElo.getAsDouble()).intValue();
			}
		}
		return elo;
	}
	public Integer getEloMoyenne() {
		OptionalDouble averageElo = Arrays.stream(EloType.values()).filter(t -> getElo(t) != t.getDefaultValue())
				.mapToInt(t -> getElo(t)).average();
		if (averageElo.isPresent()){
			return Double.valueOf(averageElo.getAsDouble()).intValue();
		}
		return null;
	}

	public Integer getEloComparator() {
		Integer eloMoyenne = getEloMoyenne();
		if (eloMoyenne == null){
			 OptionalDouble average = Arrays.stream(EloType.values())
					.mapToInt(t -> getElo(t)).average();
			return Double.valueOf(average.getAsDouble()).intValue();
		}
		return eloMoyenne;
	}
	
	
	public void setEloValue(EloType eloType, Integer value){
		if (elo == null){
			elo = new ScoreElo();
		} else {
			if (historiqueElo == null){
				historiqueElo = new HashMap<>();
			}
			DataUpdate dataUpdate = new DataUpdate();
			dataUpdate.setElo(elo);
			historiqueElo.put(dataUpdate.getTimestamp(), dataUpdate);
		}

		elo.setEloValue(eloType, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
