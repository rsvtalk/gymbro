package GymBro.Repository;

import GymBro.Model.Person;
import GymBro.Model.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface WorkoutRepo extends JpaRepository<Workout, Integer> {

    Page<Workout> findByPerson(Person person, Pageable pageable);

    Integer countByPersonAndDateBetween(Person person, Timestamp weekAgo, Timestamp now);

}
