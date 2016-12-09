<%--
  Created by IntelliJ IDEA.
  User: jorgearaujo
  Date: 07/12/16
  Time: 18:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>Login</title>
</head>
<body>
    <s:form action="login" method="post">
        <s:textfield name="username" /><br>
        <s:password name="password" /><br>
        <s:submit />
    </s:form>
</body>
</html>
