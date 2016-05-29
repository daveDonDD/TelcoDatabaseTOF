package ddoy.DBUnitTestOpenEJB.testhelper;

import java.io.FileInputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.Module;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public abstract class DBUnitTestOpenEJB extends DBTestCase{

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

		 
		public DBUnitTestOpenEJB(){
			System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.jdbc.Driver" );
			System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost/telcoDatabase" );
			System.setProperty( PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root" );
		
		}
		
		protected void populateDatabase(String fileName) throws Exception{
			IDatabaseConnection connection = null;
			
			IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(fileName)); 
			
			connection = getConnection();
			
			DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
			DatabaseOperation.INSERT.execute(connection, dataSet);
			
		}
		 
}
