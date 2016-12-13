<%--
  Created by IntelliJ IDEA.
  User: jorgearaujo
  Date: 11/12/16
  Time: 23:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<jsp:useBean id="searchAuctionBean" class="model.SearchAuctionBean" scope="session"></jsp:useBean>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>Search Auction</title>
</head>
<body>

<h1>AUCTIONS ONLINE</h1>

<c:forEach items="${searchAuctionBean.auctions}" var="auction">
    <p><a href="/detailAuction.action?auctionId=${auction.idAuction}"> ${auction.title}</a><br>
</c:forEach>

</body>
</html>
