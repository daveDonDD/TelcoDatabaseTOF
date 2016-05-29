package ddoy.telcoData;


import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import ddoy.telcoData.entities.EventRecord;
import ddoy.telcoData.entities.EventRecordList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// commit test


@Path("/telcoData")
public class TelcoDatabaseService {

	@PersistenceContext(unitName = "eventRecordPersistenceUnit")
    private EntityManager entityManager;

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public EventRecord GetFirstEventRecord(){

		// return type is the first event record for an imsi .....(a list of events will be implemented later
		
		System.out.println(" Telcodata started");  // put formal logging in here and use logs as running tests

		Query query = entityManager.createQuery("SELECT e FROM EventRecord e");
		List<EventRecord>events =  query.getResultList();
		return events.get(0);  // send first one for now - can build to a collection next
	
		}	
		
	
	@GET
	@Path("/ImsiEventIdAndCauseCode/{imsi}")     ///need to add imsi
	@Produces(MediaType.APPLICATION_JSON)
	public EventRecordList GetEventAndCauseFailureDataForImsi(@PathParam("imsi") String imsi){
		//----------------------------------------------------------------------------------------
		// User Story : As Customer Service Rep. I want to display, for a given affected IMSI, 
		// the Event ID and Cause Code for any / all failures affecting that IMSI
		//----------------------------------------------------------------------------------------
		// return type is a list of event records for an imsi 
				
		System.out.println("SELECT e FROM EventRecord e WHERE e.Imsi ='"+imsi+"'");
		Query query = entityManager.createQuery("SELECT e FROM EventRecord e WHERE e.Imsi = '"+imsi+"'");
		Collection<EventRecord>events =  query.getResultList();
		EventRecordList ev = new EventRecordList();
		ev.setEventRecordList(events);
		return ev;  // send first one for now - can build to a collection next
	
		}	
	
	//@POST		// Temp Test class for JSON
    public String lowerCase(final String message) {
        return "Hi REST!".toLowerCase();
    }
	
	
	// Later Rest addition for second use case
	public void GetImsiFailsforAGivenTimePeriod(){
		// returns a list of imsis
	}
}
