package GymBro.Repository;

import GymBro.Model.Person;
import GymBro.Model.Weight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRepo extends JpaRepository<Weight, Integer> {

    List<Weight> findAllByPersonOrderByWeightDateDesc(Person person);

    Page<Weight> findAllByPersonOrderByWeightDateDesc(Person person, Pageable pageable);

}
