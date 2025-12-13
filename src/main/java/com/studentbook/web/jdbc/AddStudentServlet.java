package com.studentbook.web.jdbc;

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

// Déclaration du servlet et mapping de l'URL
@WebServlet("/AddStudentServlet")
public class AddStudentServlet extends HttpServlet {

    // Objet utilitaire pour les opérations sur la base de données
    private StudentDbUtil studentDbUtil;

    // Source de données (connexion à la base de données)
    private DataSource dataSource;

    /**
     * Méthode permettant de récupérer la DataSource via JNDI
     */
    private DataSource getDataSource() throws NamingException {
        // Nom JNDI défini dans le serveur (Tomcat, etc.)
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

            // Initialisation de l'utilitaire de base de données
            studentDbUtil = new StudentDbUtil(dataSource);
        } catch (NamingException e) {
            // En cas d'erreur JNDI, on stoppe le servlet
            throw new ServletException(e);
        }
    }

    /**
     * Méthode appelée lors d'une requête HTTP GET
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Récupération de la session existante (sans en créer une nouvelle)
        HttpSession session = request.getSession(false);

        // Vérification de l'authentification et du rôle
        if (session == null ||
                !"instructor".equals(session.getAttribute("role"))) {

            // Redirection si l'utilisateur n'est pas un instructeur
            response.sendRedirect("access-denied.jsp");
            return;
        }

        // Affichage de la page JSP pour ajouter un étudiant
        request.getRequestDispatcher("add-student.jsp")
                .forward(request, response);
    }

    /**
     * Méthode appelée lors d'une requête HTTP POST
     * (soumission du formulaire)
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // Vérification de la session et du rôle utilisateur
        HttpSession session = request.getSession(false);
        if (session == null ||
                !"instructor".equals(session.getAttribute("role"))) {

            // Redirection si accès non autorisé
            response.sendRedirect("access-denied.jsp");
            return;
        }

        // Récupération des données envoyées par le formulaire
        String firstName = request.getParameter("firstName");
        String lastName  = request.getParameter("lastName");
        String email     = request.getParameter("email");

        // Création de l'objet Student
        Student student = new Student(firstName, lastName, email);

        try {
            // Ajout de l'étudiant dans la base de données
            studentDbUtil.addStudent(student);
        } catch (Exception e) {
            // Gestion des erreurs liées à la base de données
            throw new ServletException(e);
        }

        // Redirection vers la liste des étudiants après ajout
        response.sendRedirect("StudentControllerServlet");
    }
}
