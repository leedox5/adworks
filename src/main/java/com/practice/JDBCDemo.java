package com.practice;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.itnova.util.LocalContext;
import com.itnova.util.LocalContextFactory;

import mobis.mcams.common.ChinMaster;
import mobis.mcams.pc.dao.JAR_PCD0_D;

public class JDBCDemo {

	public static void main(String[] args) {
		Connection con = null;
		try {
			LocalContext ctx = LocalContextFactory.createLocalContext("com.ibm.as400.access.AS400JDBCDriver");
			ctx.addDataSource("jdbc/mcams_ds", "jdbc:as400://10.133.21.181","MCAMSDEV","a#engnu62");
			
			DataSource ds = (DataSource) new InitialContext().lookup("jdbc/mcams_ds");
			con = ds.getConnection();
			
			JAR_PCD0_D dao = new JAR_PCD0_D(con);
			
			String ret = dao.getIsLastSEQ("K001", "RD", "AAAAA", "E01", "1919064", "29");
			System.out.println(ret);
			
			ChinMaster chin = new ChinMaster("K001","RD","AAAAA","E01","1919064", "29");
			chin.getData();
			System.out.println(chin.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
