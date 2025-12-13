package com.studentbook.web.jdbc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Déclaration du servlet et mapping de l'URL
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    /**
     * Méthode appelée lors d'une requête HTTP GET
     * Déconnecte l'utilisateur
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        // Récupération de la session existante sans en créer une nouvelle
        HttpSession session = request.getSession(false);

        // Si une session existe, on la détruit
        if (session != null) {
            session.invalidate(); // Supprime toutes les données de session
        }

        // Redirection vers la page de connexion
        response.sendRedirect("LoginServlet");
    }
}
