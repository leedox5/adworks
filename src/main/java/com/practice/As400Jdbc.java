//ref -- http://www.missiondata.com/blog/system-administration/46/accessing-as400-databases-with-ruby-java-and-the-rubyjavabridge/
package com.practice;

import java.sql.*;

public class As400Jdbc
{
	public static void main(String args[])
	{
		Connection connection = null;
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
		} catch (ClassNotFoundException ex) {
			System.err.println("Dirver Problem: " + ex.getMessage());
		}
		
		try
		{
			connection = DriverManager.getConnection("jdbc:as400://10.133.21.181","MCAMSDEV","a#engnu62");
			PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM SYSIBM.TABLES");

			ResultSet resultSet = statement.executeQuery();

			while(resultSet.next())
			{
				System.out.println(resultSet.getObject(1));
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try { 
				if(connection!=null) connection.close(); 
			}
			catch(SQLException e)
			{
				throw new RuntimeException(e);
			}
		}

	}
}
