package model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Tutor {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String phone;

}
