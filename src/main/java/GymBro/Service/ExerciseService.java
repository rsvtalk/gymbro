package GymBro.Service;

import GymBro.Model.Exercise;
import GymBro.Model.Person;
import GymBro.Model.WorkoutExercise;
import GymBro.Repository.ExerciseRepo;
import GymBro.Exception.AccessException;
import GymBro.Exception.NoObjectException;
import GymBro.Repository.WorkoutExerciseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExerciseService {

    private final WorkoutExerciseRepo workoutExerciseRepo;
    ExerciseRepo exerciseRepo;

    @Autowired
    public ExerciseService(ExerciseRepo exerciseRepo, WorkoutExerciseRepo workoutExerciseRepo) {
        this.exerciseRepo = exerciseRepo;
        this.workoutExerciseRepo = workoutExerciseRepo;
    }

    public Exercise getExercise(int id, int userId) {
        Exercise exercise = exerciseRepo.findById(id).orElseThrow(() ->
                new NoObjectException("There is no exercise with id = " + id + " :("));

        if (exercise.getCreatedBy() != null && exercise.getCreatedBy().getId() != userId) {
            throw new AccessException("You are not authorized to access this exercise :(");
        }

        return exercise;
    }

    public List<Exercise> getSystemExercises() {
        return exerciseRepo.findByCreatedByIsNullOrderByName();
    }

    public List<Exercise> getExercisesByCreator(Person person) {
        return exerciseRepo.findByCreatedByOrderByName(person);
    }

    @Transactional
    public void addExercise(Exercise exercise) {
        exerciseRepo.save(exercise);
    }

    @Transactional
    public void updateExercise(Exercise updateExercise, int id, int userId) {
        Exercise exercise = getExercise(id, userId);

        exercise.setName(updateExercise.getName());
        exercise.setDescription(updateExercise.getDescription());
        exercise.setMuscleGroup(updateExercise.getMuscleGroup());

        exerciseRepo.save(exercise);
    }

    @Transactional
    public void deleteExercise(int id, int userId) {
        Exercise exercise = getExercise(id, userId);
        exerciseRepo.delete(exercise);
    }

    public List<Exercise> getUsedExercises(int id) {
        return exerciseRepo.findExerciseByUser(id);
    }

    public List<WorkoutExercise> getExerciseHistory(int personId, int exerciseId) {
        return workoutExerciseRepo.findByPersonAndExercise(personId, exerciseId);
    }

    public Page<WorkoutExercise> getExerciseHistory(int personId, int exerciseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return workoutExerciseRepo.findByPersonAndExercise(personId, exerciseId, pageable);
    }

}
