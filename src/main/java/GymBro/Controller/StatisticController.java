package GymBro.Controller;

import GymBro.Model.Exercise;
import GymBro.Model.Person;
import GymBro.Model.WorkoutExercise;
import GymBro.Model.enums.WeightUnit;
import GymBro.Model.utils.Converter;
import GymBro.Security.PersonDetails;
import GymBro.Service.ExerciseService;
import GymBro.Service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    ExerciseService exerciseService;
    StatisticService statisticService;
    Converter converter;

    @Autowired
    public StatisticController(ExerciseService exerciseService,
                               StatisticService statisticService,
                               Converter converter) {
        this.exerciseService = exerciseService;
        this.statisticService = statisticService;
        this.converter = converter;
    }

    @GetMapping
    public String showAllStatistic(@AuthenticationPrincipal PersonDetails personDetails,
                                   Model model) {

        model.addAttribute("exercises",
                exerciseService.getUsedExercises(personDetails.getPerson().getId()));

        return "Statistics/usedExercises";
    }

    @GetMapping("/{id}")
    public String showStatistic(@AuthenticationPrincipal PersonDetails personDetails,
                                @PathVariable int id,
                                @RequestParam(defaultValue = "0", name = "page") int page,
                                @RequestParam(defaultValue = "10", name = "size") int size,
                                Model model) {

        Person user = personDetails.getPerson();
        Exercise exercise = exerciseService.getExercise(id, user.getId());

        List<WorkoutExercise> exerciseHistory = exerciseService.getExerciseHistory(user.getId(), id);
        List<Double> weightChanges = statisticService.getWeightChangeInPercent(exerciseHistory);
        List<Integer> repsChanges = statisticService.getRepsChange(exerciseHistory);

        Page<WorkoutExercise> pageExerciseHistory =
                exerciseService.getExerciseHistory(user.getId(),id,page,size);

        if (user.getWeightUnit() == WeightUnit.LB) {
            pageExerciseHistory = converter.kgsToLbs(pageExerciseHistory);
        }

        model.addAttribute("weightUnit", user.getWeightUnit());
        model.addAttribute("exercise", exercise);
        model.addAttribute("exerciseHistory", pageExerciseHistory);
        model.addAttribute("weightChanges", weightChanges);
        model.addAttribute("repsChanges", repsChanges);

        return "Statistics/exerciseStat";
    }

}
