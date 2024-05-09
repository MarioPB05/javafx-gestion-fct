package model;

import enums.JourneyType;
import enums.Modality;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Company {

    private int id;
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

}
