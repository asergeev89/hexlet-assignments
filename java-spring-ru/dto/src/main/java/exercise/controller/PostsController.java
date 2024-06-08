package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("")
    public List<PostDTO> getAll() {
        var allPosts = postRepository.findAll();
        List<PostDTO> posts = allPosts.stream().map(this::postToDto).toList();

        for (PostDTO post : posts) {
            var allComments = commentRepository.findByPostId(post.getId());
            post.setComments(allComments.stream().map(this::commentToDto).toList());

        }
        return posts;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<PostDTO>> get(@PathVariable long id) {
        var post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        PostDTO postDto = postToDto(post);
        var comment = commentRepository.findByPostId(post.getId());
        List<CommentDTO> commentList = comment.stream().map(this::commentToDto).toList();
        postDto.setComments(commentList);
        return ResponseEntity.status(200).body(Optional.of(postDto));


    }

    private PostDTO postToDto(Post post) {
        var postDto = new PostDTO();
        postDto.setId(post.getId());
        postDto.setBody(post.getBody());
        postDto.setTitle(post.getTitle());
        return postDto;
    }

    private CommentDTO commentToDto(Comment comment) {
        var commentDto = new CommentDTO();
        commentDto.setBody(comment.getBody());
        commentDto.setId(comment.getId());
        return commentDto;
    }
}
// END
