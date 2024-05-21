package model;

import lombok.*;

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

}
