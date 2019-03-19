package com.dora.koreny.todoapp.App.controller;

import com.dora.koreny.todoapp.App.model.Todo;
import com.dora.koreny.todoapp.App.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class TodoController {

    private static final String SUCCESS = "{\"success\":true}";

    @Autowired
    private TodoService todoService;

    //List all todos X
    @GetMapping("/getAll")
    public List<Todo> getTodos() {
        return todoService.getTodos();
    }

    // List todos by status
    @PostMapping(path = "/list", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getTodosByStatus(@RequestParam("status") String status) {
        return todoService.getTodosByStatus(status);
    }

    // Add new X
    @PostMapping(path = "/addTodo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addTodo(@RequestParam("todo-title") String title) {
        todoService.addTodo(title);
        return SUCCESS;
    }

    // Remove all completed
    @DeleteMapping(path = "/todos/completed")
    public String removeAllCompleted() {
        todoService.removeAllCompleted();
        return SUCCESS;
    }

    // Toggle status
    @PutMapping(path = "/todos/{id}/toggle_status", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String toggleStatus(@PathVariable("id") long id, @RequestParam(value = "status", required = false) String status) {
        todoService.toggleStatus(id, status);
        return SUCCESS;
    }

    // Toggle all todos
    @PutMapping(path = "/todos/toggle_all", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String toggleAll(@RequestParam("toggle-all") String completed) {
        todoService.toggleAll(completed);
        return SUCCESS;
    }

    // Remove by id X
    @DeleteMapping(path = "/todos/{id}")
    public void deleteTodo(@PathVariable("id") long id) {
        todoService.deleteTodo(id);
    }

    // Update by id X
    @PutMapping(path = "/todos/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String updateTodo(@PathVariable("id") long id, @RequestParam("todo-title") String title) {
        todoService.updateTodo(id, title);
        return SUCCESS;

    }
}
