<%--
  Created by IntelliJ IDEA.
  User: Rui Pedro Dias
  Date: 08/12/2016
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>Página de Erro</title>
</head>
<body>
    <h1>Deu erro amigo....</h1>
    <h4>You got an exception. Please <i>throw</i> it to someone who can handle it.</h4>
    <p><s:property value="exceptionStack" /></p>
</body>
</html>
