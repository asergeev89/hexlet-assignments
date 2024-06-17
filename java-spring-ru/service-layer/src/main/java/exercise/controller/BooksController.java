package exercise.controller;

import java.util.List;

import exercise.dto.*;
import exercise.service.AuthorService;
import exercise.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BookService bookService;

    // BEGIN
    @Autowired
    AuthorService authorService;
    @GetMapping("")
    public List<BookDTO> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable long id) {
        return bookService.getById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookDTO> create(@RequestBody @Valid BookCreateDTO createdDto) {
        return bookService.create(createdDto);
    }

    @PutMapping("/{id}")
    public BookDTO update(@PathVariable long id, @RequestBody BookUpdateDTO updateDTO) {
        return bookService.update(id, updateDTO);
    }

    @DeleteMapping("")
    public void delete(@RequestBody BookDTO dto){
        bookService.delete(dto);
    }
    // END
}
