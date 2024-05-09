package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JourneyType {
    PART_TIME("Parcial"),
    FULL_TIME("Completa");

    private final String name;
}
