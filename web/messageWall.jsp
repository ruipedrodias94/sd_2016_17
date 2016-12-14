<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: jorgearaujo
  Date: 13/12/16
  Time: 23:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="messageWallBean" class="model.MessageWallBean" scope="session"></jsp:useBean>


<html>
<head>
    <title>Message Wall</title>
</head>
<body>

<h1>Messages History</h1>

<c:forEach items="${messageWallBean.messages}" var="message">
      <br>${message.text} </br>
</c:forEach>
<h4>Post in this message Wall</h4>

<s:form action="messageWall" method="POST">
    <s:textfield name="messageText">Your Message: </s:textfield>
    <s:submit />
</s:form>

</body>
</html>
