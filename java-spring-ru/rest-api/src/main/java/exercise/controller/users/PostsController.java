package exercise.controller.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {
    private List<Post> allPosts = Data.getPosts();
    @GetMapping("/users/{id}/posts")
    public List<Post> getById(@PathVariable String id) {
       return allPosts.stream().filter(p -> p.getUserId() == Integer.parseInt(id)).toList();
    }

    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@PathVariable String id, @RequestBody Post data) {
        Post newPost = new Post();
        newPost.setBody(data.getBody());
        newPost.setTitle(data.getTitle());
        newPost.setSlug(data.getSlug());
        newPost.setUserId(Integer.parseInt(id));
        allPosts.add(newPost);

        return newPost;
    }

}
// END
