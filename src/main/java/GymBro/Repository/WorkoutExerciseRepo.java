package GymBro.Repository;

import GymBro.Model.WorkoutExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface WorkoutExerciseRepo extends JpaRepository<WorkoutExercise, Integer> {

    @Query("SELECT we from WorkoutExercise we " +
            "JOIN FETCH we.workout workout " +
            "JOIN FETCH we.exercise exercise " +
            "JOIN FETCH workout.person person " +
            "WHERE workout.person.id = :personId " +
            "AND we.exercise.id = :exerciseId " +
            "ORDER BY workout.date DESC")
    Page<WorkoutExercise> findByPersonAndExercise(@Param("personId") int personId,
                                                  @Param("exerciseId") int exerciseId,
                                                  Pageable pageable);

    @Query("SELECT we from WorkoutExercise we " +
            "JOIN FETCH we.workout workout " +
            "JOIN FETCH we.exercise exercise " +
            "JOIN FETCH workout.person person " +
            "WHERE we.workout.person.id = :personId " +
            "AND we.exercise.id = :exerciseId " +
            "ORDER BY we.workout.date DESC")
    List<WorkoutExercise> findByPersonAndExercise(@Param("personId") int personId,
                                                  @Param("exerciseId") int exerciseId);


    @Query("SELECT we from WorkoutExercise we " +
            "JOIN FETCH we.workout workout " +
            "JOIN FETCH we.exercise exercise " +
            "WHERE workout.person.id = :personId " +
            "AND we.exercise.id = :exerciseId " +
            "AND workout.date < :date " +
            "ORDER BY workout.date DESC")
    List<WorkoutExercise> findPrevious(@Param("personId") int personId,
                                       @Param("exerciseId") int exerciseId,
                                       @Param("date") Timestamp date);

}
