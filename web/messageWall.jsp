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
<c:forEach items="${messageWallBean.messages}" var="message">
      <br>${message.text} </br>
</c:forEach>
</body>
</html>
