package ddoy.telcoData.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="eventData")
public class EventRecord implements Serializable {
	
	/**
	 * Auto Generated variable from a warning
	 */
	private static final long serialVersionUID = 1L;

	// Primary Key
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer EventId;
	
	private Integer FailureClass;
	private Integer UeType;	
	private Integer Market;
	private Integer Operator;
	private Integer CellId;
	private Integer Duration;
	private Integer CauseCode;
	private String NeVersion;
	private String Imsi; 	
	private String Hier3_Id; 	
	private String Hier32_Id;
	private String Hier321_Id;
	
	private String DateTime;
	
	
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

	public Integer getUeType() {
		return UeType;
	}

	public void setUeType(Integer uEType) {
		UeType = uEType;
	}

	public Integer getMarket() {
		return Market;
	}

	public void setMarket(Integer market) {
		Market = market;
	}

	public Integer getOperator() {
		return Operator;
	}

	public void setOperator(Integer operator) {
		Operator = operator;
	}

	public Integer getCellId() {
		return CellId;
	}

	public void setCellId(Integer cellId) {
		CellId = cellId;
	}

	public Integer getDuration() {
		return Duration;
	}

	public void setDuration(Integer duration) {
		Duration = duration;
	}

	public String getNeVersion() {
		return NeVersion;
	}

	public void setNeVersion(String neVersion) {
		NeVersion = neVersion;
	}

	public String getImsi() {
		return Imsi;
	}

	public void setImsi(String imsi) {
		Imsi = imsi;
	}

	public String getHier3_Id() {
		return Hier3_Id;
	}

	public void setHier3_Id(String Id) {
		Hier3_Id = Id;
	}

	public String getHier32_Id() {
		return Hier32_Id;
	}

	public void setHier32_Id(String hier32_Id) {
		Hier32_Id = hier32_Id;
	}

	public String getHier321_Id() {
		return Hier321_Id;
	}

	public void setHier321_Id(String hier321_Id) {
		Hier321_Id = hier321_Id;
	}
	
	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public Integer getFailureClass() {
		return FailureClass;
	}

	public void setFailureClass(Integer failureClass) {
		FailureClass = failureClass;
	}

	
}
