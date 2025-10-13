package GymBro.Model.utils;

import GymBro.Model.Weight;
import GymBro.Model.WorkoutExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Converter {

    public double lbToKg(double lbs) {
        return (Math.round((lbs * 0.45359237) * 10)) / 10.0;
    }

    public double kgToLb(double kg) {
        return (Math.round((kg * 2.20462262) * 10)) / 10.0;
    }

    public List<Double> lbsToKgs(List<Double> weightsInLbs, List<Boolean> isLbs) {
        List<Double> convertedWeights = new ArrayList<>();

        for (int i = 0; i < weightsInLbs.size(); i++) {
            if (isLbs.get(i)) {
                convertedWeights.add(lbToKg(weightsInLbs.get(i)));
            } else {
                convertedWeights.add(weightsInLbs.get(i));
            }
        }

        return convertedWeights;
    }

    public WorkoutExercise convertWE(WorkoutExercise we) {
        WorkoutExercise converted = new WorkoutExercise();

        converted.setId(we.getId());
        converted.setWorkout(we.getWorkout());
        converted.setExercise(we.getExercise());
        converted.setWorkWeight(kgToLb(we.getWorkWeight()));
        converted.setReps(we.getReps());

        return converted;
    }

    public List<WorkoutExercise> kgsToLbs(List<WorkoutExercise> workoutExercises) {
        List<WorkoutExercise> convertedWorkoutExercises = new ArrayList<>();

        for (WorkoutExercise we : workoutExercises) {
            convertedWorkoutExercises.add(convertWE(we));
        }

        return convertedWorkoutExercises;
    }

    public Page<WorkoutExercise> kgsToLbs(Page<WorkoutExercise> workoutExercisesPage) {
        List<WorkoutExercise> convertedContent = new ArrayList<>();

        for (WorkoutExercise we : workoutExercisesPage.getContent()) {
            convertedContent.add(convertWE(we));
        }

        return new PageImpl<>(convertedContent, workoutExercisesPage.getPageable(), workoutExercisesPage.getTotalElements());
    }

    public Weight convertWeight(Weight weight) {
        Weight converted = new Weight();

        converted.setId(weight.getId());
        converted.setPerson(weight.getPerson());
        converted.setPersonWeight(kgToLb(weight.getPersonWeight()));
        converted.setWeightDate(weight.getWeightDate());

        return converted;
    }

    public List<Weight> kgsToLbsWeights(List<Weight> weights) {
        List<Weight> convertedWeights = new ArrayList<>();

        for (Weight we : weights) {
            convertedWeights.add(convertWeight(we));
        }

        return convertedWeights;
    }

    public Page<Weight> kgsToLbsWeights(Page<Weight> weights) {
        List<Weight> convertedWeights = new ArrayList<>();

        for (Weight we : weights.getContent()) {
            convertedWeights.add(convertWeight(we));
        }

        return new PageImpl<>(convertedWeights, weights.getPageable(), weights.getTotalElements());
    }

    public static String cmToFt(int cm) {
        double totalInches = cm / 2.54;
        int feet = (int) (totalInches / 12);
        int inches = (int) Math.round(totalInches % 12);

        return feet + "'" + inches + "\"";
    }

}
