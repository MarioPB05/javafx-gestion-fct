package model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class CompanyTutor {

    private int id;
    private String dni;
    private String name;
    private String surname;
    private String phone;

}
