package GymBro.Controller;

import GymBro.Model.enums.WeightUnit;
import GymBro.Model.utils.Converter;
import GymBro.Model.Person;
import GymBro.Model.Workout;
import GymBro.Model.WorkoutExercise;
import GymBro.Security.PersonDetails;
import GymBro.Service.ExerciseService;
import GymBro.Service.StatisticService;
import GymBro.Service.WorkoutService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/workout")
public class WorkoutController {

    WorkoutService workoutService;
    ExerciseService exerciseService;
    StatisticService statisticService;
    Converter converter;

    @Autowired
    public WorkoutController(WorkoutService workoutService,
                             ExerciseService exerciseService,
                             StatisticService statisticService,
                             Converter converter) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
        this.statisticService = statisticService;
        this.converter = converter;
    }

    @GetMapping
    public String showAllWorkouts(@AuthenticationPrincipal PersonDetails personDetails,
                                  @RequestParam(defaultValue = "0", name = "page") int page,
                                  @RequestParam(defaultValue = "7", name = "size") int size,
                                  Model model) {

        model.addAttribute("workouts",
                workoutService.getPersonWorkouts(personDetails.getPerson(), page, size));

        return "Workout/allWorkouts";
    }

    @GetMapping("/{id}")
    public String showWorkoutById(@AuthenticationPrincipal PersonDetails personDetails,
                                  @PathVariable int id,
                                  Model model) {

        Person user = personDetails.getPerson();

        Workout workout = workoutService.findById(id, user.getId());
        List<WorkoutExercise> workoutExercises = workout.getWorkoutExercises();
        List<WorkoutExercise> previousWorkoutExercises = statisticService.getPreviousWorkoutExercises(workoutExercises);

        List<Double> weightsChange = statisticService.getListOfWeightChangeInPercent(workoutExercises, previousWorkoutExercises);
        List<Integer> repsChange = statisticService.getListOfRepsChange(workoutExercises, previousWorkoutExercises);

        if (user.getWeightUnit() == WeightUnit.LB) {
            workoutExercises = converter.kgsToLbs(workoutExercises);
        }

        model.addAttribute("weightUnit", user.getWeightUnit());
        model.addAttribute("workout", workout);
        model.addAttribute("workoutExercises", workoutExercises);
        model.addAttribute("weightsChange", weightsChange);
        model.addAttribute("repsChange", repsChange);

        return "Workout/workout";
    }

    @GetMapping("/add")
    public String showAddWorkout(@AuthenticationPrincipal PersonDetails personDetails,
                                 Model model) {
        Person user = personDetails.getPerson();


        model.addAttribute("workout", new Workout());
        model.addAttribute("systemExercises", exerciseService.getSystemExercises());
        model.addAttribute("customExercises", exerciseService.getExercisesByCreator(user));

        return "Workout/addWorkout";
    }

    @PostMapping("/add")
    public String addWorkout(@AuthenticationPrincipal PersonDetails personDetails,
                             @ModelAttribute("workout") Workout workout,
                             @RequestParam("exerciseIds") List<Integer> exerciseIds,
                             @RequestParam("weights") List<Double> weights,
                             @RequestParam("reps") List<Integer> reps,
                             @RequestParam("isLbs") List<Boolean> isLbs,
                             BindingResult bindingResult,
                             Model model) {

        Person user = personDetails.getPerson();
        List<Double> convertedWeights = converter.lbsToKgs(weights,isLbs);

        try {
            workoutService.addWorkoutInParts(user, workout, exerciseIds, convertedWeights, reps);
            return "redirect:/workout";
        } catch (ValidationException e) {
            bindingResult.reject("workout.error", e.getMessage());
            model.addAttribute("systemExercises", exerciseService.getSystemExercises());
            model.addAttribute("customExercises", exerciseService.getExercisesByCreator(user));
            return "Workout/addWorkout";
        }
    }

    @DeleteMapping("{id}")
    public String deleteWorkout(@AuthenticationPrincipal PersonDetails personDetails,
                                @PathVariable int id) {
        workoutService.deleteWorkout(id, personDetails.getPerson().getId());
        return "redirect:/workout";
    }

}
