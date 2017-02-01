package fr.scc.elo.model.request;

import javax.ws.rs.PathParam;

import lombok.Data;

@Data
public class PlayerRequest {

	@PathParam("n") String name; 
	@PathParam("e") String elo;
	@PathParam("s") Boolean createPlayers = false;
	@PathParam("d") Boolean deletePlayers = false;
	@PathParam("t") String typeMatch;
}
