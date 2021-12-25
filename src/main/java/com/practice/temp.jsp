<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="practice.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>data</title>
<style>
body,html {
    margin:0
}
table {
    border-collapse: collapse;
    border-spacing:3px;
}
table td {
    border: solid 1px #cdcdcd;
    padding:2px;
}
</style>
</head>
<body>
<table>
<%
Connection con = null;
Statement stmt = null;
ResultSet rs = null;
try
{
    con = Database.getConnectionSjgni();
    stmt = con.createStatement();
    String sql = " select * from Dept ";
    rs = stmt.executeQuery(sql);
    
    while(rs.next())
    {
%>
<tr><td><%=rs.getString(1) %></td><td><%=rs.getString(2) %></td></tr>
<%     
    }
}
finally
{
    try{ if(rs != null) rs.close(); } catch(SQLException e)   { System.err.println(e); }    
    try{ if(stmt != null) stmt.close(); } catch(SQLException e)   { System.err.println(e); }    
    try{ if(con != null) con.close(); } catch(SQLException e)   { System.err.println(e); }	
}
%>
</table>
</body>
</html>