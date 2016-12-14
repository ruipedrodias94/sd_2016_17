<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
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


<h1>Details of Auction ${detailAuctionBean.auction.title}</h1>
<c:out value="${detailAuctionBean.auction.idAuction}"> Details of auction </c:out> <br>
<c:out value="${detailAuctionBean.auction.title}"> Title </c:out><br>
<c:out value="${detailAuctionBean.auction.description}"> Description </c:out><br>
<c:out value="${detailAuctionBean.auction.amount}"> Amount </c:out><br>
<c:out value="${detailAuctionBean.auction.deadline}"> Deadline </c:out><br>
<c:out value="${detailAuctionBean.auction.idUser}"> Id User </c:out><br>

<br>
<c:if test="${detailAuctionBean.active == true}" >
<h2>Make a bid to this auction!</h2>
<form action="/bidAuction" method="post">
    <input type="hidden" name="idAuction" value="${detailAuctionBean.auction.idAuction}">
    <input type="number" name="amount">
    <input type="submit" value="Submit Bid!">
</form>
</c:if>

<h3>Bids history </h3>
<c:forEach items="${detailAuctionBean.auction.bids}" var="bids">
    <p>Bid: ${bids.idBid} // Amount: ${bids.amount} // Feita por: ${bids.idUser}</p>
</c:forEach>

<a href="/messageWall.action">See Auction Message Wall</a>
<h2>Edit Section</h2>

<c:if test="${detailAuctionBean.auction.idUser == userID}" >

    <h3>Here you can edit an auction</h3>
    <h3>Edit ${detailAuctionBean.auction.title} auction</h3>

    <form action="/editAuction.action"  method="post">
        <input type="hidden" name="auctionId" value="${detailAuctionBean.auction.idAuction}">
        Title <input type="text" name="title" value="${detailAuctionBean.auction.title}"><br>
        Id Item <input type="text" name="idItem" value="${detailAuctionBean.auction.idItem}"><br>
        Description <input type="text" name="description" value="${detailAuctionBean.auction.description}"><br>
        Deadline (DD-MM-YYYY) <input type="text" name="deadline" value="${detailAuctionBean.auction.deadline}"><br>
        Amount<input type="number" name="amount" value="${detailAuctionBean.auction.amount}"><br>
        <input type="submit" value="Submit edition">
    </form>

</c:if>


</body>
</html>
