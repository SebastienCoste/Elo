package fr.scc.elo.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import fr.scc.elo.dao.PlayerDao;
import fr.scc.elo.model.player.Player;
import fr.scc.elo.service.PlayerService;

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
@EnableAutoConfiguration
public class PlayerServiceImpl implements PlayerService{

	
	@Autowired
	private PlayerDao playerDao;

	@Override
	public List<Player> getAllPlayers() {
		return playerDao.getAllPlayers();
	}

	@Override
	public Player getPlayer(String name) {
		return playerDao.getPlayer(name);
	}

	@Override
	public Player createPlayer(String name) {
		return playerDao.createPlayer(name);
	}

	@Override
	public Player savePlayer(Player player) {
		return playerDao.savePlayer(player);
	}

	@Override
	public void deletePlayer(Player player) {
		playerDao.deletePlayer(player);
	}

	@Override
	public List<Player> saveAllPlayer(Collection<Player> player) {
		return playerDao.saveAllPlayer(player);
	}


}
