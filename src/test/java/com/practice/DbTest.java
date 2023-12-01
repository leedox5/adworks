package com.practice;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DbTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");

			// System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
			// "com.ibm.websphere.naming.WsnInitialContextFactory");
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
			// System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

			InitialContext ic = new InitialContext();

			System.out.println(ic);
			/*
			 * ic.createSubcontext("jdbc");
			 * //ic.createSubcontext("jdbc/mcams_ds");
			 * 
			 * AS400JDBCConnectionPoolDataSource dataSource = new
			 * AS400JDBCConnectionPoolDataSource();
			 * dataSource.setServerName("10.133.21.181");
			 * dataSource.setDatabaseName("RMSFLE");
			 * dataSource.setUser("MCAMSDEV");
			 * dataSource.setPassword("a#engnu62");
			 * 
			 * //AS400JDBCDataSource ds = new AS400JDBCDataSource();
			 * 
			 * ic.bind("jdbc/mcams_ds", dataSource);
			 * 
			 * Context context = new InitialContext();
			 * //Context webContext = (Context)context.lookup("jdbc/mcams_ds");
			 * 
			 * AS400JDBCDataSource ds =
			 * (AS400JDBCDataSource)context.lookup("jdbc/mcams_ds");
			 * Connection con = ds.getConnection();
			 */
		} catch (NamingException ex) {
			Logger.getLogger(DbTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Test
	public void test() {
		Connection connection = null;
		// fail("Not yet implemented");
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
		} catch (ClassNotFoundException ex) {
			System.exit(0);
		}
		try {
			connection = DriverManager.getConnection("jdbc:as400://10.133.21.181", "MCAMSDEV", "a#engnu62");
			boolean aa = connection instanceof com.ibm.as400.access.AS400JDBCConnection;
			assertEquals(aa, true);
		} catch (SQLException e) {
			fail("Error");
		}
	}

}
