package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("")
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post getById(@PathVariable long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post newPost) {
        return postRepository.save(newPost);
    }

    @PutMapping("/{id}")
    public Post create(@PathVariable long id, @RequestBody Post newPost) {
        newPost.setId(id);
        return postRepository.save(newPost);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        Post postToDelete = postRepository.findById(id).get();
        postRepository.delete(postToDelete);
        commentRepository.deleteByPostId(postToDelete.getId());
    }
}
// END
