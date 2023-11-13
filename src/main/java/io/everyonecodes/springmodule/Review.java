package io.everyonecodes.springmodule;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pk_review")
    @SequenceGenerator(name = "pk_review", sequenceName = "entity_id_review", allocationSize = 1)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int score;

    private LocalDateTime publishedDate;
}
