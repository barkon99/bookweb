package com.konew.loginandregistration.controller;

import com.konew.loginandregistration.dto.BookRate;
import com.konew.loginandregistration.entity.Book;
import com.konew.loginandregistration.entity.Rating;
import com.konew.loginandregistration.entity.User;
import com.konew.loginandregistration.repository.BookRepository;
import com.konew.loginandregistration.repository.RatingRepository;
import com.konew.loginandregistration.repository.UserRepository;
import com.konew.loginandregistration.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
@RequestMapping("/homePage")
public class HomeController {
    BookRepository bookRepository;
    UserRepository userRepository;
    RatingRepository ratingRepository;
    RateService rateService;

    @Autowired
    public HomeController(BookRepository bookRepository, UserRepository userRepository, RatingRepository ratingRepository, RateService rateService) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.ratingRepository = ratingRepository;
        this.rateService = rateService;
    }

    public void addingAttributeToModel(Principal principal, Model model)
    {
        model.addAttribute("name", principal.getName());
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        model.addAttribute("role", authorities);

        long currentUserId = getUserId();

        List<Book> books = bookRepository.findAll();


        List<Rating> addedRatings = ratingRepository.getUserRates(currentUserId);
        List<BookRate> bookRates = new ArrayList<>();

        for(Rating ratings: addedRatings)
        {
            BookRate bookRate = new BookRate();
            bookRate.setBookId(ratings.getBook().getId());
            bookRate.setUserID(ratings.getUser().getId());
            bookRate.setRate(ratings.getValue());
            bookRates.add(bookRate);
        }
        model.addAttribute("addedRates", bookRates);

        for (Book book:books) {
            double avg = rateService.getAverageRate(book);
            book.setAverageRate(avg);
        }

        model.addAttribute("books", books);

        model.addAttribute("rating", new Rating());
    }

    @GetMapping
    public String welcome(Principal principal, Model model)
    {
        addingAttributeToModel(principal, model);
        model.addAttribute("bookRate", new BookRate());

        return "welcome";
    }
    @PostMapping("/{bookID}")
    public String addingRate(@PathVariable("bookID") long bookId, @ModelAttribute("bookRate") BookRate bookRate)
    {
        long currentUserId = getUserId();

        User currentUser = userRepository.findById(currentUserId);
        Book book = bookRepository.getOne(bookId);

        rateService.saveOrUpdateRate(currentUser,bookRate,book);

        return "redirect:/homePage";
    }
    @GetMapping("/addingBook")
    public String bookForm(Model model)
    {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping("/addingBook")
    public String addingBook(@ModelAttribute("book") Book book)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        User user= userRepository.getUserByUserName(name);
        long id = user.getId();
        book.setUser(userRepository.getOne(id));

        bookRepository.save(book);
        return "book-form";
    }

    public long getUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
