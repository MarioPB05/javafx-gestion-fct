package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Modality {
    FACE_TO_FACE("Presencial"),
    BLENDED("Semipresencial"),
    DISTANCE("A distancia");

    private final String name;
}
