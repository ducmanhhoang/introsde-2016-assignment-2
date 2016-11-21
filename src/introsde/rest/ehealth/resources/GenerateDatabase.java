package introsde.rest.ehealth.resources;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import introsde.rest.ehealth.map.MapMessage;
import introsde.rest.ehealth.utils.RandomData;

@Stateless
@LocalBean
@Path("/generate")
public class GenerateDatabase {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@PersistenceUnit(unitName = "introsde-jpa")
	EntityManager entityManager;

	@PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
	private EntityManagerFactory entityManagerFactory;

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public MapMessage generateDatabase() {  
		MapMessage mm = new MapMessage();
	    Connection c = null;
	    Statement stmt = null;
	    try {
	    	File file = new File("lifecoach.sqlite");
	    	if (file.exists()) {
	    		file.delete();
	    	}
	    	
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:lifecoach.sqlite");
	      System.out.println("Opened database successfully");
	      
	      RandomData rd = new RandomData();
	      
	      
	      String insertPerson = "";
	      for (int i = 1; i <= 10;  i ++) {
	    	  String firstname = rd.getRandomFirstName();
	    	  String lastname = rd.getRandomLastName();
	    	  String username = rd.getRandomUsername(firstname, lastname);
	    	  insertPerson = insertPerson + "INSERT INTO \"Person\" VALUES(" + i + ",'" + firstname + "','" + lastname + "','" + rd.getRandomBirthdate() + "','" + rd.getRandomEmail(username) + "','" + username + "');";
	      }
	      
	      String insertLifeStatus = "";
	      int j = 0;
	      for (int i = 1; i <= 10; i ++) {
	    		  insertLifeStatus = insertLifeStatus + "INSERT INTO \"LifeStatus\" VALUES(" + (j = j + 1) + ",1," + i + ",'" + rd.getRandomWeight() +"');";
	    		  insertLifeStatus = insertLifeStatus + "INSERT INTO \"LifeStatus\" VALUES(" + (j = j + 1) + ",2," + i + ",'" + rd.getRandomHeight() + "');";
	    		  insertLifeStatus = insertLifeStatus + "INSERT INTO \"LifeStatus\" VALUES(" + (j = j + 1) + ",3," + i + ",'" + rd.getRandomWeight() + "');";
	    		  insertLifeStatus = insertLifeStatus + "INSERT INTO \"LifeStatus\" VALUES(" + (j = j + 1) + ",4," + i + ",'" + rd.getRandomWeight() + "');";
	    		  insertLifeStatus = insertLifeStatus + "INSERT INTO \"LifeStatus\" VALUES(" + (j = j + 1) + ",5," + i + ",'" + rd.getRandomWeight() + "');";
	    		  insertLifeStatus = insertLifeStatus + "INSERT INTO \"LifeStatus\" VALUES(" + (j = j + 1) + ",6," + i + ",'" + rd.getRandomWeight() + "');";
	      }
	      
	      
	      String insertHealthMeasureHistory = "";
	      j = 0;
	      for (int i = 1; i <= 10; i ++)
	    	  for (int k = 1; k <= 6; k ++) {
	    		  if (k == 2) {
	    			  insertHealthMeasureHistory = insertHealthMeasureHistory + "INSERT INTO \"HealthMeasureHistory\" VALUES(" + (j = j + 1) + "," + i + "," + k + ",'" + rd.getRandomHeight() + "','" + rd.getRandomTimestamp() + "'," + k + ");";
		    		  insertHealthMeasureHistory = insertHealthMeasureHistory + "INSERT INTO \"HealthMeasureHistory\" VALUES(" + (j = j + 1) + "," + i + "," + k + ",'" + rd.getRandomHeight() + "','" + rd.getRandomTimestamp() + "'," + k + ");";
		    		  insertHealthMeasureHistory = insertHealthMeasureHistory + "INSERT INTO \"HealthMeasureHistory\" VALUES(" + (j = j + 1) + "," + i + "," + k + ",'" + rd.getRandomHeight() + "','" + rd.getRandomTimestamp() + "'," + k + ");";
	    		  } else {
	    			  insertHealthMeasureHistory = insertHealthMeasureHistory + "INSERT INTO \"HealthMeasureHistory\" VALUES(" + (j = j + 1) + "," + i + "," + k + ",'" + rd.getRandomWeight() + "','" + rd.getRandomTimestamp() + "'," + k + ");";
	    			  insertHealthMeasureHistory = insertHealthMeasureHistory + "INSERT INTO \"HealthMeasureHistory\" VALUES(" + (j = j + 1) + "," + i + "," + k + ",'" + rd.getRandomWeight() + "','" + rd.getRandomTimestamp() + "'," + k + ");";
	    			  insertHealthMeasureHistory = insertHealthMeasureHistory + "INSERT INTO \"HealthMeasureHistory\" VALUES(" + (j = j + 1) + "," + i + "," + k + ",'" + rd.getRandomWeight() + "','" + rd.getRandomTimestamp() + "'," + k + ");";
	    		  }
	    	  }
	      
	      
	      

	      stmt = c.createStatement();
	      String sql = "PRAGMA foreign_keys=OFF;"
	      		+ "BEGIN TRANSACTION;"
	      		+ "CREATE TABLE Person ( "
	      		+ "    idPerson  INTEGER PRIMARY KEY AUTOINCREMENT,"
	      		+ "    name      TEXT  DEFAULT 'NULL',"
	      		+ "    lastname  TEXT  DEFAULT 'NULL',"
	      		+ "    birthdate DATETIME        DEFAULT 'NULL',"
	      		+ "    email     TEXT,"
	      		+ "    username  TEXT   DEFAULT 'NULL'"
	      		+ ");"
	      		+ insertPerson
	      		+ "CREATE TABLE MeasureDefinition ( "
	      		+ "    idMeasureDef INTEGER PRIMARY KEY AUTOINCREMENT,"
	      		+ "    measureName  TEXT  DEFAULT 'NULL',"
	      		+ "    measureType  TEXT  DEFAULT 'NULL'"
	      		+ ");"
	      		+ "INSERT INTO \"MeasureDefinition\" VALUES(1,'weight','double');"
	      		+ "INSERT INTO \"MeasureDefinition\" VALUES(2,'height','double');"
	      		+ "INSERT INTO \"MeasureDefinition\" VALUES(3,'steps','integer');"
	      		+ "INSERT INTO \"MeasureDefinition\" VALUES(4,'blood pressure','double');"
	      		+ "INSERT INTO \"MeasureDefinition\" VALUES(5,'heart rate','integer');"
	      		+ "INSERT INTO \"MeasureDefinition\" VALUES(6,'bmi','double');"
	      		+ "CREATE TABLE LifeStatus ( "
	      		+ "    idMeasure  INTEGER PRIMARY KEY AUTOINCREMENT,"
	      		+ "    idMeasureDef INTEGER       DEFAULT 'NULL',"
	      		+ "    idPerson     INTEGER       DEFAULT 'NULL',"
	      		+ "    value        TEXT,"
	      		+ "    FOREIGN KEY ( idMeasureDef ) REFERENCES MeasureDefinition ( idMeasureDef ) ON DELETE NO ACTION"
	      		+ "                                                                                   ON UPDATE NO ACTION,"
	      		+ "    CONSTRAINT 'fk_LifeStatus_Person1' FOREIGN KEY ( idPerson ) REFERENCES Person ( idPerson ) ON DELETE NO ACTION"
	      		+ "                                                                                                   ON UPDATE NO ACTION "
	      		+ ");"
	      		+ insertLifeStatus
	      		+ "CREATE TABLE MeasureDefaultRange ( "
	      		+ "    idRange       INTEGER PRIMARY KEY AUTOINCREMENT,"
	      		+ "    idMeasureDef INTEGER       DEFAULT 'NULL',"
	      		+ "    rangeName    TEXT   DEFAULT 'NULL',"
	      		+ "    startValue   TEXT,"
	      		+ "    endValue     TEXT,"
	      		+ "    alarmLevel   INTEGER        DEFAULT 'NULL',"
	      		+ "    FOREIGN KEY ( idMeasureDef ) REFERENCES MeasureDefinition ( idMeasureDef ) ON DELETE NO ACTION"
	      		+ "                                                                                   ON UPDATE NO ACTION "
	      		+ ");"
	      		+ "INSERT INTO \"MeasureDefaultRange\" VALUES(1,6,'Overweight','25.01','30',1);"
	      		+ "INSERT INTO \"MeasureDefaultRange\" VALUES(2,6,'Obesity','30.01',NULL,2);"
	      		+ "INSERT INTO \"MeasureDefaultRange\" VALUES(3,6,'Normal weight','20.01','25',0);"
	      		+ "INSERT INTO \"MeasureDefaultRange\" VALUES(4,6,'Underweight',NULL,'20',1);"
	      		+ "CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT NUMBER(19), PRIMARY KEY (SEQ_NAME));"
	      		+ "INSERT INTO \"SEQUENCE\" VALUES('SEQ_GEN',1900);"
	      		+ "CREATE TABLE \"HealthMeasureHistory\" (\"idMeasureHistory\" INTEGER PRIMARY KEY ,\"idPerson\" INTEGER DEFAULT ('NULL') ,\"idMeasureDefinition\" INTEGER DEFAULT ('NULL') ,\"value\" TEXT,\"timestamp\" TEXT DEFAULT ('NULL') ,\"idMeasureDef\" NUMBER(10));"
	      		+ insertHealthMeasureHistory
	      		+ "DELETE FROM sqlite_sequence;"
	      		+ "INSERT INTO \"sqlite_sequence\" VALUES('Person',1852);"
	      		+ "INSERT INTO \"sqlite_sequence\" VALUES('MeasureDefinition',6);"
	      		+ "INSERT INTO \"sqlite_sequence\" VALUES('LifeStatus',2356);"
	      		+ "INSERT INTO \"sqlite_sequence\" VALUES('MeasureDefaultRange',4);"
	      		+ "INSERT INTO \"sqlite_sequence\" VALUES('HealthMeasureHistory',550);"
	      		+ "CREATE INDEX LifeStatus_fk_HealthMeasure_MeasureDefinition_idx ON LifeStatus ( "
	      		+ "    idMeasureDef "
	      		+ ");"
	      		+ "CREATE INDEX LifeStatus_fk_LifeStatus_Person1_idx ON LifeStatus ( "
	      		+ "    idPerson "
	      		+ ");"
	      		+ "CREATE INDEX MeasureDefaultRange_fk_MeasureDefaultRange_MeasureDefinition1_idx ON MeasureDefaultRange ( "
	      		+ "    idMeasureDef "
	      		+ ");"
	      		+ "COMMIT;"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      mm.setContent(e.getClass().getName() + ": " + e.getMessage());
	    }
	    mm.setContent("Table created successfully");
	    return mm;
	}
}
