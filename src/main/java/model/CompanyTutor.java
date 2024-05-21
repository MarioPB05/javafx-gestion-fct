package model;

import lombok.*;
import utils.ConexionDB;
import utils.Utils;

import java.sql.SQLException;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class CompanyTutor {

    private Integer id;
    private String dni;
    private String name;
    private String surname;
    private String phone;

    public CompanyTutor(String dni, String name, String surname, String phone) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    public Boolean save() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query;

            if (this.id != null) {
                query = "UPDATE company_tutor SET dni = ?, name = ?, surname = ?, phone = ? WHERE id = ?";
            } else {
                query = "INSERT INTO company_tutor (dni, name, surname, phone) VALUES (?, ?, ?, ?)";
            }

            Object[] params = {this.dni, this.name, this.surname, this.phone};
            int rows = database.ejecutarInstruccionPreparada(query, params);

            if (this.id == null) {
                this.id = database.ultimoID("id", "company_tutor");
            }

            return rows > 0;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

}
