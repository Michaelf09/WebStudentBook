<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${sessionScope.role ne 'instructor'}">
    <c:redirect url="access-denied.jsp"/>
</c:if>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet"
          href="css/add-student-style.css">
    <link type="text/css" rel="stylesheet"
          href="css/style.css">
    <title>Edit a student</title>
</head>
<body>

<div id="wrapper">
    <div id="header">
        <h2>ESILV Engineer School</h2>
    </div>
</div>

<div id="container">
    <h3>Edit a Student</h3>

    <form action="EditStudentServlet" method="post">
        <table>
            <tbody>
            <tr>
                <td><label>FirstName: </label></td>
                <td><input type="text" name="firstName"
                           value="${Student.firstName}" required/></td>
            </tr>
            <tr>
                <td><label>LastName: </label></td>
                <td><input type="text" name="lastName"
                           value="${Student.lastName}" required/></td>
            </tr>
            <tr>
                <td><label>Email: </label></td>
                <td><input type="text" name="email"
                           value="${Student.email}" required/></td>
            </tr>
            <tr>
                <td><label></label></td>
                <td><input type="submit" value="Save"
                           class="save"/></td>
            </tr>
            </tbody>
        </table>
    </form>

    <div style="clear:both;"></div>
    <a href="StudentControllerServlet">Back to List</a>
</div>

</body>
</html>
