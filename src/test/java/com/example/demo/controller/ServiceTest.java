package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private List<Student> studentList;

    @BeforeEach
    public void setUp() {
        studentList = new ArrayList<>();

        Student student1 = new Student(1, "Bob", "Marlie", "bob@gmail.com");
        Student student2 = new Student(2, "Bobby", "Perlay", "bobby@gmail.com");

        studentList.add(student1);
        studentList.add(student2);
    }

    @Test
    void findById() throws Exception {
        when(studentRepository.findById(2)).thenReturn(Optional.of(studentList.get(1)));

        Optional<Student> studentById = Optional.ofNullable(studentService.getStudentById(2));

        assertAll(() -> {
            assertEquals(studentList.get(1).getEmail(), studentById.get().getEmail());
            assertEquals(studentList.get(1).getFirstName(), studentById.get().getFirstName());
        });

        verify(studentRepository).findById(2);
    }
}