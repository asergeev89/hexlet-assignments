package exercise.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

// BEGIN
@Setter
@Getter
public class ProductUpdateDTO {
    private long id;

    private String title;

    private int price;
}
// END
