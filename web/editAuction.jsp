<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>


<jsp:useBean id="editAuctionBean" class="model.EditAuctionBean" scope="session"></jsp:useBean>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <title>Edit Auction</title>
</head>
<body>

<h1>Here you can edit an auction</h1>
<h1>Edit ${editAuctionBean.antigo.title} auction</h1>

<form action="/editAuction.action" method="post">
    Title <input type="text" name="title" value="${editAuctionBean.antigo.title}"><br>
    Id Item <input type="text" name="idItem" value="${editAuctionBean.antigo.idItem}"><br>
    Description <input type="text" name="description" value="${editAuctionBean.antigo.description}"><br>
    Deadline (DD-MM-YYYY) <input type="text" name="deadline" value="${editAuctionBean.antigo.deadline}"><br>
    Amount<input type="number" name="amount" value="${editAuctionBean.antigo.amount}"><br>
    Do It!<input type="submit" value="Submit Bid!">
</form>
</body>
</html>
