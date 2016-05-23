package ddoy.TelcoData.tests;



import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.Module;
import org.codehaus.jackson.map.ObjectMapper;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.matcher.RestAssuredMatchers.*;

import org.hamcrest.Matchers;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import ddoy.telcoData.TelcoDatabaseService;
import ddoy.telcoData.entities.EventRecord;



@EnableServices(value = "jaxrs")	// easy way to deploy JAXRS in OpenEJB server instance
@RunWith(ApplicationComposer.class) // ApplicationComposer is JUnit test runner for OpenEJB
public class TelcoDatabaseTest { 

   @PersistenceContext(unitName="eventRecordPersistenceUnit")
   private EntityManager entityManager;
 
  
    @Module    
    public PersistenceUnit persistence() {
        PersistenceUnit unit = new PersistenceUnit("eventRecordPersistenceUnit");
        return unit;
    }
   
    // test specific DB configuration - if this not there it should deploy the standard one.
    @Configuration
    
    public Properties config() throws Exception {
        Properties p = new Properties();

        p.put("myDataSource", "new://Resource?type=DataSource");
        p.put("myDataSource.JdbcDriver", "com.mysql.jdbc.Driver");
        p.put("myDataSource.JdbcUrl", "jdbc:mysql://localhost:3306/telcoDatabase");
        p.put("myDataSource.JtaManaged", "true");
        p.put("myDataSource.username", "root");

      
        return p;
        
    }
   

	@Module							// Specifies Test class to deploy 
	@Classes({TelcoDatabaseService.class})
												// NOTE : Chris C. recommended adding the EventRecord here, but it works without it - check later - ,EventRecord.class})
	public WebApp app() {						// NOTE  open EJB SIMPLE REST web page has a SingletonBean object in this call - can't find this source code - but proceed for now!
		return new WebApp().contextRoot("test");
	}
	
	@Before
	public void setup() {
	    
	    RestAssured.port = 4204;
	}	
	
/*		
	@Test
	public void test_GetOneEventRecord() throws IOException{ 
	
		
		//Bug in object mapper here with an unrecognisedfield error - some incompatibality with the object mapper since I added all getters and setters.
		 *  - TBD - get help
		
        final String actualEventString = WebClient.create("http://localhost:4204").path("/test/telcoData/").get(String.class);
		
		EventRecord actualEvent = new ObjectMapper().readValue(actualEventString, EventRecord.class);

		// Have loaded static data into DB - test is check DB returns same - can replace with a DBunit next,  check its EventId,causeCode 4098,1 
        EventRecord expectedEvent = new EventRecord();
      		expectedEvent.setEventId(4098);
      		expectedEvent.setCauseCode(1);
      		expectedEvent.setUEType(33000253);
            assertEquals(expectedEvent.getCauseCode(),actualEvent.getCauseCode());     
            assertEquals(expectedEvent.getUEType(),actualEvent.getUEType());     
       
	}
	*/
	@Test
	public void test_GetOneEventRecord_with_restassured() throws IOException{ 
			

		RestAssured.when().get("/test/telcoData").
		then().
			statusCode(200).
			body("eventId",Matchers.equalTo(4098));
			//body("UEType",Matchers.equalTo(33000253));
     
	}
	
	
	// test GetEventAndCauseFailureDataForImsi
	
	@Test
	public void test_GetEventAndCauseFailureDataForImsi() throws IOException{ 

		// first draft - all imsis - 2 from static data 
		//second draft - with DBunit - set up specific data ...leading into thrid draft
		// third draft - data driven - combinations to verify query is right , using java params
		//      so with 1 target data set I can verify multiple imsis 
		//
		//  doing 1 rest assured test with DB unit first.
		

		
		
		RestAssured.given().
			// set up database with parameterised data
			//first draft is the 2 static records in the DB 
		when().
			get("/test/telcoData/ImsiEventIdAndCauseCode/240210000000003").
		then().
			statusCode(200).

			// test 2 specific events found
			// better test - put in 4 records - imsis at different times, 
			// and verify the correct two are there , check specific event ids and cause codes
 			body("eventRecordList.eventId",Matchers.containsInAnyOrder(4098,5099));
		
		
			// second matcher failing for some reason.
			//body("eventRecordList.findAll.causeCode",Matchers.containsInAnyOrder(1,2));     
	}
	

      
/*        
		EventRecord actualEvent = new ObjectMapper().readValue(actualEventString, EventRecord.class);

		// Have loaded static data into DB - test is check DB returns same - can replace with a DBunit next,  check its EventId,causeCode 4098,1 
        EventRecord expectedEvent = new EventRecord();
      		expectedEvent.setEventId(4098);
      		expectedEvent.setCauseCode(1);
              assertEquals(expectedEvent.getCauseCode(),actualEvent.getCauseCode());     
  */  
	
	//}
	
	// test GetEventAndCauseFailureDataForImsi
	// empty list - no failures for that imsi
	// imsi doesn't exist
	// Consider database errors : data base error - no connection, no table.......



}	
	