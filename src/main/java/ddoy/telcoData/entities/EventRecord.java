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

	/**
	 * @return the uEType
	 */
	public Integer getUEType() {
		return UEType;
	}

	/**
	 * @param uEType the uEType to set
	 */
	public void setUEType(Integer uEType) {
		UEType = uEType;
	}

	/**
	 * @return the market
	 */
	public Integer getMarket() {
		return Market;
	}

	/**
	 * @param market the market to set
	 */
	public void setMarket(Integer market) {
		Market = market;
	}

	/**
	 * @return the operator
	 */
	public Integer getOperator() {
		return Operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Integer operator) {
		Operator = operator;
	}

	/**
	 * @return the cellId
	 */
	public Integer getCellId() {
		return CellId;
	}

	/**
	 * @param cellId the cellId to set
	 */
	public void setCellId(Integer cellId) {
		CellId = cellId;
	}

	/**
	 * @return the duration
	 */
	public Integer getDuration() {
		return Duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Integer duration) {
		Duration = duration;
	}

	/**
	 * @return the nEVersion
	 */
	//public String getNEVersion() {
	//	return NEVersion;
	//}

	/**
	 * @param nEVersion the nEVersion to set
	 */
	//public void setNEVersion(String nEVersion) {
	//	NEVersion = nEVersion;
	//}

	/**
	 * @return the iMSI
	 */
	///public String getIMSI() {
	//	return IMSI;
	//}

	/**
	 * @param iMSI the iMSI to set
	 */
	//public void setIMSI(String iMSI) {
	//	IMSI = iMSI;
	//}

	/**
	 * @return the hIER3_ID
	 */
	//public String getHIER3_ID() {
	//	return HIER3_ID;
	//}

	/**
	 * @param hIER3_ID the hIER3_ID to set
	 */
	//public void setHIER3_ID(String hIER3_ID) {
//		HIER3_ID = hIER3_ID;
	//}

	/**
	 * @return the hIER32_ID
	 */
	//public String getHIER32_ID() {
	//	return HIER32_ID;
	//}

	/**
	 * @param hIER32_ID the hIER32_ID to set
	 */
	//public void setHIER32_ID(String hIER32_ID) {
	//	HIER32_ID = hIER32_ID;
	//}

	/**
	 * @return the hIER321_ID
	 */
	//public String getHIER321_ID() {
	//	return HIER321_ID;
	//}

	/**
	 * @param hIER321_ID the hIER321_ID to set
	 */
	//public void setHIER321_ID(String hIER321_ID) {
	//	HIER321_ID = hIER321_ID;
	//}
}
