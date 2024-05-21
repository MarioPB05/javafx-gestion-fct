package model;

import enums.JourneyType;
import enums.Modality;
import lombok.*;
import utils.ConexionDB;
import utils.Utils;

import java.sql.SQLException;

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

            Object[] params = {this.cif, this.name, this.address, this.postalCode, this.city, this.journeyType.toString(), this.modality.toString(), this.email, this.companyManager.getId(), this.companyTutor.getId()};
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

}
