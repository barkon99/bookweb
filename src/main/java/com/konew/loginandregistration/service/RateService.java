package com.konew.loginandregistration.service;

import com.konew.loginandregistration.dto.BookRate;
import com.konew.loginandregistration.entity.Book;
import com.konew.loginandregistration.entity.Rating;
import com.konew.loginandregistration.entity.User;
import com.konew.loginandregistration.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
public class RateService {
    RatingRepository ratingRepository;

    @Autowired
    public RateService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public double getAverageRate(Book book)
    {
        List<Integer> rates =  ratingRepository.getRatings(book.getId());
        if(rates.size()==0)
        {
            return 0;
        }
        OptionalDouble average = rates.stream().mapToDouble(a -> a).average();
        return Math.round(average.getAsDouble()*10)/10.0;
    }
    public Rating getUserRates(Long userId, Long bookId)
    {
        return ratingRepository.getUserRates(userId,bookId).orElseThrow(() -> new RuntimeException("No ratings for this user"));

    }

    public void saveOrUpdateRate(User currentUser, BookRate bookRate, Book book)
    {
        Rating rating =  new Rating();
        rating.setValue(bookRate.getRate());
        rating.setUser(currentUser);
        rating.setBook(book);

        if(checkRates(currentUser.getId(),book.getId()) == false) {

            ratingRepository.save(rating);
        }

        else {
            Rating updateRating = getUserRates(currentUser.getId(), book.getId());
            updateRating.setValue(bookRate.getRate());
            ratingRepository.save(updateRating);
        }

    }

    public boolean checkRates(long userId, long bookId)
    {
        return ratingRepository.getUserRates(userId,bookId).isPresent();
    }
}
