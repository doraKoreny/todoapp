package com.dora.koreny.todoapp.App.init;

import com.dora.koreny.todoapp.App.model.Status;
import com.dora.koreny.todoapp.App.model.Todo;
import com.dora.koreny.todoapp.App.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Initializer {

    @Autowired
    private TodoRepository todoRepository;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            Todo firstTodo = Todo.builder()
                    .title("first-todo")
                    .status(Status.ACTIVE)
                    .build();

            Todo secondTodo = Todo.builder()
                    .title("second-todo")
                    .status(Status.ACTIVE)
                    .build();

            todoRepository.save(firstTodo);
            todoRepository.save(secondTodo);
        };
    }
}
