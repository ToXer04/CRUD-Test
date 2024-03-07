package dev.exercise.CRUD.Test.controllers;

import dev.exercise.CRUD.Test.entities.Student;
import dev.exercise.CRUD.Test.repositories.StudentRepository;
import dev.exercise.CRUD.Test.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping("/create")
    public Student create(
            @RequestBody Student student
    ) {
        return studentService.saveStudent(student);
    }

    @GetMapping("/getall")
    public List<Student> getAll() {
        return studentService.findAll();
    }

    @GetMapping("/get/{id}")
    public Student get(
            @PathVariable Long id
    ) {
        Optional<Student> user = studentService.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    @PatchMapping("/updateId/{id}")
    public Student updateId(
            @PathVariable Long id,
            @RequestBody Student student
    ) {
        student.setId(id);
        return studentService.saveStudent(student);
    }

    @PatchMapping("/updateStatus/{id}")
    public Student updateStatus(
            @PathVariable Long id,
            @RequestParam Boolean isWorking
    ) {
        return studentService.updateIsWorking(id, isWorking);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        studentService.deleteById(id);
    }
}
