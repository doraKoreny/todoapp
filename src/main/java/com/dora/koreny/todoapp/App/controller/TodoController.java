package com.dora.koreny.todoapp.App.controller;

import com.dora.koreny.todoapp.App.model.Status;
import com.dora.koreny.todoapp.App.model.Todo;
import com.dora.koreny.todoapp.App.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class TodoController {

    private static final String SUCCESS = "{\"success\":true}";

    @Autowired
    private TodoRepository todoRepository;

    // List all todos X
    @PostMapping(path = "list")
    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    // Add new X
    @PostMapping(path = "addTodo", headers = "Content-type=application/x-www-form-urlencoded")
    public String addTodo(@RequestParam("todo-title") String title) {
        Todo todo = Todo.builder()
                .title(title)
                .status(Status.ACTIVE)
                .build();
        todoRepository.saveAndFlush(todo);
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

    // Toggle status by id DB-ben működik, de a sorrendet összecseréli, frontenden is, de a css nem változik
    @PutMapping(path = "todos/{id}/toggle_status", headers = "Content-type=application/x-www-form-urlencoded")
    public void toggleStatus(@PathVariable("id") long id, @RequestParam("status") String status) {
        List<Todo> todoList = todoRepository.findAll();
        for (Todo todo : todoList) {
            if (todo.getId() == id) {
                if (!todo.isComplete() == status.equals("true")) {
                    todo.setStatus(Status.COMPLETE);
                    todoRepository.save(todo);
                } else {
                    todo.setStatus(Status.ACTIVE);
                    todoRepository.save(todo);
                }
            }
        }
//        todoRepository.findAll()
//                .forEach(todo -> {todo.setStatus(todo.getId() == id && !todo.isComplete() == status.equals("true")
//                ? Status.COMPLETE : Status.ACTIVE);
//                todoRepository.save(todo);
//                });
    }

    // Toggle all status, átállítja db-ben mmindet completedre de visszafelé nem működik, frontenden szintén nincs nyoma
    @PutMapping(path = "todos/toggle_all", headers = "Content-type=application/x-www-form-urlencoded")
    public void toggleAll(@RequestParam("toggle-all") String completed) {
        todoRepository.findAll()
                .forEach(todo -> {todo.setStatus(completed.equals("true") ? Status.COMPLETE : Status.ACTIVE);
                todoRepository.save(todo);
                });

//        List<Todo> todos = todoRepository.findAll();
//        for (Todo todo: todos) {
//            todo.setStatus(completed.equals("true") ? Status.COMPLETE : Status.ACTIVE);
//            todoRepository.save(todo);
//        }
    }

    // Remove by id X
    @DeleteMapping(path = "todos/{id}")
    public void deleteTodo(@PathVariable("id") long id) {
        todoRepository.deleteById(id);
    }

    // Update by id X
    @PutMapping(path = "todos/{id}", headers = "Content-type=application/x-www-form-urlencoded")
    public void updateTodo(@PathVariable("id") long id, @RequestParam("todo-title") String title) {
        todoRepository.findById(id)
                .map(todo -> {todo.setTitle(title);
                return todoRepository.save(todo);
                });

//        List<Todo> todoList = todoRepository.findAll();
//        for (Todo todo: todoList) {
//            if (todo.getId() == id) {
//                todo.setTitle(title);
//                todoRepository.saveAndFlush(todo);
//            }
//        }
    }
}
