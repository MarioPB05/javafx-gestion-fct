package model;

import lombok.*;
import utils.ConexionDB;
import utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class CompanyManager {

    private Integer id;
    private String dni;
    private String name;
    private String surname;

    public CompanyManager(String dni, String name, String surname) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
    }

    public Boolean save() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query;

            if (this.id != null) {
                query = "UPDATE company_manager SET dni = ?, name = ?, surname = ? WHERE id = ?";
            } else {
                query = "INSERT INTO company_manager (dni, name, surname) VALUES (?, ?, ?)";
            }

            Object[] params = {this.dni, this.name, this.surname};
            int rows = database.ejecutarInstruccionPreparada(query, params);

            if (this.id == null) {
                this.id = database.ultimoID("id", "company_manager");
            }

            return rows > 0;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public static CompanyManager get(Integer id) {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM company_manager WHERE id = ?";
            Object[] params = {id};

            database.ejecutarConsultaPreparada(query, params);

            ResultSet result = database.getResultSet();

            if (result.next()) {
                return new CompanyManager(
                        result.getInt("id"),
                        result.getString("dni"),
                        result.getString("name"),
                        result.getString("surname")
                );
            }

            return null;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return null;
        }
    }

}
