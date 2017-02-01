package fr.scc.elo.model.request;

import javax.ws.rs.PathParam;

import lombok.Data;

@Data
public class MatchRequest {

	@PathParam("w") String winnerList; 
	@PathParam("l") String looserList;
	@PathParam("s") String score;
	@PathParam("c") Boolean createPlayers = false;
	@PathParam("t") String typeMatch;
}
