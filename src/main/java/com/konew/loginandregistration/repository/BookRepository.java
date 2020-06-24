package com.konew.loginandregistration.repository;

import com.konew.loginandregistration.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
