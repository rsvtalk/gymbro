package GymBro.Model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MuscleGroup {

    FULL_BODY("Всё тело", "Full Body день"),
    SHOULDERS("Плечи", "День плечей"),
    CHEST("Грудь", "День груди"),
    BACK("Спина","День спины"),
    ARMS("Руки", "День рук"),
    CORE("Мыщцы кора", "День мышц кора"),
    LEGS("Ноги", "День ног"),
    CARDIO("Кардио", "День кардио"),
    OTHER("Прочее", "Смешанный день");

    @Getter
    private final String russianName;

    @Getter
    private final String russianDayName;

}
