package fr.scc.elo.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import fr.scc.elo.model.elo.ScoreElo;
import fr.scc.elo.model.meta.MetaModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataUpdate  extends MetaModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 808707192687271074L;
	private String ip;
	private Integer annee;
	private Integer mois;
	private Integer jour;
	private Integer heure;
	private Integer minute;
	private Integer seconde ;
	private Long timestamp; 
	private ScoreElo elo;
	
	public DataUpdate(){
		timestamp = new Date().getTime();
		annee =  Calendar.getInstance().get(Calendar.YEAR);
		mois =  Calendar.getInstance().get(Calendar.MONTH);
		jour =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		heure =  Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		minute =  Calendar.getInstance().get(Calendar.MINUTE);
		seconde =  Calendar.getInstance().get(Calendar.SECOND);
	}
	
	public DataUpdate(String ip){
		this();
		this.ip = ip;
	}
}
