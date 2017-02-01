package fr.scc.elo.bdd;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.scc.elo.model.match.Match;

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

public interface MatchRepository extends MongoRepository<Match, String> {

	public List<Match> findByIpSource(String ipSource);
	public List<Match> findByDateBetween(Date from, Date to);
}
