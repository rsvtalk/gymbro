package GymBro.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(name = "person_weight")
    @DecimalMin(value = "20", message = "Вес должен быть больше 20кг")
    @DecimalMax(value = "500", message = "Вес должен быть меньше 500кг")
    private double personWeight;

    @Column(name = "weight_date")
    private Timestamp weightDate;

}
