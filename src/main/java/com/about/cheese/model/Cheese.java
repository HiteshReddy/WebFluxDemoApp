package com.about.cheese.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document
public class Cheese {

    @Id
    private String id;

    private String name;

    private String origin;

    private String typeOfMilk;

    private int aging;

    private String description;

}
