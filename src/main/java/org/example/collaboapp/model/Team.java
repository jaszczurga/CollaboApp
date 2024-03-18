package org.example.collaboapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "team")
public class Team extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "manager_id")
    private int managerId;

}
