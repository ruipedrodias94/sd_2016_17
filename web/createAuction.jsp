<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <title>Welcome to create auction</title>
</head>
<body>

<h1>Here you can create an auction</h1>

<s:form action="createAuction" method="POST">
    <s:textfield name="title">Title </s:textfield><br>
    <s:textfield name="idItem">Id Item </s:textfield><br>
    <s:textfield name="description">Description </s:textfield><br>
    <s:textfield name="deadline">DeadLine (DD-MM-YYYY) </s:textfield><br>
    <s:textfield name="amount">Amount </s:textfield><br>
    <s:submit>Just do it</s:submit>
</s:form>

</body>
</html>
