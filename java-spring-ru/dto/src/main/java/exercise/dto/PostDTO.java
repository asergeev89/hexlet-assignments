package exercise.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Setter
@Getter
public class PostDTO {
    private long id;
    private String title;
    public String body;
    public List<CommentDTO> comments;

}
// END
