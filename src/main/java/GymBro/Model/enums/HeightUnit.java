package GymBro.Model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum HeightUnit {

    CM("cm", "Сантиметры"), FT("ft", "Футы");

    @Getter
    private final String shortName;

    @Getter
    private final String fullRussianName;

}
