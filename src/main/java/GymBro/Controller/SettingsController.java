package GymBro.Controller;

import GymBro.Model.Person;
import GymBro.Security.PersonDetails;
import GymBro.Service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    PersonService personService;

    @Autowired
    public SettingsController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String showSettings(@AuthenticationPrincipal PersonDetails personDetails,
                               Model model) {
        model.addAttribute("person", personDetails.getPerson());
        return "Settings/settings";
    }

    @PatchMapping("/edit")
    public String editSettings(@ModelAttribute("person") Person person,
                               @AuthenticationPrincipal PersonDetails user) {
        personService.update(person, user.getPerson());
        return "redirect:/settings";
    }

}
