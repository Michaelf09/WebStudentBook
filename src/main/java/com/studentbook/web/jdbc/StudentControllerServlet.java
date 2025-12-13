package com.studentbook.web.jdbc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

// Déclaration du servlet et mapping de l'URL
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {

    // Utilitaire pour accéder aux données des étudiants
    private StudentDbUtil studentDbUtil;

    // Source de données (connexion à la base)
    private DataSource dataSource;

    /**
     * Récupère la DataSource via JNDI
     */
    private DataSource getDataSource() throws NamingException {
        // Nom JNDI configuré dans le serveur
        String jndi = "java:comp/env/jdbc/studentdb";

        // Contexte initial JNDI
        Context context = new InitialContext();

        // Recherche et retour de la DataSource
        return (DataSource) context.lookup(jndi);
    }

    /**
     * Méthode appelée au démarrage du servlet
     */
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Initialisation de la DataSource
            dataSource = getDataSource();

            // Initialisation de l'utilitaire étudiant
            studentDbUtil = new StudentDbUtil(dataSource);
        } catch (NamingException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    /**
     * Méthode appelée lors d'une requête HTTP GET
     * Affiche la liste des étudiants
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Vérification que l'utilisateur est connecté
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            // Redirection vers la page de connexion
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            // Récupération et affichage de la liste des étudiants
            listStudents(request, response);
        } catch (Exception e) {
            // Gestion des erreurs
            throw new ServletException(e);
        }
    }

    /**
     * Récupère les étudiants depuis la base de données
     * et les transmet à la page JSP
     */
    private void listStudents(HttpServletRequest request,
                              HttpServletResponse response) throws Exception {

        // Récupération de la liste des étudiants
        List<Student> students = studentDbUtil.getStudents();

        // Ajout de la liste comme attribut de requête
        request.setAttribute("STUDENT_LIST", students);

        // Redirection vers la page JSP d'affichage
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }
}
