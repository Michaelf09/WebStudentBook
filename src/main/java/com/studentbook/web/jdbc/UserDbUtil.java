package com.studentbook.web.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Classe DAO pour la gestion des utilisateurs
 * Gère l'authentification via la table users
 */
public class UserDbUtil {

    // Source de données (pool de connexions)
    private DataSource dataSource;

    /**
     * Constructeur
     * @param dataSource DataSource injectée depuis le servlet
     */
    public UserDbUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Vérifie les identifiants de l'utilisateur
     * @param username nom d'utilisateur
     * @param password mot de passe
     * @return User si authentification réussie, null sinon
     */
    public User validateUser(String username, String password) {

        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;

        try {
            // Récupération d'une connexion depuis le pool
            myConn = dataSource.getConnection();

            // Requête SQL paramétrée pour éviter les injections SQL
            String sql =
                    "select * from users where username=? and password=?";

            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, username);
            myStmt.setString(2, password);

            // Exécution de la requête
            myRs = myStmt.executeQuery();

            // Si un utilisateur est trouvé
            if (myRs.next()) {
                int id = myRs.getInt("id");
                String role = myRs.getString("role");

                // Création et retour de l'objet User
                return new User(id, username, password, role);
            } else {
                // Aucun utilisateur trouvé
                return null;
            }

        } catch (Exception e) {
            // Affichage de l'erreur en console
            System.out.println(e.getMessage());
            return null;

        } finally {
            // Fermeture des ressources JDBC
            try {
                if (myRs != null) myRs.close();
                if (myStmt != null) myStmt.close();
                if (myConn != null) myConn.close(); // retourne au pool
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
