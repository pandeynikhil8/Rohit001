<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="fo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%@ include file="new.html" %>
<center>
<h2>Allocated Asset Report:</h2> </center>
<table border="1" align="center" id="fon">
		<tr>
			<th>ASSET ID</th>
			<th>EMPLOYEE NAME</th>
			<th>ALLOCATION ID</th>
			<th>ALLOCATION DATE</th>
			<th>RELEASE DATE</th>
			<th>ASSET NAME</th>
		</tr>
		<fo:forEach var="report" items="${report}">

			<tr>
				<td>${report.assetId}</td>
				<td>${report.empName}</td>
				<td>${report.allocationId}</td>
				<td>${report.allocationDate}</td>
				<td>${report.releaseDate}</td>
				<td>${report.assetName}</td>
				
			</tr>
		
				</fo:forEach>
		</table>		
		
	
				
	<center>
	<h2>Unallocated/Available Asset Report:</h2></center>	
<table border="1" align="center" id="fon">

		<tr>
			<th>ASSET ID</th>
			<th>ASSET NAME</th>
			<th>DESCRIPTION</th>
			<th>QUANTITY</th>
			<th>STATUS</th>
		</tr>
		
		<fo:forEach var="avail" items="${avail}">

			<tr>
				<td>${avail.assetId}</td>
				<td>${avail.assetName}</td>
				<td>${avail.assetDesc}</td>
				<td>${avail.quantity}</td>
				<td>${avail.status}</td>
				
				
			</tr>
		

		</fo:forEach>
	</table>
	<br>
	<center>
	
	 <a href="generateReport.do"><button type="submit" id="nwi">Generate Report</button></a>
	</center>
</body>
</html>