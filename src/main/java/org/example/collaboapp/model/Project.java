package org.example.collaboapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.collaboapp.model.utils.BaseModel;

import java.util.HashSet;
import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", updatable = false, nullable = false)
    private int projectId;

    @Column(nullable = false)
    private String title;

    private String description;


    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User manager;

    @ManyToMany
    @JoinTable(
            name = "user_projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users= new HashSet<>();

    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL)
    private Set<Meeting> meetings = new HashSet<>();

}