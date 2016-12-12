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

<jsp:useBean id="DetailAuctionBean" class="model.DetailAuctionBean" scope="session"></jsp:useBean>

<html>
<head>
    <title>Details</title>
</head>
<body>

<!-- NEEEED TO TEST THIS SHIT -->

<h1>Details of auction ${DetailAuctionBean.detailAuction().idAuction}</h1>

<br> Description: ${DetailAuctionBean.detailAuction().description} </br>

<br> Title: ${DetailAuctionBean.detailAuction().title} </br>

<br> Amount: ${DetailAuctionBean.detailAuction().amount} </br>

<br> Deadline: ${DetailAuctionBean.detailAuction().deadline} </br>

<br> Owner: ${DetailAuctionBean.detailAuction().idUser} </br>

</body>
</html>
