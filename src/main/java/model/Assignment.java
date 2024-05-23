package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.*;
import utils.ConexionDB;
import utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Assignment {

    private Integer id;
    private Student student;
    private Company company;
    private Tutor tutor;
    private LocalDate creationDate;

    public Assignment(Student student, Company company, Tutor tutor) {
        this.student = student;
        this.company = company;
        this.tutor = tutor;
        this.creationDate = LocalDate.now();
    }

    public Boolean save() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "INSERT INTO assignment (student_id, company_id, tutor_id, creation_date) VALUES (?, ?, ?, ?)";
            Object[] params = {this.student.getId(), this.company.getId(), this.tutor.getId(), Utils.formatDateDatabase(this.creationDate)};

            int rows = database.ejecutarInstruccionPreparada(query, params);

            if (this.id != null) {
                this.id = database.ultimoID("id", "assigment");
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
            String query = "DELETE FROM assignment WHERE id = ?";
            Object[] params = {this.id};

            int rows = database.ejecutarInstruccionPreparada(query, params);

            return rows > 0;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public static ObservableList<Assignment> getAssigments() {
        ObservableList<Assignment> assigments = FXCollections.observableArrayList();

        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM assignment";
            database.ejecutarConsulta(query);
            ResultSet rs = database.getResultSet();

            while (rs.next()) {
                Assignment assigment = new Assignment(
                        rs.getInt("id"),
                        Student.get(rs.getInt("student_id")),
                        Company.get(rs.getInt("company_id")),
                        Tutor.get(rs.getInt("tutor_id")),
                        rs.getDate("creation_date").toLocalDate()
                );

                assigments.add(assigment);
            }

            return assigments;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return null;
        }
    }

}
