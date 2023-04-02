## Student Administration System
This is a simple web-based student management system built using Spring Boot. 
It allows users to perform CRUD operations on students.

### Technologies Used

- Spring Boot
- Thymeleaf
- Bootstrap
- H2

### Getting Started
To get started with this project, you will need to have the following installed:

- Java Development Kit (JDK) version 8 or later
- Maven

Once you have Java and Maven installed, you can clone the repository and run the project using the following commands:

- git clone https://github.com/SemeniukRuslan/StudentAdministrationSystem.git
- cd StudentAdministrationSystem
- mvn spring-boot:run

This will start the application on port 8082. You can access it by visiting http://localhost:8082 in your web browser.

### Features
This student management system allows users to perform the following operations:

- Add, view, update, and delete students
- Add, view, update, and delete courses
- Enroll students in courses and view enrollments

### Validation

Before the CRUD operations the following conditions must be met:

* all fields should be present;
* first_name should be a positive length;
* last_name should be a positive length;
* email should be not empty and have a valid format;

### Create new student

Data structure used to the form to create new student.

![formToCreateNewStudent](https://github.com/SemeniukRuslan/StudentAdministrationSystem/blob/master/src/main/resources/FormToCreateNewStudent.png "formToCreateNewStudent")

### List student

List all students in database.

![ListStudents](ListStudents.png "ListStudents")

### List student after update

List all students in database after update.

![ListStudentsAfterUpdate](ListStudentsAfterUpdate.png "ListStudentsAfterUpdate")

### List student after delete one student

List all students in database after delete one student.

![ListStudentsAfterDelete](ListStudentsAfterDelete.png "ListStudentsAfterDelete")

### Future Plans

* create exception handler
* create tests to service and controller
* update project with react component
