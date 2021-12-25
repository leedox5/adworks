package com.itnova.util;

import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class RunScript {

	public static void main(String[] args) throws IOException, SQLException {
		Connection con = null;
		String fpath = "";
		String db = "";
		
		db = args[0];
		fpath = args[1];
		System.out.println("Input File --> " + fpath);
		System.out.println("----------------------------------------------------------------------");
		try {
			LocalContext ctx = LocalContextFactory.createLocalContext("com.ibm.as400.access.AS400JDBCDriver");
			ctx.addDataSource("jdbc/mcams_ds", "jdbc:as400://10.133.21.183","MCAMSDEV","shqkm09d#");
			ctx.addDataSource("jdbc/MCAMSPROD", "jdbc:as400://10.10.133.65","WDEV103","shqkm09d#");
			
			DataSource ds = (DataSource) new InitialContext().lookup("jdbc/"+ db);
			con = ds.getConnection();
			//executeDBScripts("D:/work/sql/DebugInsert.sql", con);
			executeDBScripts(fpath, con);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(con != null) { con.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String executeScript(Statement stmt, String inst) throws SQLException {
		try {
			stmt.executeUpdate(inst);
			return ">>> Success! ";
		} catch (Exception e) {
			return ">>> Error: " + e.getMessage();
		}
	}
	
	private static boolean executeDBScripts(String aFilePath, Connection con) throws IOException, SQLException{
		boolean isScriptExecuted = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(aFilePath));
			String str = new String();
			StringBuffer sb = new StringBuffer();
			while((str = br.readLine()) != null) {
				sb.append(str + "\n");
				//System.out.println(str);
			}
			br.close();
			
			String[] inst = sb.toString().split("\nGO");
			Statement stmt = con.createStatement();
			
			for(int i=0; i<inst.length; i++) {
				if(!inst[i].trim().equals("")) {
					showCmd(Han(inst[i]));
					System.out.println(executeScript(stmt,inst[i]));
					System.out.println("----------------------------------------------------------------------");
				}
			}
			isScriptExecuted = true;
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
		return isScriptExecuted;
	}

	private static String Han(String string) throws UnsupportedEncodingException {
		if(string == null)	return null;
		return new String(string.getBytes("UTF-8"), "KSC5601");
	}
	
	private static void showCmd(String string) {
		String[] lines = string.split("\n");
		for(String line : lines) {
			if(line.startsWith("DROP") || line.startsWith("CREATE") || line.startsWith("GRANT") || line.startsWith("INSERT")) {
				System.out.println(line);
			}
		}
	}
}
