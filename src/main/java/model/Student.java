package model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Student implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
