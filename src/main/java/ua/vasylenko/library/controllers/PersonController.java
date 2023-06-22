package ua.vasylenko.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.vasylenko.library.dao.BookDAO;
import ua.vasylenko.library.dao.PersonDAO;
import ua.vasylenko.library.models.Person;
import ua.vasylenko.library.util.PersonValidator;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonDAO personDAO, BookDAO bookDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("peopleList", personDAO.getPeople());
        return "people/index";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("person") Person person) {
        return "people/newUser";
    }

    @PostMapping()
    public String createNewUser(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/newUser";
        personDAO.create(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String userProfile(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.getPerson(id));
        model.addAttribute("books", bookDAO.getPersonBookList(id));
        return "people/userPage";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") int userId, Model model) {
        model.addAttribute("user", personDAO.getPerson(userId));
        return "people/editUser";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user")Person person, @PathVariable("id") int userId) {
        personDAO.update(userId, person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int userId) {
        personDAO.delete(userId);
        return "redirect:/people";
    }
}