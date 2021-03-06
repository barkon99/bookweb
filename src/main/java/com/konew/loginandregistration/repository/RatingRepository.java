package com.konew.loginandregistration.repository;

import com.konew.loginandregistration.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(value = "SELECT rate from rating where book_id=:book_id", nativeQuery = true)
    List<Integer>getRatings(@Param("book_id") long book_id);

    @Query(value = "select * from rating where appuser_id=:appuser_id and book_id=:book_id", nativeQuery = true)
    Optional<Rating> getUserRates(@Param("appuser_id") long appuser_id, @Param("book_id") long book_id);

    @Query(value = "select * from rating where appuser_id=:appuser_id", nativeQuery = true)
    List<Rating> getUserRates(@Param("appuser_id") long appuser_id);


}
