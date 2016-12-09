<%--
  Created by IntelliJ IDEA.
  User: jorgearaujo
  Date: 07/12/16
  Time: 18:14
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Login</title>
</head>
<body>
<h2>Faça o seu login</h2>
    <s:form action="login" method="POST">
        <s:textfield label="Username " name="username"/>
        <s:password label="Password " name="password"/>
        <s:submit />
    </s:form>
</body>
</html>