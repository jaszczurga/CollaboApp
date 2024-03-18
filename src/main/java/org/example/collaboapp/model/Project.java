package org.example.collaboapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", updatable = false, nullable = false)
    private int projectId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "manager_id")
    private int managerId;

    @ManyToMany
    @JoinTable(
            name = "user_projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

}