package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JourneyType {
    PART_TIME("Partida"),
    FULL_TIME("Continua");

    private final String name;
}
