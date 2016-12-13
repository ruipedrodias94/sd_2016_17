<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: jorgearaujo
  Date: 13/12/16
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="myAuctionsBean" class="model.MyAuctionsBean" scope="session"></jsp:useBean>

<html>
<head>
    <title>My Auctions</title>
</head>
<body>
<c:forEach items="${myAuctionsBean.myAuctions}" var="auction">
    <a href="/detailAuction.action?auctionId=${auction.idAuction}"> ${auction.title} </a><br>
</c:forEach>
</body>
</html>
