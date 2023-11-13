package io.everyonecodes.springmodule;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
}
