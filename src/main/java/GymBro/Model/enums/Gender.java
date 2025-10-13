package GymBro.Model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Gender {

    MALE("Муж."), FEMALE("Жен.");

    @Getter
    private final String genderName;

}
