<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Returned car</title>
</head>
<body>
	<h2>Car details</h2>
	
	<ul>
		<li>ID:<c:out value="${ returnedCar.id }"/></li>
		<li>Name:<c:out value="${ returnedCar.name }"/></li>
		<li>Price:<c:out value="${ returnedCar.price }"/></li>
	</ul>
	
	<nav>
		<a href="<%= request.getContextPath() %>">Home</a>
	</nav>
</body>
</html>