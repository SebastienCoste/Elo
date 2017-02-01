package fr.scc.elo.model.filter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.scc.elo.model.meta.MetaModel;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ConnectionData extends MetaModel{

	
	private String ip;
	private Set<Long> connections;
	private Long lastConnectionDate;
	private int occurences;
	private int totalOccurences;
	private int spamAlert;
	private static Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	public Set<Long> getConnections(){
		if (connections == null){
			connections= new HashSet<>();
		}
		
		return connections;
	}
	
	public String getLastConnectionDateAsString(){
		return formatter.format(new Date(lastConnectionDate));
	}
	
	public List<String> getAllConnectionsAsString(){
		Collections.sort(new ArrayList<>(connections));
		List<String> connAsStr = connections.stream()
					.map(c -> formatter.format(new Date(c)))
					.collect(Collectors.toList());
		
		return connAsStr;
	}
	public String getIpNoPoint(){
		return ip.replace("\\.", "-").replace(":", "-");
	}
	
	public void incOccurences(){
		occurences++;
		totalOccurences++;
	}

}
