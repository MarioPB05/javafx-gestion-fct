package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.*;
import utils.ConexionDB;
import utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Tutor {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String phone;

    public Tutor(String name, String surname, String email, String phone) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
    }

    public Boolean save() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "INSERT INTO tutor (name, surname, email, phone) VALUES (?, ?, ?, ?)";
            Object[] params = {this.name, this.surname, this.email, this.phone};

            int rows = database.ejecutarInstruccionPreparada(query, params);

            if (this.id == null) {
                this.id = database.ultimoID("id", "tutor");
            }

            return rows > 0;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public Boolean delete() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "DELETE FROM tutor WHERE id = ?";
            Object[] params = {this.id};

            int rows = database.ejecutarInstruccionPreparada(query, params);

            return rows > 0;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public static ObservableList<Tutor> getTutors() {
        ObservableList<Tutor> tutors = FXCollections.observableArrayList();

        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM tutor";
            database.ejecutarConsulta(query);
            ResultSet rs = database.getResultSet();

            while (rs.next()) {
                Tutor tutor = new Tutor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
                tutors.add(tutor);
            }

            return tutors;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return null;
        }
    }

    public static Tutor get(Integer id) {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM tutor WHERE id = ?";
            Object[] params = {id};

            database.ejecutarConsultaPreparada(query, params);

            ResultSet result = database.getResultSet();

            if (result.next()) {
                return new Tutor(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getString("surname"),
                    result.getString("email"),
                    result.getString("phone")
                );
            }
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
        }

        return null;
    }

    @Override
    public String toString() {
        return this.name + " " + this.surname + " (" + this.email + ")";
    }

}
