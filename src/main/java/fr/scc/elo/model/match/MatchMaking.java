package fr.scc.elo.model.match;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.scc.elo.model.Team;
import fr.scc.elo.model.elo.EloType;
import fr.scc.elo.model.meta.MetaModel;
import fr.scc.elo.model.player.Player;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MatchMaking  extends MetaModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EloType type;
	
	private Integer eloBlue;
	private Integer eloRed;
	
	private Team teamRed;
	private Team teamBlue;

	public String getBluePlayer(int i){
		List<Player> blue = getTeamBlue().getMembers();
		if (i < blue.size()){
			return blue.get(i).getName();
		}
		return StringUtils.EMPTY;
	}
	
	public String getRedPlayer(int i){
		List<Player> red = getTeamRed().getMembers();
		if (i < red.size()){
			return red.get(i).getName();
		}
		return StringUtils.EMPTY;
	}
	
	public Integer getEloBlue(){
		return teamBlue.getMembers().stream().mapToInt(p -> p.getEloMoyenne(type)).sum() / teamBlue.getMembers().size();
	}
	
	public Integer getEloRed(){
		return teamRed.getMembers().stream().mapToInt(p -> p.getEloMoyenne(type)).sum() / teamRed.getMembers().size();
	}
}
