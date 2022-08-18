package mrw007.springframework.spring5mvcrest.api.v1.model;

import lombok.Data;

@Data
public class CustomerDTO {
    Long id;
    String firstName;
    String lastName;
}
