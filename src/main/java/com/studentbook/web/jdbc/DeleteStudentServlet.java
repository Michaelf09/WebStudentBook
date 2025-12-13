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
@WebServlet("/DeleteStudentServlet")
public class DeleteStudentServlet extends HttpServlet {

    // Utilitaire pour accéder à la base de données
    private StudentDbUtil studentDbUtil;

    // Source de données (connexion à la base)
    private DataSource dataSource;

    /**
     * Récupère la DataSource via JNDI
     */
    private DataSource getDataSource() throws NamingException {
        // Nom JNDI configuré dans le serveur (ex: Tomcat)
        String jndi = "java:comp/env/jdbc/studentdb";

        // Contexte initial JNDI
        Context context = new InitialContext();

        // Recherche de la DataSource
        return (DataSource) context.lookup(jndi);
    }

    /**
     * Méthode exécutée au démarrage du servlet
     */
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Initialisation de la connexion à la base
            dataSource = getDataSource();

            // Initialisation de l'utilitaire de base de données
            studentDbUtil = new StudentDbUtil(dataSource);
        } catch (NamingException e) {
            // Arrêt du servlet en cas d'erreur
            throw new ServletException(e);
        }
    }

    /**
     * Méthode appelée lors d'une requête HTTP GET
     * Supprime un étudiant
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Récupération de la session existante
        HttpSession session = request.getSession(false);

        // Vérification de l'existence de la session et du rôle
        if (session == null ||
                !"instructor".equals(session.getAttribute("role"))) {

            // Redirection si l'utilisateur n'est pas autorisé
            response.sendRedirect("access-denied.jsp");
            return;
        }

        // Récupération de l'identifiant de l'étudiant à supprimer
        int id = Integer.parseInt(request.getParameter("studentId"));

        // Suppression de l'étudiant dans la base de données
        studentDbUtil.deleteStudent(id);

        // Redirection vers la liste des étudiants après suppression
        response.sendRedirect("StudentControllerServlet");
    }
}
