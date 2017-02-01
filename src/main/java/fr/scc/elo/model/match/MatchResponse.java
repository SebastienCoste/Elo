package fr.scc.elo.model.match;

import java.util.List;

import fr.scc.elo.model.player.Player;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResponse {

	private List<Player> playersUpdated;
}
