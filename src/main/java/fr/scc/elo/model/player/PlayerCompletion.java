package fr.scc.elo.model.player;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerCompletion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String id;
	private String label;
	private String value;
}
