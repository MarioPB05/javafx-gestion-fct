package model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Assigment {

    private int id;
    private Student student;
    private Company company;
    private Tutor tutor;
    private LocalDate creationDate;

}
