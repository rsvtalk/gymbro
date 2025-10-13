package GymBro.Service;

import GymBro.Exception.NoObjectException;
import GymBro.Model.Person;
import GymBro.Model.Weight;
import GymBro.Model.enums.Gender;
import GymBro.Repository.WeightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeightService {

    WeightRepo weightRepo;
    WorkoutService workoutService;

    @Autowired
    public WeightService(WeightRepo weightRepo, WorkoutService workoutService) {
        this.weightRepo = weightRepo;
        this.workoutService = workoutService;
    }

    public List<Weight> getAllWeights(Person person) {
        return weightRepo.findAllByPersonOrderByWeightDateDesc(person);
    }

    public Page<Weight> getAllWeights(Person person, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("weightDate").descending());
        return weightRepo.findAllByPersonOrderByWeightDateDesc(person, pageable);
    }

    public List<Double> getWeightDifference(List<Weight> weights) {
        List<Double> results = new ArrayList<>();

        if (weights.isEmpty()) return results;
        if (weights.size() == 1) return List.of(0.0);

        for (int i = 1; i < weights.size(); i++) {
            results.add(weights.get(i - 1).getPersonWeight() - weights.get(i).getPersonWeight());
        }

        return results;
    }

    public Weight getLastWeight(Person person) {
        List<Weight> weights = getAllWeights(person);
        if (weights.isEmpty()) {
            throw new NoObjectException("No weight found");
        }
        return weights.getFirst();
    }

    @Transactional
    public void addWeight(Weight weight) {
        weight.setWeightDate(new Timestamp(System.currentTimeMillis()));
        weightRepo.save(weight);
    }

    public Boolean checkPreviousWeight(Person user) {
        return getAllWeights(user).isEmpty();
    }

    public List<Integer> getCalories(Person person) {
        double weight;

        try {
            weight = getLastWeight(person).getPersonWeight();
        } catch (NoObjectException e) {
            return List.of(0,0,0,0,0);
        }

        int height = person.getHeight();
        int age = LocalDate.now().getYear() - person.getYear();
        Gender gender = person.getGender();
        int countOfWorkouts = workoutService.getCountOfWorkouts(person);
        double bmr = (10 * weight) + (6.25 * height) - (5 * age);

        if (gender == Gender.MALE) {
            bmr += 5;
        } else {
            bmr -= 161;
        }

        double activityCoef;

        if (countOfWorkouts < 1) {
            activityCoef = 1.2;
        } else if (countOfWorkouts < 3) {
            activityCoef = 1.375;
        } else if (countOfWorkouts <= 5) {
            activityCoef = 1.55;
        } else if (countOfWorkouts <= 7) {
            activityCoef = 1.725;
        } else activityCoef = 1.9;


        int tdee = (int) (bmr * activityCoef);

        int notSafeDecrease = (int) (tdee * 0.75);
        int safeDecrease = (int) (tdee * 0.85);
        int safeRise = (int) (tdee * 1.10);
        int notSafeRise = (int) (tdee * 1.20);

        return new ArrayList<>(List.of(notSafeDecrease, safeDecrease, tdee, safeRise, notSafeRise));
    }
}

