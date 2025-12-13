<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Web Student Tracker</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>

<div id="wrapper">
    <div id="header">
        <h2>ESILV Engineer School</h2>
    </div>
</div>

<div id="container">
    <div>
        <p>
            Welcome,
            <strong>${sessionScope.username}</strong>
            (<em>${sessionScope.role}</em>)
            |
            <a href="LogoutServlet" style="color:black;">Logout</a>
        </p>
    </div>

    <div id="content">

        <!-- Bouton Add Student seulement pour l'instructor -->
        <c:if test="${sessionScope.role == 'instructor'}">
            <form action="AddStudentServlet" method="get">
                <input type="submit" value="Add new Student"
                       class="add-student-button"/>
            </form>
        </c:if>

        <table>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>

            <c:forEach var="tempStudent" items="${STUDENT_LIST}">
                <c:url var="editLink" value="EditStudentServlet">
                    <c:param name="studentId"
                             value="${tempStudent.id}"/>
                </c:url>

                <c:url var="deleteLink" value="DeleteStudentServlet">
                    <c:param name="studentId"
                             value="${tempStudent.id}"/>
                </c:url>

                <tr>
                    <td>${tempStudent.firstName}</td>
                    <td>${tempStudent.lastName}</td>
                    <td>${tempStudent.email}</td>
                    <td>
                        <c:if test="${sessionScope.role == 'instructor'}">
                            <a href="${editLink}">Edit</a>
                            |
                            <a href="${deleteLink}"
                               onclick="return confirm('Delete this student?');">
                                Delete
                            </a>
                        </c:if>
                        <c:if test="${sessionScope.role == 'student'}">
                            <!-- Student: read-only -->
                            View only
                        </c:if>
                    </td>
                </tr>
            </c:forEach>

        </table>
    </div>
</div>

</body>
</html>
