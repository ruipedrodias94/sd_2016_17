<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: jorgearaujo
  Date: 12/12/16
  Time: 18:44
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>

<h1>Do your Registry</h1>
<s:form action="register" method="POST">
    <s:textfield name="username">Username </s:textfield>
    <s:password name="password">Password </s:password>
    <s:submit />
</s:form>
</body>
</html>
