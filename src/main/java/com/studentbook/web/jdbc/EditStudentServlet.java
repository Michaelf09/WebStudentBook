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
@WebServlet("/EditStudentServlet")
public class EditStudentServlet extends HttpServlet {

    // Utilitaire pour les opérations CRUD sur la base de données
    private StudentDbUtil studentDbUtil;

    // Source de données (connexion à la base)
    private DataSource dataSource;

    /*
     * ATTENTION :
     * L'identifiant de l'étudiant est stocké dans un champ de classe.
     * Cette approche fonctionne pour un projet pédagogique,
     * mais n'est PAS recommandée en production (problème de concurrence).
     */
    private int id;

    /**
     * Récupère la DataSource via JNDI
     */
    private DataSource getDataSource() throws NamingException {
        // Nom JNDI configuré dans le serveur (ex : Tomcat)
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
            // Arrêt du servlet en cas d'erreur JNDI
            throw new ServletException(e);
        }
    }

    /**
     * Méthode appelée lors d'une requête HTTP GET
     * Affiche le formulaire de modification d'un étudiant
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Récupération de la session existante
        HttpSession session = request.getSession(false);

        // Vérification de l'existence de la session et du rôle utilisateur
        if (session == null ||
                !"instructor".equals(session.getAttribute("role"))) {

            // Redirection si l'utilisateur n'est pas autorisé
            response.sendRedirect("access-denied.jsp");
            return;
        }

        // Récupération de l'identifiant de l'étudiant depuis l'URL
        id = Integer.parseInt(request.getParameter("studentId"));

        // Récupération de l'étudiant depuis la base de données
        Student student = studentDbUtil.fetchStudent(id);

        // Ajout de l'étudiant comme attribut de requête
        request.setAttribute("Student", student);

        // Redirection vers la page JSP d'édition
        request.getRequestDispatcher("edit-student.jsp")
                .forward(request, response);
    }

    /**
     * Méthode appelée lors d'une requête HTTP POST
     * Met à jour les informations de l'étudiant
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // Vérification de la session et du rôle
        HttpSession session = request.getSession(false);
        if (session == null ||
                !"instructor".equals(session.getAttribute("role"))) {

            // Redirection si accès non autorisé
            response.sendRedirect("StudentControllerServlet");
            return;
        }

        // Récupération des données du formulaire
        String fn = request.getParameter("firstName");
        String ln = request.getParameter("lastName");
        String email = request.getParameter("email");

        // Création de l'objet Student mis à jour
        Student student = new Student(id, fn, ln, email);

        // Mise à jour de l'étudiant dans la base de données
        studentDbUtil.updateStudent(student);

        // Redirection vers la liste des étudiants
        response.sendRedirect("StudentControllerServlet");
    }
}
