package ddoy.TelcoData.tests;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.matcher.RestAssuredMatchers.*;
import com.jayway.restassured.response.Response;

import org.hamcrest.Matchers;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import ddoy.telcoData.TelcoDatabaseService;
import ddoy.telcoData.entities.EventRecord;



@EnableServices(value = "jaxrs")	// easy way to deploy JAXRS in OpenEJB server instance
@RunWith(ApplicationComposer.class) // ApplicationComposer is JUnit test runner for OpenEJB
public class TelcoDatabaseTest extends DBTestCase{ 

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
   

	@Module							// Specifies Service class to deploy 
	@Classes({TelcoDatabaseService.class})	// NOTE : Chris C. recommended adding the EventRecord here, but it works without it - check later - ,EventRecord.class})
											// NOTE  open EJB SIMPLE REST web page has a SingletonBean object in this call - can't find this source code - but proceed for now!	
	
	// initiate JAXRS web service
	public WebApp app() {						
		return new WebApp().contextRoot("test");
	}
	
	@Before
	public void setup() { 
		// set port in addition to default URL http://localhost    
	    RestAssured.port = 4204;
	}	
	
	/** * Load the data which will be inserted for the test * @return IDataSet */
	 protected IDataSet getDataSet() throws Exception {	
       return new FlatXmlDataSetBuilder().build(new FileInputStream("/testFiles/GetImsiEventIdAndCauseCodeDBUnitData.xml"));
	}	
	
	public TelcoDatabaseTest() throws Exception
	{
		super();
		System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.jdbc.Driver" );
		System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost/telcoDatabase" );
		System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root" );
		
		populateDatabase("testFiles/GetImsiEventIdAndCauseCodeDBUnitData.xml");
	}
	
	private void populateDatabase(String fileName) throws Exception{
		IDatabaseConnection connection = null;
		
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(fileName)); 
		
		connection = getConnection();
		
		DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
		DatabaseOperation.INSERT.execute(connection, dataSet);
		
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
			

		RestAssured.when().
			get("/test/telcoData").
		then().
			statusCode(200).
			body("eventId",Matchers.equalTo(4091));
			//body("UEType",Matchers.equalTo(33000253));  // bug pending for string elements
     
	}
	
	
	@Test
	public void test_GetEventAndCauseFailureDataForImsi_2_events_from_a_bigger_list() throws IOException{ 

		// first draft - DONE - all imsis - 2 from static data 
		// second draft - DONE -  specific imsi with DBunit - set up specific data fir this query
		// third draft - data driven - combinations to verify query is right , using java params
		//      so with 1 target data set I can verify multiple imsis 
				
		RestAssured.given().
			// Have set up database with parameterised data in constructor with a specific datafile
			//first draft is the 2 static records in the DB 
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

		
		RestAssured.given().
			// Have set up database with parameterised data in constructor with a specific datafile
			//first draft is the 2 static records in the DB 
		when().
			get("/test/telcoData/ImsiEventIdAndCauseCode/240210000000005").
		then().
			// test 2 specific events found out of 4 in data file
			assertThat().statusCode(200).					// get worked
			body("eventRecordList",Matchers.hasSize(0));	// empty list
	}
	

}	
	