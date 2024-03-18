package org.example.collaboapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.collaboapp.model.utils.BaseModel;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "meetings")
public class Meeting extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meetingId;

    @Column(nullable = false)
    private String title;

    private String description;


    @Column(name = "start_datetime")
    private Date startDatetime;

    @Column(name = "end_datetime")
    private Date endDatetime;

//    @ManyToOne
//    @JoinColumn(name = "project_id", insertable = false, updatable = false)
//    @NotNull
//    private Project project;
    @Column(name = "project_id", nullable = false)
    private int projectId;

}
