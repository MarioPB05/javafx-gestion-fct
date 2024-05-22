package model;

import lombok.*;
import utils.ConexionDB;
import utils.Utils;

import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Student implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
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

            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
