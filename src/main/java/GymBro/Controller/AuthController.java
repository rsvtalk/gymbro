package GymBro.Controller;

import GymBro.Model.Person;
import GymBro.Service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PersonService personService;

    @Autowired
    public AuthController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/login")
    public String login() {
        return "Auth/login";
    }

    @GetMapping("/register")
    public String showRegForm(Model model) {
        model.addAttribute("person", new Person());
        return "Auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("person") Person person,
                           BindingResult br,
                           Model model) {

        if (br.hasErrors()) {
            return "Auth/register";
        }

        if (personService.usernameExists(person.getUsername())) {
            model.addAttribute("error", "Такой email уже зарегистрирован :(");
            return "Auth/register";
        }

        personService.register(person);

        return "redirect:/auth/login";

    }



}
