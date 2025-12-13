package com.studentbook.web.jdbc;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object)
 */
public class StudentDbUtil {

    // Source de données (pool de connexions)
    private DataSource dataSource;

    /**
     * Constructeur
     * @param theDataSource DataSource injectée depuis le servlet
     */
    public StudentDbUtil(DataSource theDataSource) {
        dataSource = theDataSource;
    }

    /**
     * Récupère la liste de tous les étudiants
     */
    public List<Student> getStudents() throws Exception {

        // Liste des étudiants à retourner
        List<Student> students = new ArrayList<>();

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            // Récupération d'une connexion depuis le pool
            myConn = dataSource.getConnection();

            // Requête SQL
            String sql = "select * from student order by last_name";

            // Création de la requête
            myStmt = myConn.createStatement();

            // Exécution de la requête
            myRs = myStmt.executeQuery(sql);

            // Parcours des résultats
            while (myRs.next()) {

                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                // Création de l'objet Student
                Student tempStudent =
                        new Student(id, firstName, lastName, email);

                // Ajout à la liste
                students.add(tempStudent);
            }

            return students;
        } finally {
            // Fermeture des ressources JDBC
            close(myConn, myStmt, myRs);
        }
    }

    /**
     * Ajoute un nouvel étudiant dans la base de données
     */
    public void addStudent(Student student) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            myConn = dataSource.getConnection();

            // Requête SQL paramétrée
            String sql =
                    "insert into student (first_name,last_name,email) values (?,?,?)";

            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, student.getFirstName());
            myStmt.setString(2, student.getLastName());
            myStmt.setString(3, student.getEmail());

            // Exécution de l'insertion
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    /**
     * Récupère un étudiant à partir de son id
     */
    public Student fetchStudent(int id) {

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        Student student = null;

        try {
            myConn = dataSource.getConnection();

            // Création de la requête SQL
            myStmt = myConn.createStatement();
            String sql = "select * from student where id=" + id;

            // Exécution de la requête
            myRs = myStmt.executeQuery(sql);

            // Lecture du résultat
            while (myRs.next()) {
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                student = new Student(id, firstName, lastName, email);
            }

            return student;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    /**
     * Met à jour les informations d'un étudiant
     */
    public void updateStudent(Student student) {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            myConn = dataSource.getConnection();

            String sql =
                    "update student set first_name=?, last_name=?, email=? where id=?";

            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, student.getFirstName());
            myStmt.setString(2, student.getLastName());
            myStmt.setString(3, student.getEmail());
            myStmt.setInt(4, student.getId());

            // Exécution de la mise à jour
            myStmt.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            close(myConn, myStmt, null);
        }
    }

    /**
     * Supprime un étudiant à partir de son id
     */
    public void deleteStudent(int id) {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            myConn = dataSource.getConnection();

            String sql = "delete from student where id=?";

            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, id);

            // Exécution de la suppression
            myStmt.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            close(myConn, myStmt, null);
        }
    }

    /**
     * Ferme proprement les ressources JDBC
     */
    private void close(Connection myConn,
                       Statement myStmt,
                       ResultSet myRs) {

        try {
            if (myStmt != null) myStmt.close();
            if (myRs != null) myRs.close();

            // La connexion est retournée au pool
            if (myConn != null) myConn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
