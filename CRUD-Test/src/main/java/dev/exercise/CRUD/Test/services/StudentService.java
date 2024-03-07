package dev.exercise.CRUD.Test.services;

import dev.exercise.CRUD.Test.entities.Student;
import dev.exercise.CRUD.Test.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public Student updateIsWorking(Long id, Boolean isWorking) {
        Optional<Student> user = studentRepository.findById(id);
        if(user.isPresent()) {
            user.get().setIsWorking(isWorking);
            return studentRepository.save(user.get());
        } else {
            return null;
        }
    }
    public Student saveStudent(Student student) {
        return studentRepository.saveAndFlush(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
