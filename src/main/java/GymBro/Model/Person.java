package GymBro.Model;

import GymBro.Model.enums.Gender;
import GymBro.Model.enums.HeightUnit;
import GymBro.Model.enums.WeightUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Вы ввели некорректный email")
    @Size(min = 6, max = 50, message = "Email может быть от 6 до 50 символов")
    private String username;

    @Column
    @Min(value = 1925, message = "Год рождения должен быть больше 1925")
    @Max(value = 2025, message = "Год рождения должен быть меньше 2025")
    private Integer year;

    @Column
    @Size(min = 5, message = "Пароль должен быть больше 5 символов")
    private String password;

    @Column
    @Min(value = 50, message = "Самый низкий человек в мире - 54см")
    @Max(value = 250, message = "Самый высокий человек в мире - 251см")
    int height;

    @Column
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit = WeightUnit.KG;

    @Enumerated(EnumType.STRING)
    private HeightUnit heightUnit = HeightUnit.CM;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Weight> weights;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workout> workouts;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> ownExercises;

}
