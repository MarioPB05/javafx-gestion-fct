package model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class CompanyManager {

    private int id;
    private String dni;
    private String name;
    private String surname;

}
