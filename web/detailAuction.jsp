<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%--
  Created by IntelliJ IDEA.
  User: Rui Pedro Dias
  Date: 11/12/2016
  Time: 22:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="detailAuctionBean" class="model.DetailAuctionBean" scope="session"></jsp:useBean>

<html>
<head>
    <title>Details</title>
</head>
<body>

<!-- NEEEED TO TEST THIS SHIT -->
<h1>Details of project ${detailAuctionBean.auction.title}</h1>
<c:out value="${detailAuctionBean.auction.idAuction}"> Details of auction </c:out> <br>
<c:out value="${detailAuctionBean.auction.title}"> Title </c:out><br>
<c:out value="${detailAuctionBean.auction.description}"> Description </c:out><br>
<c:out value="${detailAuctionBean.auction.amount}"> Amount </c:out><br>
<c:out value="${detailAuctionBean.auction.deadline}"> Deadline </c:out><br>
<c:out value="${detailAuctionBean.auction.idUser}"> Id User </c:out><br>

<br>

<h2>Make a bid to this auction!</h2>
<form action="/bidAuction" method="post">
    <input type="hidden" name="idAuction" value="${detailAuctionBean.auction.idAuction}">
    <input type="number" name="amount">
    <input type="submit" value="Submit Bid!">
</form>

</body>
</html>
