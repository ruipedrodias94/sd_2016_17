<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Rui Pedro Dias
  Date: 08/12/2016
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sucess</title>
</head>
<body>

    <h1>Boa, fez login com sucesso</h1>
    <c:choose>
        <c:when test="${session.loggedin == true}">
            <p>Bem vindo seu vaginas, ${session.username} com o id: ${session.userID}</p>
        </c:when>
    </c:choose>
    <a href="createAuction.jsp">Criar grande projecto</a>

</body>
</html>
