<%@ taglib prefix="c" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Rui Pedro Dias
  Date: 11/12/2016
  Time: 22:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Details</title>
</head>
<body>

<!-- NEEEED TO TEST THIS SHIT -->

<c:set var="object" value="${auction}"/>

<h1>Details of auction "${object.title}"</h1>

</body>
</html>
