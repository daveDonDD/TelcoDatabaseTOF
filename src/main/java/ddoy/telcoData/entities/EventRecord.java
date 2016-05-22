package ddoy.telcoData.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="eventData")
public class EventRecord implements Serializable {
	private String DateTime;
	
	// Primary Key
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer EventId;
	
	private Integer FailureClass;
	private Integer UEType;	
	private Integer Market;
	private Integer Operator;
	private Integer CellId;
	private Integer Duration;
	private Integer CauseCode;
	private String NEVersion;
	private String IMSI; 	
	private String HIER3_ID; 	
	private String HIER32_ID;
	private String HIER321_ID;
	
	
	
	// get and set methods TBD
	
	public Integer getEventId(){
		return EventId;
	}
	
	public void setEventId(int id){
		this.EventId = id;
	}
	
	public Integer getCauseCode(){
		return CauseCode;
	}
	
	public void setCauseCode(int id){
		this.CauseCode = id;
	}
}
