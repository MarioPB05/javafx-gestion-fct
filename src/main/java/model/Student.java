package model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Student {

    private int id;
    private String dni;
    private String name;
    private String surname;
    private LocalDate birthdate;

    public Student(String dni, String name, String surname, LocalDate birthdate) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
    }

}
