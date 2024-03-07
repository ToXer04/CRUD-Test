package dev.exercise.CRUD.Test;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.exercise.CRUD.Test.controllers.StudentController;
import dev.exercise.CRUD.Test.entities.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(value = "test")
class StudentTestApplicationTests {
    @Autowired
    private StudentController studentController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void controllerExistTest() {
        assertThat(studentController).isNotNull();
    }
    private Student createStudent() throws Exception {
        Student student = new Student();
        student.setName("Mario");
        student.setSurname("Rossi");
        student.setIsWorking(false);
        return createStudentb(student);
    }
    private Student createStudentb(Student student) throws Exception {
        MvcResult result = createStudentRequest(student);
        Student studentMap = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(studentMap.getId()).isNotNull();
        assertThat(studentMap).isNotNull();
        return studentMap;
    }
    private MvcResult createStudentRequest(Student student) throws Exception {
        if(student == null) return null;
        String studentJSON = objectMapper.writeValueAsString(student);
        return this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/student/create")
                        .content(studentJSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
    private Student getStudentFromID(Long id) throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/get/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        try{
            String studentJSON = result.getResponse().getContentAsString();
            Student student = objectMapper.readValue(studentJSON, Student.class);
            assertThat(student.getId()).isNotNull();
            assertThat(student).isNotNull();
            return student;
        } catch (Exception e) {
            return null;
        }
    }
    @Test
    void createStudentTest() throws Exception {
        Student result = createStudent();
    }
    @Test
    void readStudentListTest() throws Exception {
        createStudent();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/getall"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        List<Student> students = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        System.out.println("Students in list are: " + students.size());
        assertThat(students.size()).isNotZero();
    }
    @Test
    void readSingleStudentTest() throws Exception {
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();
        Student studentID = getStudentFromID(student.getId());
        assertThat(studentID.getId()).isEqualTo(student.getId());
    }
    @Test
    void updateIsWorkingTest() throws Exception {
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();
        Student updatedStudent = studentController.updateStatus(student.getId(), !student.getIsWorking());
        assertThat(!student.getIsWorking()).isEqualTo(updatedStudent.getIsWorking());
    }
    @Test
    void deleteSingleStudentTest() throws Exception {
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/delete/" + student.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Student studentID = getStudentFromID(student.getId());
        assertThat(studentID).isNull();
    }
}