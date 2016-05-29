package ddoy.TelcoData.tests;



import java.io.FileInputStream;
import java.io.IOException;


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
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.matcher.RestAssuredMatchers.*;
import com.jayway.restassured.response.Response;

import org.hamcrest.Matchers;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import ddoy.DBUnitTestOpenEJB.testhelper.DBUnitTestOpenEJB;
import ddoy.telcoData.TelcoDatabaseService;
import ddoy.telcoData.entities.EventRecord;



@EnableServices(value = "jaxrs")	// easy way to deploy JAXRS in OpenEJB server instance
@RunWith(ApplicationComposer.class) // ApplicationComposer is JUnit test runner for OpenEJB
public class TelcoDatabaseTest extends DBUnitTestOpenEJB{ 

	@Module							// Specifies Service class to deploy 
	@Classes({TelcoDatabaseService.class})	// NOTE : Chris C. recommended adding the EventRecord here, but it works without it - check later - ,EventRecord.class})
											// NOTE  open EJB SIMPLE REST web page has a SingletonBean object in this call - can't find this source code - but proceed for now!	
	
	// initiate JAXRS web service
	public WebApp app() {						
		return new WebApp().contextRoot("test");
	}
	
	@Before
	public void setup() throws Exception { 
		// set port in addition to default URL http://localhost    
	    RestAssured.port = 4204;
		populateDatabase("testFiles/GetImsiEventIdAndCauseCodeDBUnitData.xml");	// Exception handling TBD

	}	
	
	@Test
	public void test_GetOneFullEventRecord() throws IOException{ 
	
        final String actualEventString = WebClient.create("http://localhost:4204").path("/test/telcoData/").get(String.class);
		
		EventRecord actualEvent = new ObjectMapper().readValue(actualEventString, EventRecord.class);

		// Have loaded static data into DB - test is check DB returns same - can replace with a DBunit next,  check its EventId,causeCode 4098,1 
        EventRecord expectedEvent = new EventRecord();
      		expectedEvent.setEventId(4091);
      		expectedEvent.setFailureClass(1);
      		expectedEvent.setCauseCode(1);
      		expectedEvent.setMarket(240);
      		expectedEvent.setUeType(33000253);
      		expectedEvent.setOperator(21);
      		expectedEvent.setCellId(4);
      		expectedEvent.setDuration(1000);
      		expectedEvent.setNeVersion("12A");
      		expectedEvent.setImsi("240210000000003");
      		expectedEvent.setHier321_Id("1150444940909480000");
      		expectedEvent.setHier32_Id("8226896360947470000");
      		expectedEvent.setHier3_Id("4809532081614990000");
            assertEquals(expectedEvent.getCauseCode(),actualEvent.getCauseCode());     
            assertEquals(expectedEvent.getUeType(),actualEvent.getUeType());     
       
	}
	
	@Test
	public void test_GetOneEventRecord_with_restassured() throws IOException{ 
			
		RestAssured.when().
			get("/test/telcoData").
		then().
			statusCode(200).
			body("eventId",Matchers.equalTo(4091));
			//body("UEType",Matchers.equalTo(33000253));  // bug pending for string elements
     
	}
	
	
	@Test
	public void test_GetEventAndCauseFailureDataForImsi_2_events_from_a_bigger_list() throws IOException{ 

		RestAssured.
		given().
			// Have set up database with parameterized data in @Before with a specific datafile
			// first draft is the 2 static records in the DB
			// Therefore no need to use given for the test - should be more granular per test if that was done here
			// but would require DN mod per test
		when().
			get("/test/telcoData/ImsiEventIdAndCauseCode/240210000000003").
		then().
			assertThat().statusCode(200).					// get worked

			// test 2 specific events found out of 4 in data file
 			body("eventRecordList",Matchers.hasSize(2)).
 			
 			// and verify the correct two are there , check specific event ids and cause codes
			body("eventRecordList.eventId",Matchers.containsInAnyOrder(4091,4093)).			//need to improve matchers here so its exact to event IDS
			body("eventRecordList.causeCode",Matchers.containsInAnyOrder(1,2));				//data driven tests would help.
		
	}
	
	@Test
	public void test_GetEventAndCauseFailureDataForImsi_imsi_not_found() throws IOException{ 

		RestAssured.
		when().
			get("/test/telcoData/ImsiEventIdAndCauseCode/240210000000005").
		then().
			// test 2 specific events found out of 4 in data file
			assertThat().statusCode(200).					// get worked
			body("eventRecordList",Matchers.hasSize(0));	// empty list
	}

	@Override
	// Compulsory method in parent class DBUnitTestOpenEJB to populate required dataset
	// Load the data which will be inserted for the test * @return IDataSet */
	 protected IDataSet getDataSet() throws Exception {	
      return new FlatXmlDataSetBuilder().build(new FileInputStream("testFiles/GetImsiEventIdAndCauseCodeDBUnitData.xml"));
	}	
}	
	