package com.dora.koreny.todoapp.App.service;

import com.dora.koreny.todoapp.App.model.Status;
import com.dora.koreny.todoapp.App.model.Todo;
import com.dora.koreny.todoapp.App.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public void addTodo(String title) {
        Todo todo = Todo.builder()
                .title(title)
                .status(Status.ACTIVE)
                .build();
        todoRepository.saveAndFlush(todo);
    }

    public void toggleById(long id, String status) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new IllegalStateException("Todo not found"));
        if (!todo.isComplete() == status.equals("true")) {
            todo.setStatus(Status.COMPLETE);
            todoRepository.save(todo);
        } else {
            todo.setStatus(Status.ACTIVE);
            todoRepository.save(todo);
        }
    }

    public void toggleAll(String completed) {
        todoRepository.findAll()
                .forEach(todo -> {todo.setStatus(completed.equals("true") ? Status.COMPLETE : Status.ACTIVE);
                    todoRepository.save(todo);
                });
    }

    public void updateTodoById(long id, String title) {
        todoRepository.findById(id)
                .map(todo -> {todo.setTitle(title);
                    return todoRepository.save(todo);
                });
    }

}

