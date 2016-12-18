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

<div id="results"></div>
<script>
    var itemNumber = ${detailAuctionBean.auction.idItem};
    var url = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByProduct&SERVICE-VERSION=1.12.0&SECURITY-APPNAME=JorgeAra-iBei-PRD-145f0c74d-be9688e0&RESPONSE-DATA-FORMAT=JSON&callback=_cb_findItemsByKeywords&REST-PAYLOAD&paginationInput.entriesPerPage=1&productId.@type=ISBN&productId="+itemNumber+"&sortOrder=PricePlusShippingLowest";
    // Parse the response and build an HTML table to display search results
    function _cb_findItemsByKeywords(root) {
        var items = root.findItemsByProductResponse[0].searchResult[0].item || [];
        var html = [];
        html.push('<table width="100%" border="0" cellspacing="0" cellpadding="3"><tbody>');
        for (var i = 0; i < items.length; ++i) {
            var item = items[i];
            var title = item.title;
            var pic = item.galleryURL;
            var viewitem = item.viewItemURL;
            var itemPrice = item.sellingStatus[0].currentPrice[0].__value__;
            if (null != title && null != viewitem) {
                html.push('<tr><td>' + '<img src="' + pic + '" border="0">' + '</td>' +
                    '<td><a href="' + viewitem + '" target="_blank">' + title + '</a></td><td>' + 'Current Lowest Price on Ebay: ' + itemPrice + '</td></tr>');
            }


            html.push('</tbody></table>');
            document.getElementById("results").innerHTML = html.join("");
        }  // End _cb_findItemsByKeywords() function
    }
    s=document.createElement('script'); // create script element
    s.src= url;
    document.body.appendChild(s);

    //document.write("<a href=\"" + url + "\">" + url + "</a>");
</script>

</body>
</html>
