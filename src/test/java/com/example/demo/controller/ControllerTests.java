package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTests {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    public void afterEach() throws IOException {
        studentRepository.deleteAll();
    }

    @Test
    public void testGetAllStudents() {
        // Create some students to populate the database
        Student student1 = new Student(2, "Bob", "Smith", "bob.smith@example.com");
        Student student2 = new Student(44, "John", "Jones", "john.jones@example.com");
        studentRepository.save(student1);
        studentRepository.save(student2);

        // Send a GET request to the /students endpoint
        final ResponseEntity<String> response = restTemplate.getForEntity("/students", String.class);

        // Assert that the response is an HTML document
        final String contentType = String.valueOf(response.getHeaders().getContentType());
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(contentType, containsString("text/html"));

        // Render the Thymeleaf view and extract the student names from the HTML
        Document doc = Jsoup.parse(Objects.requireNonNull(response.getBody()));
        Element table = doc.select("table").first();
        Elements rows = Objects.requireNonNull(table).select("tr");

        // Verify the contents of the view
        Element row1 = rows.get(1);
        MatcherAssert.assertThat(row1.child(0).text(), Matchers.containsString("Bob"));
        MatcherAssert.assertThat(row1.child(1).text(), Matchers.containsString("Smith"));
        MatcherAssert.assertThat(row1.child(2).text(), Matchers.containsString("bob.smith@example.com"));

        Element row2 = rows.get(2);
        MatcherAssert.assertThat(row2.child(0).text(), Matchers.containsString("John"));
        MatcherAssert.assertThat(row2.child(1).text(), Matchers.containsString("Jones"));
        MatcherAssert.assertThat(row2.child(2).text(), Matchers.containsString("john.jones@example.com"));
    }

    @Test
    public void testCreateStudent() {
        // Create a new student
        Student student = new Student(22, "Bob", "Smith", "bob.smith@example.com");

        // Send a POST request to the /students endpoint to create the student
        ResponseEntity<String> response = restTemplate.postForEntity("/students", student, String.class);

        // Assert that the response status code is 302 (redirect)
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.is(HttpStatus.FOUND));

        // Verify that the student was saved to the database
        Optional<Student> byId = studentRepository.findById(22);
        MatcherAssert.assertThat(byId.get().getFirstName(), Matchers.is("Bob"));
        MatcherAssert.assertThat(byId.get().getLastName(), Matchers.is("Smith"));
        MatcherAssert.assertThat(byId.get().getEmail(), Matchers.is("bob.smith@example.com"));
    }

    @Test
    public void testUpdateStudent() {
        // Create a new student
        Student student = new Student(4, "Bob", "Smith", "bob.smith@example.com");
        studentRepository.save(student);

        // Update the student's information
        student.setFirstName("Bobby");
        student.setLastName("Smith1");
        student.setEmail("bobby.smith1@example.com");

        // Send a POST request to the /students/{id} endpoint to update the student
        restTemplate.postForObject("/students/" + student.getId(), student, String.class);

        // Verify that the student's information was updated in the database
        Optional<Student> updatedStudent = studentRepository.findById(student.getId());
        MatcherAssert.assertThat(updatedStudent.get().getFirstName(), Matchers.is("Bobby"));
        MatcherAssert.assertThat(updatedStudent.get().getLastName(), Matchers.is("Smith1"));
        MatcherAssert.assertThat(updatedStudent.get().getEmail(), Matchers.is("bobby.smith1@example.com"));
    }

    @Test
    public void testDeleteStudent() {
        // Create a new student
        Student student = new Student(7, "Bob", "Smith", "bob.smith@example.com");
        studentRepository.save(student);

        // Send a GET request to the /students/{id} endpoint to delete the student
        restTemplate.delete("/students/" + student.getId(), String.class);

        // Verify that the student was deleted from the database
        Student deletedStudent = studentRepository.findById(student.getId()).orElse(null);
        MatcherAssert.assertThat(deletedStudent, Matchers.nullValue());
    }
}