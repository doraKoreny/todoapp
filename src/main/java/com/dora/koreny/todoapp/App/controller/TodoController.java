package com.dora.koreny.todoapp.App.controller;

import com.dora.koreny.todoapp.App.model.Status;
import com.dora.koreny.todoapp.App.model.Todo;
import com.dora.koreny.todoapp.App.repository.TodoRepository;
import com.dora.koreny.todoapp.App.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class TodoController {

    private static final String SUCCESS = "{\"success\":true}";

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    // List all todos X
    @PostMapping(path = "list", headers = "Content-type=application/x-www-form-urlencoded")
    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    // Add new X
    @PostMapping(path = "addTodo", headers = "Content-type=application/x-www-form-urlencoded")
    public String addTodo(@RequestParam("todo-title") String title) {
        todoService.addTodo(title);
        return SUCCESS;
    }

    // Remove all completed
    @DeleteMapping(path = "todos/completed")
    public String removeAllCompleted() {
        List<Todo> todos = todoRepository.findAll();
        for (Todo todo: todos) {
            if (todo.getStatus() == Status.COMPLETE) {
                todoRepository.delete(todo);
            }
        }
        return SUCCESS;
    }


//     Toggle status by id DB-ben működik, de a sorrendet összecseréli, frontenden is, de a css nem változik
    @PutMapping(path = "todos/{id}/toggle_status", headers = "Content-type=application/x-www-form-urlencoded")
    public String toggleStatus(@PathVariable("id") long id, @RequestParam(value = "status", required = false) String status) {
        todoService.toggleById(id, status);
        return SUCCESS;
    }

    // Toggle all status, átállítja db-ben mmindet completedre de visszafelé nem működik, frontenden szintén nincs nyoma
    @PutMapping(path = "todos/toggle_all", headers = "Content-type=application/x-www-form-urlencoded")
    public String toggleAll(@RequestParam("toggle-all") String completed) {
        todoService.toggleAll(completed);
        return SUCCESS;
    }

    // Remove by id X
    @DeleteMapping(path = "todos/{id}")
    public void deleteTodo(@PathVariable("id") long id) {
        todoRepository.deleteById(id);
    }

    // Update by id X
    @PutMapping(path = "todos/{id}", headers = "Content-type=application/x-www-form-urlencoded")
    public String updateTodo(@PathVariable("id") long id, @RequestParam("todo-title") String title) {
        todoService.updateTodoById(id, title);
        return SUCCESS;

    }
}
