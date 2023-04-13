package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTests {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() throws IOException {
        studentRepository.deleteAll();
    }

    private void sendStudentRequest(String endpoint, HttpMethod method, Student student) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", String.valueOf(student.getId()));
        map.add("firstName", student.getFirstName());
        map.add("lastName", student.getLastName());
        map.add("email", student.getEmail());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + endpoint, method, request, String.class);
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.is(HttpStatus.FOUND));
    }

    @Test
    public void testGetAllStudents() {
        Student student1 = new Student(1, "Bob", "Smith", "bob.smith@example.com");
        Student student2 = new Student(2, "John", "Jones", "john.jones@example.com");

        sendStudentRequest("/students", HttpMethod.POST, student1);
        sendStudentRequest("/students", HttpMethod.POST, student2);
        List<Student> students = studentRepository.findAll();

        MatcherAssert.assertThat(students.size(), Matchers.is(2));
        MatcherAssert.assertThat(students.get(0).getFirstName(), Matchers.is(student1.getFirstName()));
        MatcherAssert.assertThat(students.get(0).getLastName(), Matchers.is(student1.getLastName()));
        MatcherAssert.assertThat(students.get(0).getEmail(), Matchers.is(student1.getEmail()));

        MatcherAssert.assertThat(students.get(1).getFirstName(), Matchers.is(student2.getFirstName()));
        MatcherAssert.assertThat(students.get(1).getLastName(), Matchers.is(student2.getLastName()));
        MatcherAssert.assertThat(students.get(1).getEmail(), Matchers.is(student2.getEmail()));
    }

    @Test
    public void testSaveStudent() {
        Student student = new Student(6, "Jill", "Dio", "jill.dio@example.com");

        sendStudentRequest("/students", HttpMethod.POST, student);
        Optional<Student> studentRepositoryById = studentRepository.findById(student.getId());

        MatcherAssert.assertThat(studentRepositoryById.get().getFirstName(), Matchers.is(student.getFirstName()));
        MatcherAssert.assertThat(studentRepositoryById.get().getLastName(), Matchers.is(student.getLastName()));
        MatcherAssert.assertThat(studentRepositoryById.get().getEmail(), Matchers.is(student.getEmail()));
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student(9, "Gulia", "Romans", "gulia.romans@example.com");
        sendStudentRequest("/students", HttpMethod.POST, student);

        student.setFirstName("Jane");
        student.setEmail("jane.romans@example.com");

        sendStudentRequest("/students/" + student.getId(), HttpMethod.POST, student);

        Optional<Student> studentRepositoryById = studentRepository.findById(student.getId());
        MatcherAssert.assertThat(studentRepositoryById.get().getFirstName(), Matchers.is(student.getFirstName()));
        MatcherAssert.assertThat(studentRepositoryById.get().getLastName(), Matchers.is(student.getLastName()));
        MatcherAssert.assertThat(studentRepositoryById.get().getEmail(), Matchers.is(student.getEmail()));
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student(33, "Robert", "Mono", "robert.mono@example.com");
        sendStudentRequest("/students", HttpMethod.POST, student);

        restTemplate.getForEntity(baseUrl + "/students/" + student.getId(), String.class);

        Student deletedStudent = studentRepository.findById(student.getId()).orElse(null);
        MatcherAssert.assertThat(deletedStudent, Matchers.nullValue());
    }
}