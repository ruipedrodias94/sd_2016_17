<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<jsp:useBean id="editAuctionBean" class="model.EditAuctionBean" scope="session"></jsp:useBean>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <title>Edit Auction</title>
</head>
<body>

<h1>Here you can edit an auction</h1>

<s:form action="editAuction" method="POST">
    <s:textfield name="title">Title </s:textfield><br>
    <s:textfield name="idItem">Id Item </s:textfield><br>
    <s:textfield name="description">Description </s:textfield><br>
    <s:textfield name="deadline">DeadLine (DD-MM-YYYY) </s:textfield><br>
    <s:textfield name="amount">Amount </s:textfield><br>
    <s:submit>Just do it</s:submit>
</s:form>

</body>
</html>
