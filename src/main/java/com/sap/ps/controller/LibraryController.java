package com.sap.ps.controller;

import com.sap.ps.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LibraryController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    LibraryController(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;

    }

    @RequestMapping("/books")
    public List<Book> getBooks(@RequestParam(value="title", defaultValue="")String title) {
        if (StringUtils.isEmpty(title)) {
            return this.bookRepository.findAll();
        } else {
            return this.bookRepository.findByTitle(title);
        }
    }

    @RequestMapping("/books/{id}")
    public Book findBookById(@PathVariable String id) {
        return this.bookRepository.findOne(Long.valueOf(id));
    }

    /***
     * update book, status = null && userId !== null, return book
     * status != null && userId != null, borrow book
     * */
    @RequestMapping(value="/books/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody BookPatch book) {
        if (book.getId() == null) {
            book.setId(Long.valueOf(id));
        }
        Book result = this.bookRepository.updateBook(book);
//        Book oldBook = this.bookRepository.findOne(Long.valueOf(id));
//        Book result = null;
//        if (!StringUtils.isEmpty(book.getStatus()) && !StringUtils.isEmpty(book.getUserId())) {
//        	User user = this.userRepository.findOne(Long.valueOf(book.getUserId()));
//        	oldBook.getUsers().add(user);
//        	oldBook.setStatus(book.getStatus());
//        	result = this.bookRepository.save(oldBook);
//        } else if (StringUtils.isEmpty(book.getStatus()) && !StringUtils.isEmpty(book.getUserId())) {
//        	//TODO:
//        }
        return new ResponseEntity<Book>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/books", method = RequestMethod.POST)
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        Book result = this.bookRepository.save(book);
        return new ResponseEntity<Book>(result, HttpStatus.OK);
    }

    @RequestMapping("/users")
    public List<User> findAllUsers(@RequestParam(value="name", defaultValue="")String name) {
        if (StringUtils.isEmpty(name)) {
            return this.userRepository.findAll();
        } else {
            return this.userRepository.findByName(name);
        }
    }

    @RequestMapping(value="/users", method = RequestMethod.POST)
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User result = this.userRepository.save(user);
        return new ResponseEntity<User>(result, HttpStatus.OK);
    }

//    @RequestMapping(value="/borrows", method = RequestMethod.POST)
//    public ResponseEntity<Borrow> borrowBook(@RequestBody Borrow borrow) {
//        Borrow result = this.borrowRepository.save(borrow);
//        return new ResponseEntity<Borrow>(result, HttpStatus.OK);
//    }

//    @RequestMapping(value="/borrows/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Borrow> returnBookById(@PathVariable String id) {
//        Borrow borrow = this.borrowRepository.findOne(Long.valueOf(id));
//        borrow.setStatus("RETURNED");
//        this.borrowRepository.save(borrow);
//        return new ResponseEntity<Borrow>(HttpStatus.NO_CONTENT);
//    }    

}
