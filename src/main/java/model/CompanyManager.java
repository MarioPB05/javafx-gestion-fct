package model;

import lombok.*;

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
}
