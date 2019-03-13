package com.dora.koreny.todoapp.App.repository;

import com.dora.koreny.todoapp.App.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {
//    @Query("update Todo  todo set todo.title = :title where todo.id = :id")
//    @Modifying(clearAutomatically = true)
//    void updateTodoById(@Param("title") String title, @Param("id") Long id);
//
//    @Query("update Todo  todo set todo.status = :status where todo.id = :id")
//    @Modifying(clearAutomatically = true)
//    void updateStatus(String status, long id);
}
