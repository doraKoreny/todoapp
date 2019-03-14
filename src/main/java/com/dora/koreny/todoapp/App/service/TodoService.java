package com.dora.koreny.todoapp.App.service;

import com.dora.koreny.todoapp.App.model.Status;
import com.dora.koreny.todoapp.App.model.Todo;
import com.dora.koreny.todoapp.App.repository.TodoRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    public String getTodosByStatus(String status) {
        List<Todo> todos;
        if (status == null || status.isEmpty()) {
            todos = todoRepository.findAll();
        } else {
            todos = todoRepository.findAll()
                    .stream()
                    .filter(todo -> todo.getStatus().toString().equals(status.toUpperCase()))
                    .collect(Collectors.toList());
        }
        JSONArray array = new JSONArray();
        for (Todo todo : todos) {
            JSONObject jo = new JSONObject();
            jo.put("id", todo.getId());
            jo.put("title", todo.getTitle());
            jo.put("completed", todo.isComplete());
            array.put(jo);
        }
        return array.toString(2);
    }

    public void addTodo(String title) {
        Todo todo = Todo.builder()
                .title(title)
                .status(Status.ACTIVE)
                .build();
        todoRepository.saveAndFlush(todo);
    }


    public void removeAllCompleted() {
        List<Todo> todos = todoRepository.findAll();
        for (Todo todo: todos) {
            if (todo.getStatus() == Status.COMPLETE) {
                todoRepository.delete(todo);
            }
        }
    }

    public void toggleStatus(long id, boolean isComplete) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new IllegalStateException("Todo not found"));
        if (isComplete) {
            todo.setStatus(Status.COMPLETE);
        } else {
            todo.setStatus(Status.ACTIVE);
        }
        todoRepository.save(todo);
    }

    public void toggleAll(String completed) {
        todoRepository.findAll()
                .forEach(todo -> {todo.setStatus(completed.equals("true") ? Status.COMPLETE : Status.ACTIVE);
                    todoRepository.save(todo);
                });
    }

    public void deleteTodo(long id) {
        todoRepository.deleteById(id);
    }

    public void updateTodo(long id, String title) {
        todoRepository.findById(id)
                .map(todo -> {todo.setTitle(title);
                    return todoRepository.save(todo);
                });
    }
}

