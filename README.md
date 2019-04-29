# Object-Oriented Programming Project (MySTARS)

## Introduction
A school project where my team were tasked to design and develop a Console-based application (non-Graphical UI) for My Student Automated Registration System (MySTARS).

## Approach Taken
The main idea for our design approach is to separate the classes into separate responsibilities, to avoid having a god class. The classes are then separated and grouped into 4 packages; interface, control, boundary, and entity to reflect their respective stereotypes. This creates a neat and organised class design architecture. 

The student class is designed in to facilitate operations done by students, via accepting inputs from the boundary class and invoking the method in respective entity classes to achieve the requested command.

Serialization is implemented instead of a text file based database to allow easy storage of objects instead of rows and columns of texts and string. 

The MainAppUI class consists of sub-menu methods to allow for ease of code modifications and neat structure, to make it easily understandable and readable. 

## For the UI:
-	Details of IndexGroups and students are displayed in a proper and neat UI table.
-	Users interact with the system by pressing number keys 1-9 to select the option provided by the current menu on display. 
-	Each student has 1 schedule that contains the list of courses they have registered.
-	Password is masked when users are entering their password to prevent shoulder-surfing. Passwords are also stored as a hash in the database for added level of security. 
-	Error checking is done for invalid inputs such as user entering alphabetical data when system requested for numerical integers.


## Principles Used
**Single Responsibility Principle (SRP)**

Each class in the project have a well-defined responsibility. For example, the “StudentMgmt” class is design for the management of students and nothing else. 

**Open-Closed Principle (OCP)** 

Abstraction have been implemented for login interface of students and administrator. The student and administrator both uses different databases to store their login credential. However, since they use similar login interface, we allow “Student” and “Admin” class to implement “LoginInterface” to override the login method. The login method allows the classes to access different databases without changing the source code of the module. 

**Assumptions**
1.	All databases have been created in the ‘database’ folder and pre-loaded with existing data.
2.	The pre-loaded data in the databases are 100% correct and accurate. (No errors while retrieving data)
3.	During Student creation by the Administrator, the access period is set such that the Student will be able to access the account for at least a year upon creation. (until changed by the Administrator)
4.	During Student creation by the Administrator, the email and mobile number provided is valid and the default notification mode is set to both email and SMS. 
5.	When creating or editing a Student, the Administrator should only add / update student names with a maximum of 21 characters.
6.	The Administrator is a responsible person and will only enter meaningful data. 
(He will always enter verified and correct data provided from the school, 
etc. he will not add a course with a course name “I CREATED THIS FOR FUN”) 
7.	Students taking double degrees belongs to two schools. 
8.	Each Course belongs to only one school.
9.	There is no AU credits limit for students to overload modules. 
10.	“School” class is not included as they are not needed in this scenario.	
