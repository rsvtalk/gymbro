package GymBro.Repository;

import GymBro.Model.Exercise;
import GymBro.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepo extends JpaRepository<Exercise, Integer> {

    List<Exercise> findByCreatedByOrderByName(Person person);

    List<Exercise> findByCreatedByIsNullOrderByName();

    @Query("SELECT DISTINCT exercise FROM Exercise exercise " +
            "JOIN exercise.workoutExercises we " +
            "JOIN we.workout workout " +
            "WHERE workout.person.id = :userId " +
            "ORDER BY exercise.name")
    List<Exercise> findExerciseByUser(@Param("userId") int userId);

}
