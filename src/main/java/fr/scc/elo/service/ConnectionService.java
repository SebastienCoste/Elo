package fr.scc.elo.service;

import java.util.List;

public interface ConnectionService {

	void setContext(List<String> ipUpdate, List<String> ipDelete);
	boolean acceptIPUpdate(String ip);
	boolean acceptIPDelete(String ip);
}
