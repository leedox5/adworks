package kr.leedox.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ServiceLocator {

	public static DataSource getDataSource(String jndiName) throws NamingException {
		
		Context ctx = new InitialContext();
		Context envContext = (Context)ctx.lookup("java:/comp/env");
	    DataSource ds = (DataSource) envContext.lookup(jndiName);
		
		return ds;
	}
	
}
