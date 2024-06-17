package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    AuthorMapper mapper;
    public List<AuthorDTO> getAll() {
        var allAuthors = authorRepository.findAll();
        return allAuthors.stream().map(mapper::map).toList();
    }

    public AuthorDTO getById(long id) {
        var author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        return mapper.map(author);
    }

    public AuthorDTO create(AuthorCreateDTO createDTO) {
        var newAuthor = mapper.map(createDTO);
        authorRepository.save(newAuthor);
        return mapper.map(newAuthor);
    }

    public AuthorDTO update(long id, AuthorUpdateDTO updateDTO) {
        var authorToUpdate = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        mapper.update(updateDTO, authorToUpdate);
        authorRepository.save(authorToUpdate);
        return mapper.map(authorToUpdate);
    }

    public void delete(AuthorDTO dto){
        var authorToDelete = authorRepository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        authorRepository.delete(authorToDelete);
    }
    // END
}
