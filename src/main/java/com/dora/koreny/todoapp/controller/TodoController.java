package com.dora.koreny.todoapp.controller;

import com.dora.koreny.todoapp.model.Todo;
import com.dora.koreny.todoapp.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoController {

    private static final String SUCCESS = "{\"success\":true}";

    @Autowired
    private TodoRepository todoRepository;

    @PostMapping(path = "/addTodo")
    public String addTodo(@RequestBody Todo todo) {
        todoRepository.save(todo);
        return SUCCESS;
    }
}
