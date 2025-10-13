package GymBro.Service;

import GymBro.Model.Person;
import GymBro.Repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class PersonService {

    private final PersonRepo personRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepo personRepo, PasswordEncoder passwordEncoder) {
        this.personRepo = personRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepo.save(person);
    }

    public boolean usernameExists(String username) {
        return personRepo.findByUsername(username).isPresent();
    }

    @Transactional
    public void update(Person updatePerson,
                       Person user) {

        user.setName(updatePerson.getName());
        user.setHeight(updatePerson.getHeight());
        user.setYear(updatePerson.getYear());
        user.setWeightUnit(updatePerson.getWeightUnit());
        user.setHeightUnit(updatePerson.getHeightUnit());

        personRepo.save(user);
    }

}
