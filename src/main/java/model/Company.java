package model;

import enums.JourneyType;
import enums.Modality;
import lombok.*;

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
}
