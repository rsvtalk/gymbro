package GymBro.Model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WeightUnit {

    KG("kg", "Килограммы"), LB("lbs", "Фунты");

    @Getter
    private final String shortName;

    @Getter
    private final String fullRussianName;

}
