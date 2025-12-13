package com.studentbook.web.jdbc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    // Source de données (connexion à la base)
    private DataSource dataSource;

    // Utilitaire pour la gestion des utilisateurs
    private UserDbUtil userDbUtil;

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

            // Initialisation de l'utilitaire utilisateur
            userDbUtil = new UserDbUtil(dataSource);
        } catch (NamingException e) {
            // Arrêt du servlet en cas d'erreur JNDI
            throw new ServletException(e);
        }
    }

    /**
     * Méthode appelée lors d'une requête HTTP GET
     * Affiche la page de connexion
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        /*
         * Pré-remplissage du champ username à partir d'un cookie
         */
        String lastUsername = "";
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie c : cookies) {
                // Recherche du cookie nommé "username"
                if ("username".equals(c.getName())) {
                    lastUsername = c.getValue();
                    break;
                }
            }
        }

        // Envoi du dernier username à la page JSP
        request.setAttribute("lastUsername", lastUsername);

        // Affichage de la page login.jsp
        request.getRequestDispatcher("login.jsp")
                .forward(request, response);
    }

    /**
     * Méthode appelée lors d'une requête HTTP POST
     * Traite le formulaire de connexion
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // Récupération des identifiants saisis
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Vérification des identifiants dans la base de données
        User user = userDbUtil.validateUser(username, password);

        /*
         * Si l'utilisateur n'existe pas ou si le mot de passe est incorrect
         */
        if (user == null) {
            // Message d'erreur
            request.setAttribute("LOGIN_ERROR", "Invalid username or password");

            // Pré-remplissage du champ username
            request.setAttribute("lastUsername", username);

            // Retour à la page de connexion
            request.getRequestDispatcher("login.jsp")
                    .forward(request, response);
            return;
        }

        /*
         * Connexion réussie
         */

        // Création d'un cookie pour mémoriser le username
        Cookie cookie = new Cookie("username", username);
        cookie.setMaxAge(7 * 24 * 60 * 60); // Cookie valide 7 jours
        response.addCookie(cookie);

        // Création de la session utilisateur
        HttpSession session = request.getSession(true);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        // Redirection vers la liste des étudiants
        response.sendRedirect("StudentControllerServlet");
    }
}
