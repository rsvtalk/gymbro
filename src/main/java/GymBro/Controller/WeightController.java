package GymBro.Controller;

import GymBro.Model.Person;
import GymBro.Model.Weight;
import GymBro.Model.enums.HeightUnit;
import GymBro.Model.enums.WeightUnit;
import GymBro.Model.utils.Converter;
import GymBro.Security.PersonDetails;
import GymBro.Service.WeightService;
import GymBro.Service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/weight")
public class WeightController {

    WeightService weightService;
    WorkoutService workoutService;
    Converter converter;

    @Autowired
    public WeightController(WeightService weightService,
                            WorkoutService workoutService,
                            Converter converter) {
        this.weightService = weightService;
        this.workoutService = workoutService;
        this.converter = converter;
    }

    @GetMapping()
    public String showBodyInfo(@AuthenticationPrincipal PersonDetails user, Model model) {
        Person person = user.getPerson();
        int countOfWorkouts = workoutService.getCountOfWorkouts(person);
        List<Weight> weights = weightService.getAllWeights(person);

        if (person.getWeightUnit() == WeightUnit.LB) {
            weights = converter.kgsToLbsWeights(weights);
        }

        List<Double> weightDifference = weightService.getWeightDifference(weights);
        List<Integer> calorieLimits = weightService.getCalories(person);

        if (weights.isEmpty()) {
            return "redirect:/weight/add";
        }

        if (person.getHeightUnit() == HeightUnit.FT) {
            String heightInFeet = Converter.cmToFt(person.getHeight());
            model.addAttribute("heightInFeet", heightInFeet);
        }

        model.addAttribute("person", person);
        model.addAttribute("countOfWorkouts", countOfWorkouts);
        model.addAttribute("weights", weights);
        model.addAttribute("weightDifference", weightDifference);
        model.addAttribute("calorieLimits", calorieLimits);

        return "Weight/weightInfo";
    }

    @GetMapping("/all")
    public String showAll(@AuthenticationPrincipal PersonDetails personDetails,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size", defaultValue = "50") int size,
                          Model model) {

        Person user = personDetails.getPerson();

        Page<Weight> weights = weightService.getAllWeights(user, page, size);
        List<Weight> weightList = weightService.getAllWeights(user);

        if (user.getWeightUnit() == WeightUnit.LB) {
            weights = converter.kgsToLbsWeights(weights);
            weightList = converter.kgsToLbsWeights(weightList);
        }

        List<Double> weightDifference = weightService.getWeightDifference(weightList);

        model.addAttribute("weightUnit", user.getWeightUnit());
        model.addAttribute("weights", weights);
        model.addAttribute("weightDifference", weightDifference);

        return "Weight/weightHistory";
    }

    @GetMapping("/add")
    public String addWeight(@AuthenticationPrincipal PersonDetails personDetails,
                            Model model) {
        Person user = personDetails.getPerson();
        Boolean isFirst = weightService.checkPreviousWeight(user);

        model.addAttribute("weightUnit", user.getWeightUnit());
        model.addAttribute("weight", new Weight());
        model.addAttribute("isFirst", isFirst);

        return "Weight/addWeight";
    }

    @PostMapping("/add")
    public String addWeight(@AuthenticationPrincipal PersonDetails personDetails,
                            @ModelAttribute("weight") Weight weight,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "Weight/addWeight";
        }

        Person user = personDetails.getPerson();

        if (user.getWeightUnit() == WeightUnit.LB) {
            weight.setPersonWeight(converter.lbToKg(weight.getPersonWeight()));
        }

        weight.setPerson(user);
        weightService.addWeight(weight);

        return "redirect:/weight";
    }

}
