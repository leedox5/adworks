<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cars</title>
</head>
<body>
	<form id="form-1" action="controller">
		<input type="hidden" name="action" value="viewcar">
		<label for="carId">Id:</label>
		<input id="carId" type="text" name="carId">
		<button type="submit">Submit</button>
	</form>
	<nav>
		<a href="<%= request.getContextPath() %>">Home</a>
	</nav>
</body>
</html>