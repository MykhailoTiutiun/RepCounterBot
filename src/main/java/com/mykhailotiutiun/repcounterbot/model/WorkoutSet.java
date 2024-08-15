package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSet {

    private Long id;
    private Integer number;
    private Integer reps;
    private Integer weight;

    public String print(String pattern) {
        return String.format(pattern, number, weight, reps);
    }
}
