package model;

import enums.JourneyType;
import enums.Modality;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class Company {

    private Integer id;
    private String cif;
    private String name;
    private String address;
    private String postalCode;
    private String city;
    private JourneyType journeyType;
    private Modality modality;
    private String email;
    private CompanyManager companyManager;
    private CompanyTutor companyTutor;

    public Company(String cif, String name, String address, String postalCode, String city, JourneyType journeyType, Modality modality, String email, CompanyManager companyManager, CompanyTutor companyTutor) {
        this.cif = cif;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.journeyType = journeyType;
        this.modality = modality;
        this.email = email;
        this.companyManager = companyManager;
        this.companyTutor = companyTutor;
    }

    public boolean checkExistence() {
        ConexionDB database = Utils.getDatabaseConnection();
        return database.existeValor(this.cif, "cif", "company");
    }

    public Boolean save() {
        try {
            if (!this.companyManager.save() || !this.companyTutor.save()) {
                return false;
            }

            ConexionDB database = Utils.getDatabaseConnection();
            String query;

            if (this.id != null) {
                query = "UPDATE company SET cif = ?, name = ?, address = ?, postal_code = ?, city = ?, journey_type = ?, modality = ?, email = ?, manager_id = ?, tutor_id = ? WHERE id = ?";
            }else {
                query = "INSERT INTO company (cif, name, address, postal_code, city, journey_type, modality, email, manager_id, tutor_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            }

            ArrayList<Object> params = new ArrayList<>();

            params.add(this.cif);
            params.add(this.name);
            params.add(this.address);
            params.add(this.postalCode);
            params.add(this.city);
            params.add(this.journeyType.toString());
            params.add(this.modality.toString());
            params.add(this.email);
            params.add(this.companyManager.getId());
            params.add(this.companyTutor.getId());

            if (this.id != null) {
                params.add(this.id);
            }

            int rows = database.ejecutarInstruccionPreparada(query, params);

            if (this.id == null) {
                this.id = database.ultimoID("id", "company");
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
            String query = "DELETE FROM company WHERE id = ?";
            Object[] params = {this.id};

            int rows = database.ejecutarInstruccionPreparada(query, params);

            if (!this.companyManager.delete() || !this.companyTutor.delete()) {
                return false;
            }

            return rows > 0;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return false;
        }
    }

    public static ObservableList<Company> getAllCompanies() {
        ObservableList<Company> companies = FXCollections.observableArrayList();

        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM company";
            database.ejecutarConsulta(query);
            ResultSet result = database.getResultSet();

            while (result.next()) {
                CompanyManager companyManager = CompanyManager.get(result.getInt("manager_id"));
                CompanyTutor companyTutor = CompanyTutor.get(result.getInt("tutor_id"));

                Company company = new Company(
                        result.getInt("id"),
                        result.getString("cif"),
                        result.getString("name"),
                        result.getString("address"),
                        result.getString("postal_code"),
                        result.getString("city"),
                        JourneyType.valueOf(result.getString("journey_type")),
                        Modality.valueOf(result.getString("modality")),
                        result.getString("email"),
                        companyManager,
                        companyTutor
                );

                companies.add(company);
            }

            return companies;
        }catch (SQLException e) {
            Utils.errorLogger(e.getMessage());

            return null;
        }
    }

    public static Company get(Integer id) {
        try {
            ConexionDB database = Utils.getDatabaseConnection();
            String query = "SELECT * FROM company WHERE id = ?";
            Object[] params = {id};

            database.ejecutarConsultaPreparada(query, params);
            ResultSet result = database.getResultSet();

            if (result.next()) {
                CompanyManager companyManager = CompanyManager.get(result.getInt("manager_id"));
                CompanyTutor companyTutor = CompanyTutor.get(result.getInt("tutor_id"));

                return new Company(
                        result.getInt("id"),
                        result.getString("cif"),
                        result.getString("name"),
                        result.getString("address"),
                        result.getString("postal_code"),
                        result.getString("city"),
                        JourneyType.valueOf(result.getString("journey_type")),
                        Modality.valueOf(result.getString("modality")),
                        result.getString("email"),
                        companyManager,
                        companyTutor
                );
            }

            return null;
        } catch (SQLException e) {
            Utils.errorLogger(e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return this.name + ", " + this.city + " (" + this.cif + ")";
    }

}
