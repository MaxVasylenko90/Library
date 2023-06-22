package ua.vasylenko.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.vasylenko.library.dao.BookDAO;
import ua.vasylenko.library.dao.PersonDAO;
import ua.vasylenko.library.models.Book;
import ua.vasylenko.library.models.Person;
import ua.vasylenko.library.util.BookValidator;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final BookValidator bookValidator;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, BookValidator bookValidator, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.bookValidator = bookValidator;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String bookList(Model model) {
        model.addAttribute("bookList", bookDAO.getBooks());
        return "books/index";
    }

    @GetMapping("/new")
    public String addNewBook(@ModelAttribute("book") Book book) {
        return "books/newBook";
    }

    @PostMapping()
    public String newBook(@ModelAttribute("book")@Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/newBook";
        bookDAO.addNewBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id, @ModelAttribute("editedPerson") Person person, Model model) {
        model.addAttribute("book", bookDAO.getBook(id));
        Optional<Person> assignedPerson = bookDAO.getPerson(id);
        if (assignedPerson.isPresent())
            model.addAttribute("owner", assignedPerson.get());
        else model.addAttribute("people", personDAO.getPeople());
        return "books/bookPage";
    }

    @PatchMapping("/{id}/assign")
    public String bookAssign(@PathVariable("id") int bookId, @ModelAttribute("editedPerson") Person person) {
        bookDAO.assignPerson(bookId, person.getId());
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int bookId) {
        bookDAO.releaseBook(bookId);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int bookId, Model model) {
        model.addAttribute("book", bookDAO.getBook(bookId));
        return "/books/editBook";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int bookId, @ModelAttribute("book") Book book) {
        bookDAO.update(bookId, book);
        return "redirect:/books/{id}";
    }




}
