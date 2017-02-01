package fr.scc.elo.dao.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.scc.elo.bdd.PlayerRepository;
import fr.scc.elo.dao.PlayerDao;
import fr.scc.elo.model.elo.ScoreElo;
import fr.scc.elo.model.player.Player;


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

@Component
public class PlayerDaoImpl implements PlayerDao{


	@Autowired
	private PlayerRepository playerRepository;

	@Override
	public List<Player> getAllPlayers() {
		return playerRepository.findAll();
	}

	@Override
	public Player createPlayer(String name) {

		Player found = getPlayer(name);
		if (found == null){
			return playerRepository.insert(new Player(name));
		}
		return found;
	}

	@Override
	public Player createOrUpdatePlayer(String name, ScoreElo elo) {
		Player found = getPlayer(name);
		if (found != null){
			found.setElo(elo);
			return playerRepository.save(found);
		} 
		return playerRepository.insert(new Player(name, elo));
	}

	@Override
	public void deletePlayer(String name) {
		playerRepository.delete(new Player(name));
	}

	@Override
	public Player getPlayer(String name) {
		return playerRepository.findByName(name);
	}

	@Override
	public Player savePlayer(Player player) {
		return playerRepository.save(player);
	}

	@Override
	public void deletePlayer(Player player) {
		playerRepository.delete(player);
	}

	@Override
	public List<Player> saveAllPlayer(Collection<Player> player) {
		return playerRepository.save(player);
	}
}
