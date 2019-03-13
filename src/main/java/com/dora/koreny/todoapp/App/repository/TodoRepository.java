package com.dora.koreny.todoapp.App.repository;

import com.dora.koreny.todoapp.App.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
