package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.*;
import utils.ConexionDB;
import utils.Utils;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Student implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String dni;
    private String name;
    private String surname;
    private LocalDate birthdate;

    public Student(String dni, String name, String surname, LocalDate birthdate) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
    }

    public Boolean save() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "INSERT INTO student (dni, name, surname, birthdate) VALUES (?, ?, ?, ?)";
            Object[] params = {this.dni, this.name, this.surname, Utils.formatDateDatabase(this.birthdate)};

            int rows = database.ejecutarInstruccionPreparada(query, params);

            if (this.id != null) {
                this.id = database.ultimoID("id", "student");
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
            String query = "DELETE FROM student WHERE id = ?";
            Object[] params = {this.id};

            int rows = database.ejecutarInstruccionPreparada(query, params);

            return rows > 0;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public static ObservableList<Student> getStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();

        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM student";
            database.ejecutarConsulta(query);
            ResultSet rs = database.getResultSet();

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getDate("birthdate").toLocalDate()
                );

                students.add(student);
            }

            return students;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return null;
        }
    }

}
