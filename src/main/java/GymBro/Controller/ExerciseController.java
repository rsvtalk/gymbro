package GymBro.Controller;

import GymBro.Model.Exercise;
import GymBro.Model.Person;
import GymBro.Security.PersonDetails;
import GymBro.Service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public String showAllExercises(@AuthenticationPrincipal PersonDetails personDetails,
                                  Model model) {
        model.addAttribute("customExercises", exerciseService.getExercisesByCreator(personDetails.getPerson()));
        model.addAttribute("systemExercises", exerciseService.getSystemExercises());
        return "Exercise/allExercises";
    }

    @GetMapping("/{id}")
    public String showExerciseById(@AuthenticationPrincipal PersonDetails personDetails,
                       @PathVariable int id,
                       Model model) {
        Person user = personDetails.getPerson();

        model.addAttribute("exercise", exerciseService.getExercise(id, user.getId()));
        model.addAttribute("user", user);

        return "Exercise/exercise";
    }

    @GetMapping("/add")
    public String showAddExercise(Model model) {
        model.addAttribute("exercise", new Exercise());
        return "Exercise/addExercise";
    }

    @PostMapping("/add")
    public String addExercise(@AuthenticationPrincipal PersonDetails personDetails,
                              @Valid @ModelAttribute Exercise exercise,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "Exercise/addExercise";
        }

        exercise.setCreatedBy(personDetails.getPerson());
        exerciseService.addExercise(exercise);

        return "redirect:/exercise";
    }

    @GetMapping("/{id}/edit")
    public String showEditExercise(@AuthenticationPrincipal PersonDetails personDetails,
                                   @PathVariable int id,
                                   Model model) {
        model.addAttribute("exercise", exerciseService.getExercise(id, personDetails.getPerson().getId()));
        return "Exercise/editExercise";
    }

    @PatchMapping("/{id}/edit")
    public String editExercise(@AuthenticationPrincipal PersonDetails personDetails,
                               @Valid @ModelAttribute("exercise") Exercise exercise,
                               BindingResult bindingResult,
                               @PathVariable int id) {

        if (bindingResult.hasErrors()) {
            return "Exercise/editExercise";
        }

        exerciseService.updateExercise(exercise, id, personDetails.getPerson().getId());

        return "redirect:/exercise";
    }

    @DeleteMapping("{id}")
    public String deleteExercise(@AuthenticationPrincipal PersonDetails personDetails,
                                 @PathVariable int id) {
        exerciseService.deleteExercise(id, personDetails.getPerson().getId());
        return "redirect:/exercise";
    }

}
