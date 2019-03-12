package com.dora.koreny.todoapp;

import com.dora.koreny.todoapp.model.Status;
import com.dora.koreny.todoapp.model.Todo;
import com.dora.koreny.todoapp.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class Init {

    @Autowired
    private TodoRepository todoRepository;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            Todo firstTodo = Todo.builder()
                    .title("first-todo")
                    .status(Status.ACTIVE)
                    .build();

            todoRepository.save(firstTodo);
        };
    }
}
