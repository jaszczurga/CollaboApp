package org.example.collaboapp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@MappedSuperclass
public class BaseModel {

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

//    @Column(name = "updated_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    protected Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = new Date();
//    }
}
