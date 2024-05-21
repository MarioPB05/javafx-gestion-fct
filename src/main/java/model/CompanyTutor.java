package model;

import lombok.*;
import utils.ConexionDB;
import utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

            ArrayList<Object> params = new ArrayList<>();
            params.add(this.dni);
            params.add(this.name);
            params.add(this.surname);
            params.add(this.phone);

            if (this.id != null) {
                params.add(this.id);
            }

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

    public Boolean delete() {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "DELETE FROM company_tutor WHERE id = ?";
            Object[] params = {this.id};

            int rows = database.ejecutarInstruccionPreparada(query, params);

            return rows > 0;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public static CompanyTutor get(Integer id) {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM company_tutor WHERE id = ?";
            Object[] params = {id};

            database.ejecutarConsultaPreparada(query, params);

            ResultSet result = database.getResultSet();

            if (result.next()) {
                return new CompanyTutor(
                        result.getInt("id"),
                        result.getString("dni"),
                        result.getString("name"),
                        result.getString("surname"),
                        result.getString("phone")
                );
            }

            return null;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return null;
        }
    }

}
