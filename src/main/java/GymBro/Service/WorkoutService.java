package GymBro.Service;

import GymBro.Model.Person;
import GymBro.Model.Workout;
import GymBro.Model.WorkoutExercise;
import GymBro.Repository.WorkoutRepo;
import GymBro.Exception.AccessException;
import GymBro.Exception.NoObjectException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class WorkoutService {

    WorkoutRepo workoutRepo;
    ExerciseService exerciseService;

    @Autowired
    public WorkoutService(WorkoutRepo workoutRepo,
                          ExerciseService exerciseService) {
        this.workoutRepo = workoutRepo;
        this.exerciseService = exerciseService;
    }

    public Workout findById(int id, int userId) {
        Workout workout = workoutRepo.findById(id)
                .orElseThrow(() -> new NoObjectException("No workout found with id " + id));

        if (workout.getPerson().getId() != userId) {
            throw new AccessException("You do not have permission to access this workout");
        }

        return workout;
    }

    public Page<Workout> getPersonWorkouts(Person person, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return workoutRepo.findByPerson(person, pageable);
    }

    @Transactional
    public void addWorkout(Workout workout) {
        workoutRepo.save(workout);
    }

    @Transactional
    public void deleteWorkout(int id, int userId) {
        Workout workout = findById(id, userId);
        workoutRepo.delete(workout);
    }

    public Integer getCountOfWorkouts(Person person) {
        LocalDateTime nowLDT = LocalDateTime.now();
        LocalDateTime weekAgoLDT = nowLDT.minusDays(7);

        Timestamp now = Timestamp.valueOf(nowLDT);
        Timestamp weekAgo = Timestamp.valueOf(weekAgoLDT);

        return workoutRepo.countByPersonAndDateBetween(person, weekAgo, now);
    }

    @Transactional
    public void addWorkoutInParts(Person user,
                                Workout workout,
                                List<Integer> exerciseIds,
                                List<Double> weights,
                                List<Integer> reps) {

        if(exerciseIds.size() != weights.size() || exerciseIds.size() != reps.size()) {
            throw new ValidationException("Переданы некорректные объемы данных");
        }

        if (exerciseIds.isEmpty()) {
            throw new ValidationException("Выберите хотя бы одно упражнение");
        }

        List<WorkoutExercise> exercises = new ArrayList<>();

        for(int i = 0; i < exerciseIds.size(); i++) {
            WorkoutExercise workoutExercise = new WorkoutExercise();

            if(weights.get(i) <= 0 || reps.get(i) <= 0) {
                throw new ValidationException("Неправильное значение");
            }

            workoutExercise.setWorkout(workout);
            workoutExercise.setExercise(exerciseService.getExercise(exerciseIds.get(i), user.getId()));
            workoutExercise.setWorkWeight(weights.get(i));
            workoutExercise.setReps(reps.get(i));

            exercises.add(workoutExercise);
        }

        workout.setWorkoutExercises(exercises);
        workout.setPerson(user);
        workout.setDate(new Timestamp(System.currentTimeMillis()));

        addWorkout(workout);
    }

}
