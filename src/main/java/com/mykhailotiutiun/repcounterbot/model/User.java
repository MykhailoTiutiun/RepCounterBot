package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String localTag = "en-US";

}
