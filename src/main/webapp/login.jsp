<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Login</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>

<div id="wrapper">
    <div id="header">
        <h2>ESILV Engineer School</h2>
    </div>
</div>

<div id="container">
    <div id="content">
        <h3>Login</h3>

        <c:if test="${not empty LOGIN_ERROR}">
            <p style="color:red;">${LOGIN_ERROR}</p>
        </c:if>

        <form action="LoginServlet" method="post">
            <table>
                <tr>
                    <td><label>Username:</label></td>
                    <td><input type="text" name="username"
                               value="${lastUsername}"/></td>
                </tr>
                <tr>
                    <td><label>Password:</label></td>
                    <td><input type="password" name="password"/></td>
                </tr>
                <tr>
                    <td><label></label></td>
                    <td><input type="submit" value="Login"
                               class="add-student-button"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>

</body>
</html>
