# WebStudentBook

##  Project Overview

**WebStudentBook** is a Java EE web application developed using **JSP, Servlets, JDBC, and MySQL**.  
The application allows authenticated users to view and manage a list of students, with **role-based access control**.

This project was developed as part of a web technologies course to demonstrate:
- MVC architecture
- Session-based authentication
- Role-based authorization
- Secure access to web resources
- JDBC database integration
- Modern UI with CSS

---

##  Project Architecture (MVC)

- **Model**
  - `Student`
  - `User`
  - `StudentDbUtil`

- **View**
  - `login.jsp`
  - `list-students.jsp`
  - `add-student.jsp`
  - `edit-student.jsp`
  - `access-denied.jsp`

- **Controller**
  - `LoginServlet`
  - `LogoutServlet`
  - `StudentControllerServlet`
  - `AddStudentServlet`
  - `EditStudentServlet`
  - `DeleteStudentServlet`

---

##  Authentication & Authorization

### User Roles
- **Instructor**
  - Can add, edit, and delete students
- **Student**
  - Can only view the list of students

### Security Features
- Session-based authentication
- Role stored in session (`role`)
- Restricted access enforced at:
  - Servlet level
  - JSP level
  - UI level (buttons hidden for students)

### Access Denied Handling
If a student tries to access a restricted resource (even via URL), they are redirected to a dedicated page:

