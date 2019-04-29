/**
 * @author Ang Poh Keong
 * @version 1.0
	@since 2017-04-06
 */

package Control;

import java.util.*;

import Objects.Student;

public class StudentMgmtController {
	/**
     * Check if student exist in the school database
     * @param studentID ID of student 
     * @return Results of the checking
     */
	public static boolean checkExistingStudent(String studentID) {
		boolean success = false;
		List<Student> list = new ArrayList<Student>();
		Student student = new Student();
		
		try {
			list = student.retrieveAllStudentObjects();

			for (int i = 0; i < list.size(); i++) {
				Student s1 = (Student) list.get(i);

				if (s1.getStudentID().equals(studentID)) {
					success = true;
					break;
				}

			}
		} catch (Exception e) {
			System.out.println("Exception >> " + e.getMessage());
		}
		return success;
	}

	 /**
     * Add student to the database
     * @param s1 Student object
     * @return Results of adding the student
     */
	public static boolean addStudent(Student s1) {
		boolean success = false;
		List<Student> list = new ArrayList<Student>();
		Student student = new Student();
		
		try {
			list = student.retrieveAllStudentObjects();
			list.add(s1);
			student.writeSerializedObject(list);

			success = true;
		} catch (Exception e) {
			System.out.println("Exception >> " + e.getMessage());
		}
		return success;
	}
	
    /**
     * Edit the login period of a student
     * @param studentID ID of student
     * @param start Start date for the login period
     * @param end End date for the login period
     * @return Results of editing the student login period
     */
	public static boolean editStudentLoginPeriod(String studentID, Calendar start, Calendar end) {
		boolean success = false;
		List<Student> list = new ArrayList<Student>();
		Student student = new Student();

		try {
			list = student.retrieveAllStudentObjects();

			for (int i = 0; i < list.size(); i++) {
				Student s1 = (Student) list.get(i);

				if (s1.getStudentID().equals(studentID)) {
					s1.setStartTime(start);
					s1.setEndTime(end);

					s1.updateStudentObject(s1);

					success = true;
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception >> " + e.getMessage());
		}
		return success;
	}
	
	 /**
     * Method for admin to print list of students in the course. Check for course already exists is done in MainApp before invoke of this method
     * @param courseID of course
     * @return Results if the method is run successfully
     */
    public static boolean printStudentsInCourse(String courseID) {
        boolean success = false;
        List<Student> studList = new ArrayList<Student>();
        int studCount = 0;
        Student student = new Student();
 
        try {
            studList = student.retrieveAllStudentObjects();
 
            System.out.println("List of students in the course " + courseID + ":\n");
 
            System.out.println("========================================================");
            System.out.println("|         Name         |  Gender  |     Nationality    |");
            System.out.println("========================================================");

            
            for (int i = 0; i < studList.size(); i++) {
                Student student1 = (Student) studList.get(i);
 
                if (student1.getCourseList() != null) {
                    if (student1.getCourseList().contains(courseID)) {
                        studCount++;            
                        System.out.format("| %-21s| %-9s| %-19s|\n",student1.getName(),student1.getGender(),student1.getNationality());
                    }
                }
            }
            System.out.println("========================================================");
 
            if (studCount == 0) {
                System.out.println("There are no students in this course!");
            } else {
                System.out.println("Number of students in the course: " + studCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
 
    /**
     * Method for admin to print list of students in the course. Check for course already exists is done in MainApp before invoke of this method
     * @param indexGrpNumber ID of the index group
     * @return Results if the method is run successfully
     */
    public static boolean printStudentsInIndexGrp(Integer indexGrpNumber) {
        boolean success = false;
        List<Student> studList = new ArrayList<Student>();
        int studCount = 0;
        Student student = new Student();
 
        try {
            studList = student.retrieveAllStudentObjects();
 
            System.out.println("List of students in the index group " + indexGrpNumber + ":");
 
            System.out.println("========================================================");
            System.out.println("|         Name         |  Gender  |     Nationality    |");
            System.out.println("========================================================");

            
            for (int i = 0; i < studList.size(); i++) {
                Student student1 = (Student) studList.get(i);
 
                if (student1.getIndexGroupList() != null) {
                    if (student1.getIndexGroupList().contains(indexGrpNumber)) {
                        studCount++;
                       
                        System.out.format("| %-21s| %-9s| %-19s|\n",student1.getName(),student1.getGender(),student1.getNationality());               
                    }
                }
            }
            System.out.println("========================================================");
            
            if (studCount == 0) {
                System.out.println("There are no students in this index group!");
            } else {
                System.out.println("Number of students in the index group: " + studCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
    
    /**
     * Print the list of all students
     * @return Results if the method is run successfully
     */
    public static boolean printAllStudents() {
        boolean success = false;
        List<Student> studList = new ArrayList<Student>();
        int studCount = 0;
        Student student = new Student();
 
        try {
            studList = student.retrieveAllStudentObjects();
 
            System.out.println("List of students: \n");
 
            System.out.println("==============================================================================");
            System.out.println("|         Name         |   Matric Num   |     Student ID    |      School    |");
            System.out.println("==============================================================================");

            
            for (int i = 0; i < studList.size(); i++) {
                Student student1 = (Student) studList.get(i);
                System.out.format("| %-21s| %-15s| %-18s| %-15s|\n",student1.getName(),student1.getMatricNum(),student1.getStudentID(),student1.getSchool());
                
                studCount++;
            }
             
            System.out.println("==============================================================================");
            
            if (studCount == 0) {
                System.out.println("There are no students!");
            } else {
                System.out.println("\nNumber of students: " + studCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}
