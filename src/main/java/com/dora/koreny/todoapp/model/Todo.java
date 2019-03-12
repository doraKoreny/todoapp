package com.dora.koreny.todoapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Todo {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private Status status;

    public boolean isComplete() {
        return this.status == Status.COMPLETE;
    }

}
