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
<jsp:useBean id="loginFBBean" class="model.LoginFacebookBean" scope="session"></jsp:useBean>
<jsp:useBean id="indexBean" class="model.IndexBean" scope="session"></jsp:useBean>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Login</title>
</head>
<body>
<h1>Do your login</h1>
    <s:form action="login" method="POST">
        <s:textfield name="username">Username </s:textfield>
        <s:password name="password">Password </s:password>
        <s:submit />
    </s:form>

<a href="/register.jsp">Not registered yet?</a>

<a href="${indexBean.authUrl}">loga no f�b�</a>

</body>
</html>