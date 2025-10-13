package GymBro.Service;

import GymBro.Model.Workout;
import GymBro.Model.WorkoutExercise;
import GymBro.Repository.WorkoutExerciseRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class StatisticService {


    private final WorkoutExerciseRepo workoutExerciseRepo;

    public StatisticService(WorkoutExerciseRepo workoutExerciseRepo) {
        this.workoutExerciseRepo = workoutExerciseRepo;
    }

    public List<Double> getWeightChangeInPercent(List<WorkoutExercise> we) {

        if (we.isEmpty()) {
            return new ArrayList<>();
        }

        List<WorkoutExercise> rewe = we.reversed();
        List<Double> result = new ArrayList<>();

        double prev = rewe.getFirst().getWorkWeight();

        for (int i = 0; i < we.size(); i++) {
            double x = rewe.get(i).getWorkWeight();
            double res = (x - prev)/prev*100;
            prev = x;
            result.add(Math.round(res * 10.0) / 10.0);
        }

        return result.reversed();
    }

    public List<Integer> getRepsChange(List<WorkoutExercise> we) {

        if (we.isEmpty()) {
            return new ArrayList<>();
        }

        List<WorkoutExercise> rewe = we.reversed();
        List<Integer> result = new ArrayList<>();

        int prev = rewe.getFirst().getReps();

        for (int i = 0; i < we.size(); i++) {
            int x = rewe.get(i).getReps();
            result.add(x - prev);
            prev = x;
        }

        return result.reversed();
    }

    public WorkoutExercise getOnlyPreviousWorkoutExercise(WorkoutExercise we) {
        Workout workout = we.getWorkout();
        List<WorkoutExercise> previousWE = workoutExerciseRepo.findPrevious(workout.getPerson().getId(),
                                                                            we.getExercise().getId(),
                                                                            workout.getDate());

        if (previousWE.isEmpty()) {
            return we;
        } else {
            return previousWE.getFirst();
        }
    }

    public List<WorkoutExercise> getPreviousWorkoutExercises(List<WorkoutExercise> workoutExercises) {
        List<WorkoutExercise> result = new ArrayList<>();

        for (WorkoutExercise we : workoutExercises) {
            result.add(getOnlyPreviousWorkoutExercise(we));
        }

        return result;
    }

    public List<Double> getListOfWeightChangeInPercent(List<WorkoutExercise> workoutExercises,
                                                       List<WorkoutExercise> previousworkoutExercises) {
        List<Double> result = new ArrayList<>();

        for(int i = 0; i < workoutExercises.size(); i++) {
            double a = workoutExercises.get(i).getWorkWeight();
            double b = previousworkoutExercises.get(i).getWorkWeight();

            double percent = (Math.round((a - b)/b*1000))/10.0;
            result.add(percent);
        }

        return result;
    }

    public List<Integer> getListOfRepsChange(List<WorkoutExercise> workoutExercises,
                                             List<WorkoutExercise> previousworkoutExercises){
        List<Integer> result = new ArrayList<>();

        for(int i = 0; i < workoutExercises.size(); i++) {
            int a = workoutExercises.get(i).getReps();
            int b = previousworkoutExercises.get(i).getReps();
            result.add(a - b);
        }

        return result;
    }


}
