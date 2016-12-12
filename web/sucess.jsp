<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Rui Pedro Dias
  Date: 08/12/2016
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sucess</title>
</head>
<body>

    <h1>You made it!</h1>
    <c:choose>
        <c:when test="${session.loggedin == true}">
            <p>Welcome user ${session.username} with id ${session.userID}</p>
        </c:when>
    </c:choose>

    <a href="createAuction.jsp">Create Auction</a>

    <br>
    <s:form action="searchAuction" method="POST">
        <s:textfield name="code" >Auction code </s:textfield><br>
        <s:submit/>
    </s:form>
</body>
</html>
