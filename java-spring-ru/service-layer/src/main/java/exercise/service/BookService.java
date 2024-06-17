package exercise.service;

import exercise.dto.*;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.mapper.BookMapper;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookMapper mapper;
    public List<BookDTO> getAll() {
        var allBooks = bookRepository.findAll();
        return allBooks.stream().map(mapper::map).toList();
    }

    public BookDTO getById(long id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        var bookDto = mapper.map(book);
        try{
            book.getAuthor().getId();
//            var author = authorRepository.findById(mapper.map(book).getAuthorId());
            bookDto.setAuthorFirstName(book.getAuthor().getFirstName());
            bookDto.setAuthorLastName(book.getAuthor().getLastName());
            bookDto.setAuthorId(book.getAuthor().getId());
        } catch (Exception e) {

        }

        return bookDto;
    }

    public ResponseEntity<BookDTO> create(BookCreateDTO createDTO) {
        var newBook = mapper.map(createDTO);

        var author = authorRepository.findById(createDTO.getAuthorId());
        if(author.isEmpty()) {
            return ResponseEntity.status(400).body(mapper.map(newBook));
        }
        newBook.setAuthor(author.get());
        bookRepository.save(newBook);
        return ResponseEntity.status(201).body(mapper.map(newBook));
    }

    public BookDTO update(long id, BookUpdateDTO updateDTO) {
        var bookToUpdate = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        mapper.update(updateDTO, bookToUpdate);
        bookRepository.save(bookToUpdate);
        return mapper.map(bookToUpdate);
    }

    public void delete(BookDTO dto){
        var bookToDelete = bookRepository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        bookRepository.delete(bookToDelete);
    }
    // END
}
