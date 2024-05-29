package exercise;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> get() {
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(posts.size())).body(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Optional<Post>> getById(@PathVariable String id) {
        Optional<Post> foundPost = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        HttpStatus status;
        if (foundPost.isPresent()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(foundPost);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post newPost) {
        posts.add(newPost);
        return ResponseEntity.status(201).body(newPost);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Optional<Post>> update(@PathVariable String id, @RequestBody Post data) {
        Optional<Post> foundPost = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        int status = 204;
        if (foundPost.isPresent()) {
            status = 200;
            foundPost.get().setTitle(data.getTitle());
            foundPost.get().setId(data.getId());
            foundPost.get().setBody(data.getBody());
        }

        return ResponseEntity.status(status).body(foundPost);
    }
    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
