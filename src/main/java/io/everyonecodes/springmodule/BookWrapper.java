package io.everyonecodes.springmodule;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookWrapper {
    private Book book;
    private double averageScore;
}
