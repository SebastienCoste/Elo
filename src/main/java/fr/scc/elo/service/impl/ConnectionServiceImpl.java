package fr.scc.elo.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import fr.scc.elo.service.ConnectionService;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
public class ConnectionServiceImpl implements ConnectionService {

	private List<String> authIPUpdate;
	private List<String> authIPDelete;
	@Override
	public boolean acceptIPUpdate(String ip) {
		return CollectionUtils.contains(authIPUpdate.iterator(), ip);
	}
	@Override
	public void setContext(List<String> ipUpdates, List<String> ipDelete) {
		setAuthIPUpdate(ipUpdates);
		setAuthIPDelete(ipDelete);
	}
	@Override
	public boolean acceptIPDelete(String ip) {
		return CollectionUtils.contains(authIPDelete.iterator(), ip);
	}

}
