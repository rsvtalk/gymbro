package GymBro.Repository;

import GymBro.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {

    public Optional<Person> findByUsername(String username);

}
