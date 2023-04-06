package com.mykhailotiutiun.repcounterbot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@ToString
@Document(collection = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    Long id;

    String username;

}
