package GymBro.Model;

import GymBro.Model.enums.MuscleGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100)
    @NotBlank(message = "Название упражнения не должно быть пустым")
    @Size(min = 2, max = 50, message = "Длина названия упражнения должна быть от 2 до 50 символов")
    private String name;

    @Column
    private String description;

    @Column(name = "muscle_group")
    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Person createdBy;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> workoutExercises;

}
