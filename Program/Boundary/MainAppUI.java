/**
 * Represents the main boundary class for users to access the program.
 * @author Lim Boon Leng
 * @version 1.8
 * @since 2017-04-12
 */

package Boundary;
import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.Scanner;

import Control.CourseMgmtController;
import Control.PasswordHashController;
import Control.StudentMgmtController;
import Objects.Admin;
import Objects.Course;
import Objects.IndexGroup;
import Objects.Lesson;
import Objects.Schedule;
import Objects.Student;
import Objects.Waitlist;

public class MainAppUI {
	/**
	 * Initialise the main menu for users to use.
	 * @param args The arguments to invoke.
	 */
	public static void main(String[] args) {
		mainMenu();
	}

	/**
	 * Allows users to login.
	 */
	public static void mainMenu() {
		Console cons = System.console();
		Admin admin = new Admin();
		Student student = new Student();
		Scanner sc = new Scanner(System.in);

		while (admin.getAdminID() == null|| student.getStudentID() == null) {
			System.out.println("WELCOME TO NTU STARS SYSTEM (CONSOLE)");
			System.out.println("Please enter your userID: ");
			String userID = sc.nextLine();

			char[] password = cons.readPassword("Please enter your password: ");
			String passString = new String(password);

			if (admin.login(userID, PasswordHashController.hash(passString))) {
				admin = new Admin(userID, PasswordHashController.hash(passString));
				adminMenu(admin, sc);
			} else if (student.login(userID, PasswordHashController.hash(passString))) {
				student = student.retrieveStudentObject(userID);
				studentTimeout(student);
				studentMenu(student, sc);
			} else {
				System.out.println("Invalid userID or password!\n");
			}
			admin.setAdminID(null);
			student.setStudentID(null);
		}
	}

	/**
	 * Provides a list of operations for an admin that is logged in.
	 * @param admin The current admin in session.
	 * @param sc The scanner that reads in input from the admin.
	 */
	public static void adminMenu(Admin admin, Scanner sc) {
		int choice = 0;
		boolean validInput = false;

		do {
			System.out.println("");
			System.out.println("Welcome, Admin");
			System.out.println("What do you want to do? ");
			System.out.println("1) Add a new course");
			System.out.println("2) Add a new index group");
			System.out.println("3) Update existing course");
			System.out.println("4) Check vacancy for an existing index group");
			System.out.println("5) Add a new student");
			System.out.println("6) Edit student access periods");
			System.out.println("7) Print list of students by index group number");
			System.out.println("8) Print list of students by course");
			System.out.println("9) Logout");

			do {
				try {
					choice = sc.nextInt();
					sc.nextLine();
				if (choice >= 1) {
					validInput = true;
					}
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid integer!");
					sc.nextLine();
				}
			} while (!validInput); 

			switch (choice) {
			case 1:
				adminAddCourseMenu(sc);
				break;
			case 2:
				adminAddIndexGrpMenu(sc);
				break;
			case 3:
				adminUpdateCourseMenu(sc);
				break;
			case 4:
				adminCheckVacancyMenu(sc);
				break;
			case 5:
				adminAddStudentMenu(sc);
				break;
			case 6:
				adminEditStudentAccMenu(sc);
				break;
			case 7:
				adminPrintStudByGrpMenu(sc);
				break;
			case 8:
				adminPrintStudByCourseMenu(sc);
				break;
			default:
				System.out.println("");
				break;
			}
		} while (choice > 0 && choice < 9);
	}

	/**
	 * Provides a list of operations for a student that is logged in.
	 * @param stud The current student in session.
	 * @param sc The scanner that reads in input from the student.
	 */
	public static void studentMenu(Student stud, Scanner sc) {
		int choice = 0;
		boolean validInput = false;

		do {
			System.out.println("");
			System.out.println("Welcome, " + stud.getStudentID());
			System.out.println("What do you want to do? ");
			System.out.println("1) Register a course");
			System.out.println("2) Drop course");
			System.out.println("3) Print courses registered");
			System.out.println("4) Check vacancies for an index group number");
			System.out.println("5) Change index group number of course");
			System.out.println("6) Swap index group number with another student");
			System.out.println("7) Select Notification Mode");
			System.out.println("8) Logout");
			
			do {
				try {
					choice = sc.nextInt();
				if (choice >= 1) {
					validInput = true;
					sc.nextLine();
					}
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid integer!");
					sc.nextLine();
				}
			} while (!validInput);
			validInput = false;

			switch (choice) {
			case 1:
				studentTimeout(stud);
				studentRegistCourseMenu(stud, sc);
				stud = stud.retrieveStudentObject(stud.getStudentID());
				break;
			case 2:
				studentTimeout(stud);
				studentDropCourseMenu(stud, sc);
				stud = stud.retrieveStudentObject(stud.getStudentID());
				break;
			case 3:
				studentTimeout(stud);
				studentPrintCoursesMenu(stud);
				break;
			case 4:
				studentTimeout(stud);
				studentCheckVacancyMenu(stud, sc);
				break;
			case 5:
				studentTimeout(stud);
				studentChangeIndexGroupMenu(stud, sc);
				stud = stud.retrieveStudentObject(stud.getStudentID());
				break;
			case 6:
				studentTimeout(stud);
				studentSwapIndexGroupMenu(stud, sc);
				stud = stud.retrieveStudentObject(stud.getStudentID());
				break;
			case 7:
				studentTimeout(stud);
				studentSelectNotifMenu(stud, sc);
				stud = stud.retrieveStudentObject(stud.getStudentID());
				break;
			default:
				System.out.println("");
				break;
			}
		} while (choice > 0 && choice < 8);
	}

	/**
	 * Provides a form for an admin that is logged in to add a new course.
	 * @param sc The scanner that reads in input from the admin.
	 */
	@SuppressWarnings("resource")
	public static void adminAddCourseMenu(Scanner sc) {
		String courseID;
		String courseName;
		String school;
		String courseType;
		ArrayList<Integer> indexGroupList = new ArrayList<Integer>();
		String indexList;
		Course tempCourse;
		int AUCredits = 1;
		boolean validInput = false;
		Course course = new Course();
		
		System.out.println("Enter the course code: ");
		courseID = sc.nextLine();
		System.out.println("Enter the course name: ");
		courseName = sc.nextLine();
		System.out.println("Enter the AU credits worth: ");
		do {
			try {
			AUCredits = sc.nextInt();
			sc.nextLine();
			if (AUCredits >= 1) {
				validInput = true;
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;
		System.out.println("Enter the course type: (CORE/ONLINE/GERPE/GER-CORE/UE)");
		courseType = sc.nextLine();
		System.out.println("Enter the school: ");
		school = sc.nextLine();
		System.out.println("Enter the list of index groups: etc.(10111;10112;10113...) ");
		Scanner s = null;
		
		do {
			indexList = sc.nextLine();
			s = new Scanner(indexList).useDelimiter(";");

			while (s.hasNext()) {
				try {
					int index = s.nextInt();
					if (index >= 0) {
						validInput = true;
						indexGroupList.add(index);
					}
				} catch (InputMismatchException e) {
					validInput = false;
					indexGroupList.clear();
					System.out.println("Enter valid integers!");
					break;
				}
			}
		} while (!validInput);
		s.close();

		tempCourse = new Course(courseID.toUpperCase(), courseName, AUCredits, school, courseType, indexGroupList);

		System.out.println("Confirm to add course: Y/N");
		char option = sc.next().charAt(0);

		if (option == 'Y' || option == 'y') {
			if (course.retrieveCourseObject(courseID) == null) {
				CourseMgmtController.addCourse(tempCourse);
				System.out.println("Course successfully added. Returning to main menu.\n");
				Course.printAllCourses();
			} else {
				System.out.println("Course already exists! Returning to main menu.\n");
			}
		} else if (option == 'N' || option == 'n') {
			System.out.println("Cancelled. Returning to main menu.");
		} else {
			while (option != 'Y' || option != 'y' || option != 'N' || option != 'n') {
				System.out.println("Enter Y/N only!");
				option = sc.next().charAt(0);

				if (option == 'Y' || option == 'y') {
					if (course.retrieveCourseObject(courseID) == null) {
						CourseMgmtController.addCourse(tempCourse);
						System.out.println("Course successfully added. Returning to main menu.\n");
						Course.printAllCourses();
						break;
					} else {
						System.out.println("Course already exists! Returning to main menu.\n");
						break;
					}
				} else if (option == 'N' || option == 'n') {
					System.out.println("Cancelled. Returning to main menu.\n");
					break;
				}
			}
		}
	}
	
	/**
	 * Provides a form for an admin that is logged in to add a new index group.
	 * @param sc The scanner that reads in input from the admin.
	 */
	public static void adminAddIndexGrpMenu(Scanner sc) {
		Integer indexGroupID = null;
		String indexGroupName;
		int numOfLessons = 0;
		Waitlist wl = new Waitlist();	
		ArrayList<Lesson> lessonList = new ArrayList<Lesson>();	
		Queue<String> studentQueueList = new LinkedList<String>();
		boolean validInput = false;

		System.out.println("Enter the index group number: ");
		do {
			try {
			indexGroupID = sc.nextInt();
			sc.nextLine();
			if (indexGroupID >= 1) {
				validInput = true;
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;
		System.out.println("Enter the index group name: ");
		indexGroupName = sc.nextLine();
		System.out.println("Enter the number of lessons: ");
		do {
			try {
			numOfLessons = sc.nextInt();
			sc.nextLine();
			if (numOfLessons >= 1) {
				validInput = true;
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;
		
		for (int i = 0; i < numOfLessons; i++) {
			System.out.println("\nEnter the details for lesson " + (i + 1));
			System.out.println("Enter the teacher's designation and name: ");
			String prof = sc.nextLine();
			System.out.println("Enter the day of the lesson: (full day name)");
			String day = sc.nextLine();
			System.out.println("Enter the start time of the lesson: ");
			String start = sc.nextLine();
			System.out.println("Enter the end time of the lesson: ");
			String end = sc.nextLine();
			System.out.println("Enter the venue of the lesson: ");
			String venue = sc.nextLine();
			System.out.println("Choose the type of lesson: \n1. LEC/STUDIO \n2. TUT \n3. LAB");
			int lessonType = 0;
			String lessType = null;
			do {
				try {
					lessonType = sc.nextInt();
				sc.nextLine();
				if (lessonType >= 1 && lessonType <= 3) {
					validInput = true;
					}
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid integer!");
					sc.nextLine();
				}
			} while (!validInput);
			validInput = false;
			
			if (lessonType == 1) {
				lessType = "LEC/STUDIO";
			} else if (lessonType == 2) {
				lessType = "TUT";
			} else if (lessonType == 3) {
				lessType = "LAB";
			}
			
			Lesson l1 = new Lesson(prof, day, start, end, venue, lessType, null);	
			lessonList.add(l1);
		}
		
		boolean flag = false;
		int waitListID = 0;
		
		while (!flag) {
			flag = !(wl.checkWaitListExists(waitListID));
			waitListID++;
		}
		
		wl = new Waitlist(waitListID, indexGroupID, studentQueueList);	
		IndexGroup ig = new IndexGroup(indexGroupID, 0, 10, indexGroupName, lessonList, wl);

		System.out.println("Confirm to add course: Y/N");
		char option = sc.next().charAt(0);

		if (option == 'Y' || option == 'y') {
			if (ig.retrieveIndexGroupObject(indexGroupID) == null) {
				CourseMgmtController.addIndexGrp(ig);
				System.out.println("Index Group successfully added. Returning to main menu.\n");
			} else {
				System.out.println("Index Group already exists! Returning to main menu.\n");
			}
		} else if (option == 'N' || option == 'n') {
			System.out.println("Cancelled. Returning to main menu.");
		} else {
			while (option != 'Y' || option != 'y' || option != 'N' || option != 'n') {
				System.out.println("Enter Y/N only!");
				option = sc.next().charAt(0);

				if (option == 'Y' || option == 'y') {
					if (ig.retrieveIndexGroupObject(indexGroupID) == null) {
						CourseMgmtController.addIndexGrp(ig);
						System.out.println("Index Group successfully added. Returning to main menu.\n");
						break;
					} else {
						System.out.println("Index Group already exists! Returning to main menu.\n");
						break;
					}
				} else if (option == 'N' || option == 'n') {
					System.out.println("Cancelled. Returning to main menu.\n");
					break;
				}
			}
		}
	}

	/**
	 * Provides a form for an admin that is logged in to update an existing course.
	 * @param sc The scanner that reads in input from the admin.
	 */
	public static void adminUpdateCourseMenu(Scanner sc) {
		int option = 0;
		String courseName;
		int AUCredits = 1;
		String school;
		String courseType;
		ArrayList<Integer> indexGroupList = new ArrayList<Integer>();
		Integer indexGrp = null;
		Course tempCourse = new Course();
		String courseCode;
		boolean validInput = false;

		System.out.println("Enter the course code you wish to update: ");
		courseCode = sc.nextLine();

		tempCourse = tempCourse.retrieveCourseObject(courseCode);

		if (tempCourse != null) {
			System.out.println("Course exists! What do you want to update?: ");
			System.out.println("1) Name of the course");
			System.out.println("2) AU Credits of the course");
			System.out.println("3) School of the course");
			System.out.println("4) Type of the course (CORE/GERPE/UE/Online)");
			System.out.println("5) Add new index group to the course");
			System.out.println("6) Remove existing index group from the course");
			System.out.println("7) Go back to main menu");

			do {
				try {
					option = sc.nextInt();
				if (option >= 1) {
					validInput = true;
					sc.nextLine();
					}
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid integer!");
					sc.nextLine();
				}
			} while (!validInput);

			validInput = false;
			
			switch (option) {
			case 1:
				System.out.println("Current name of the course: " + tempCourse.getCourseName());
				System.out.println("Enter the new name of the course: ");
				courseName = sc.nextLine();
				tempCourse.setCourseName(courseName);
				break;
			case 2:
				System.out.println("Current AU credits of the course: " + tempCourse.getAUCredits());
				System.out.println("Enter the new AU credits of the course: ");
				do {
					try {
					AUCredits = sc.nextInt();
					if (AUCredits >= 1) {
						validInput = true;
						sc.nextLine();
						}
					} catch (InputMismatchException e) {
						System.out.println("Enter a valid integer!");
						sc.nextLine();
					}
				} while (!validInput);
				validInput = false;
				tempCourse.setAUCredits(AUCredits);
				break;
			case 3:
				System.out.println("Current school of the course: " + tempCourse.getSchool());
				System.out.println("Enter the new school of the course: ");
				school = sc.nextLine();
				tempCourse.setSchool(school);
				break;
			case 4:
				System.out.println("Current course type of the course: " + tempCourse.getCourseType());
				System.out.println("Enter the new course type of the course: ");
				courseType = sc.nextLine();
				tempCourse.setCourseType(courseType);
				break;
			case 5:
				indexGroupList = tempCourse.getIndexGroupList();
				System.out.println("Current list of index groups in the course: " + indexGroupList);
				for (int i = 0; i < indexGroupList.size(); i++) {
					System.out.println(indexGroupList.get(i));
				}
				System.out.println("Enter the new index group to add: ");
				do {
					try {
					indexGrp = sc.nextInt();
					if (indexGrp >= 1) {
						validInput = true;
						sc.nextLine();
						}
					} catch (InputMismatchException e) {
						System.out.println("Enter a valid integer!");
						sc.nextLine();
					}
				} while (!validInput);
				validInput = false;
				indexGroupList.add(indexGrp);
				tempCourse.setIndexGroupList(indexGroupList);
				break;
			case 6:
				indexGroupList = tempCourse.getIndexGroupList();
				System.out.println("Current list of index groups in the course: " + indexGroupList);
				for (int i = 0; i < indexGroupList.size(); i++) {
					System.out.println(indexGroupList.get(i));
				}
				System.out.println("Enter the index group to remove: ");
				do {
					try {
					indexGrp = sc.nextInt();
					if (indexGrp >= 1) {
						validInput = true;
						sc.nextLine();
						}
					} catch (InputMismatchException e) {
						System.out.println("Enter a valid integer!");
						sc.nextLine();
					}
				} while (!validInput);
				validInput = false;
				indexGroupList.remove(indexGrp);
				tempCourse.setIndexGroupList(indexGroupList);
				break;
			default:
				break;
			}

			if (CourseMgmtController.updateCourse(tempCourse)) {
				System.out.println("Course successfully updated! Returning to main menu.");
			}
		} else {
			System.out.println("Course DOES NOT exists! Returning to main menu.");
		}
	}

	/**
	 * Provides a form for an admin that is logged in to check the vacancy of a course's index group.
	 * @param sc The scanner that reads in input from the admin.
	 */
	public static void adminCheckVacancyMenu(Scanner sc) {
		IndexGroup tempGrp = new IndexGroup();
		Integer indexGrp = null;
		boolean validInput = false;

		System.out.println("Enter the index group number: ");
		
		do {
			try {
			indexGrp = sc.nextInt();
			if (indexGrp >= 1) {
				validInput = true;
				sc.nextLine();
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;

		tempGrp = tempGrp.retrieveIndexGroupObject(indexGrp);
		if (tempGrp != null) {
			System.out.println("Vacancy of Index Group " + indexGrp + ": " + tempGrp.calculateVacancy());
			System.out.println("Current number of students: " + tempGrp.getCurrentNumOfStudents());
			System.out.println("Maximum number of students: " + tempGrp.getMaxNumOfStudents());
		}
	}

	/**
	 * Provides a form for an admin that is logged in to add a new student.
	 * @param sc The scanner that reads in input from the admin.
	 */
	@SuppressWarnings("resource")
	private static void adminAddStudentMenu(Scanner sc) {
		String studentID;
		String passwordHash;
		String name;
		String matricNum;
		String nationality;
		char gender;
		String school;
		ArrayList<String> schoolList = new ArrayList<String>();
		String email;
		String mobileNum;

		System.out.println("Enter the new student's ID: ");
		studentID = sc.nextLine();
		System.out.println("Enter the new student's password: ");
		passwordHash = sc.nextLine();
		passwordHash = PasswordHashController.hash(passwordHash);
		System.out.println("Enter the new student's name: ");
		name = sc.nextLine();
		System.out.println("Enter the new student's matriculation number: ");
		matricNum = sc.nextLine();
		System.out.println("Enter the new student's nationality: ");
		nationality = sc.nextLine();
		System.out.println("Enter the new student's gender: (M/F)");
		gender = sc.nextLine().charAt(0);
		while (gender != 'M' && gender != 'F') {
			System.out.println("Please enter M or F!");
			gender = sc.nextLine().charAt(0);
		}
		System.out.println("Enter the new student's school: (school1;school2) ");
		school = sc.nextLine();
		Scanner s = new Scanner(school).useDelimiter(";");
		while (s.hasNext()) {
			schoolList.add(s.next());
		}
		s.close();
		System.out.println("Enter the new student's email address: ");
		email = sc.nextLine();
		System.out.println("Enter the new student's mobile number: ");
		mobileNum = sc.nextLine();

		ArrayList<String> courseList = new ArrayList<String>();
		ArrayList<Integer> indexGroupList = new ArrayList<Integer>();
		ArrayList<String> exemptedCoursesList = new ArrayList<String>();
		ArrayList<String> completedCoursesList = new ArrayList<String>();
		ArrayList<Integer> waitListIDList = new ArrayList<Integer>();
		Schedule schedule = new Schedule();
		Calendar startTime = Calendar.getInstance(Locale.ENGLISH);
		Calendar endTime = Calendar.getInstance(Locale.ENGLISH);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.ENGLISH);

		try {
			startTime.setTime(sdf.parse("05/04/17 00:00:00"));
			endTime.setTime(sdf.parse("05/04/18 23:00:00"));
		} catch (Exception e) {
		}

		Student s1 = new Student(studentID, passwordHash, name, matricNum, nationality, gender, schoolList, courseList,
				indexGroupList, exemptedCoursesList, completedCoursesList, waitListIDList, schedule, startTime, endTime,
				email, mobileNum, "both");

		System.out.println("Confirm to add student: Y/N");
		char option = sc.next().charAt(0);

		if (option == 'Y' || option == 'y') {
			if (StudentMgmtController.checkExistingStudent(studentID) == false) {
				StudentMgmtController.addStudent(s1);
				System.out.println("Student successfully added. Returning to main menu.");
				StudentMgmtController.printAllStudents();
			} else {
				System.out.println("Student already exists! Returning to main menu.");
			}
		} else if (option == 'N' || option == 'n') {
			System.out.println("Cancelled. Returning to main menu.");
		} else {
			while (option != 'Y' || option != 'y' || option != 'N' || option != 'n') {
				System.out.println("Enter Y/N only!");
				option = sc.next().charAt(0);

				if (option == 'Y' || option == 'y') {
					if (StudentMgmtController.checkExistingStudent(studentID) == false) {
						StudentMgmtController.addStudent(s1);
						System.out.println("Student successfully added. Returning to main menu.");
						StudentMgmtController.printAllStudents();
						break;
					} else {
						System.out.println("Student already exists! Returning to main menu.");
						break;
					}
				} else if (option == 'N' || option == 'n') {
					System.out.println("Cancelled. Returning to main menu.");
					break;
				}
			}
		}
	}

	/**
	 * Provides a form for an admin that is logged in to edit an existing student.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void adminEditStudentAccMenu(Scanner sc) {
		String studentID;
		String startStr;
		String endStr;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.ENGLISH);
		Calendar start = Calendar.getInstance(Locale.ENGLISH);
		Calendar end = Calendar.getInstance(Locale.ENGLISH);
		boolean validInput = false;

		System.out.println("Enter the student's ID: ");
		studentID = sc.nextLine();
		
		while (!validInput) {
			System.out.println("Enter the start of the access period: (dd/MM/yy HH:mm:ss)");
			startStr = sc.nextLine();
			System.out.println("Enter the end of the access period: (dd/MM/yy HH:mm:ss)");
			endStr = sc.nextLine();
	
			try {
				start.setTime(sdf.parse(startStr));
				end.setTime(sdf.parse(endStr));
				validInput = true;
			} catch (Exception e) {
				validInput = false;
				System.out.println("You entered the wrong format!");
			}
		}

		System.out.println("Confirm to update student's access period: Y/N");
		char option = sc.next().charAt(0);

		if (option == 'Y' || option == 'y') {
			if (StudentMgmtController.checkExistingStudent(studentID) == true) {
				StudentMgmtController.editStudentLoginPeriod(studentID, start, end);
				System.out.println("Student's access period successfully updated. Returning to main menu.");
			} else {
				System.out.println("Student does not exist! Returning to main menu.");
			}
		} else if (option == 'N' || option == 'n') {
			System.out.println("Cancelled. Returning to main menu.");
		} else {
			while (option != 'Y' || option != 'y' || option != 'N' || option != 'n') {
				System.out.println("Enter Y/N only!");
				option = sc.next().charAt(0);

				if (option == 'Y' || option == 'y') {
					if (StudentMgmtController.checkExistingStudent(studentID) == true) {
						StudentMgmtController.editStudentLoginPeriod(studentID, start, end);
						System.out.println("Student's access period successfully updated. Returning to main menu.");
						break;
					} else {
						System.out.println("Student does not exist! Returning to main menu.");
						break;
					}
				} else if (option == 'N' || option == 'n') {
					System.out.println("Cancelled. Returning to main menu.");
					break;
				}
			}
		}

	}

	/**
	 * Provides a form for an admin that is logged in to display all students that are
	 * registered in a specified index group.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void adminPrintStudByGrpMenu(Scanner sc) {
		Integer indexGrpNumber = null;
		boolean validInput = false;
		IndexGroup indexGroup = new IndexGroup();

		System.out.println("Enter the index group number: ");
		do {
			try {
				indexGrpNumber = sc.nextInt();
			if (indexGrpNumber >= 1) {
				validInput = true;
				sc.nextLine();
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;

		if (indexGroup.retrieveIndexGroupObject(indexGrpNumber) != null) {
			StudentMgmtController.printStudentsInIndexGrp(indexGrpNumber);
		} else {
			System.out.println("Index group DOES NOT exists! Returning to main menu.");
		}
	}

	/**
	 * Provides a form for an admin that is logged in to display all students that are
	 * registered in a specified course.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void adminPrintStudByCourseMenu(Scanner sc) {
		String courseID;
		Course course = new Course();

		System.out.println("Enter the course code: ");
		courseID = sc.nextLine();

		if (course.retrieveCourseObject(courseID) != null) {
			StudentMgmtController.printStudentsInCourse(courseID);
		} else {
			System.out.println("Course DOES NOT exists! Returning to main menu.");
		}
	}

	/**
	 * Provides a form for a student that is logged in to select their preferred notification mode.
	 * @param stud The current student in session.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void studentSelectNotifMenu(Student stud, Scanner sc) {
		String email;
		String mobileNum;
		int choice = 0;
		boolean validInput = false;

		System.out.println("Choose your preferred notification mode: ");
		System.out.println("Current preferred mode: " + stud.getNotifMode());
		System.out.println("1) Via email to " + stud.getEmail());
		System.out.println("2) Via SMS to " + stud.getMobileNum());
		System.out.println("3) Both");
		System.out.println("4) Update email address");
		System.out.println("5) Update mobile number");
		System.out.println("6) Return to main menu");

		do {
			try {
				choice = sc.nextInt();
			if (choice >= 1) {
				validInput = true;
				sc.nextLine();
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;

		switch (choice) {
		case 1:
			stud.setNotifMode("EMAIL");
			stud.updateStudentObject(stud);
			System.out.println("You have successfully set your preferred mode to be via email. Returning to main menu.\n");
			break;
		case 2:
			stud.setNotifMode("SMS");
			stud.updateStudentObject(stud);
			System.out.println("You have successfully set your preferred mode to be via SMS. Returning to main menu.\n");
			break;
		case 3:
			stud.setNotifMode("BOTH");
			stud.updateStudentObject(stud);
			System.out.println("You have successfully set your preferred mode to be via both email and sms. Returning to main menu.\n");
			break;
		case 4:
			System.out.println("Your current email address: " + stud.getEmail());
			System.out.println("Enter your new email address: ");
			email = sc.nextLine();
			stud.setEmail(email);
			stud.updateStudentObject(stud);
			System.out.println("Your email has been successfully updated. Returning to main menu.\n");
			break;
		case 5:
			System.out.println("Your current mobile number: " + stud.getMobileNum());
			System.out.println("Enter your new mobile number: ");
			mobileNum = sc.nextLine();
			stud.setMobileNum(mobileNum);
			stud.updateStudentObject(stud);
			System.out.println("Your mobile number has been successfully updated. Returning to main menu.\n");
			break;
		default:
			break;
		}
	}

	/**
	 * Provides a form for a student that is logged in to swap their index group with another peer.
	 * @param stud The current student in session.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void studentSwapIndexGroupMenu(Student stud, Scanner sc) {
		String peerUsername;
		String peerPassword;
		int ownIndexNumber = 0;
		int peerIndexNumber = 0;
		boolean validInput = false;
		Console cons = System.console();

		System.out.println("Enter the peer's username: ");
		peerUsername = sc.nextLine();

		char[] password = cons.readPassword("Enter the peer's password: ");
		peerPassword = new String(password);
		
		System.out.println("Enter the index group number you want to swap: ");
		do {
			try {
				ownIndexNumber = sc.nextInt();
			if (ownIndexNumber >= 1) {
				validInput = true;
				sc.nextLine();
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;
		System.out.println("Enter the peer's index group number to swap: ");
		do {
			try {
				peerIndexNumber = sc.nextInt();
			if (peerIndexNumber >= 1) {
				validInput = true;
				sc.nextLine();
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;

		stud.swapIndex(peerUsername, PasswordHashController.hash(peerPassword), ownIndexNumber, peerIndexNumber, sc);
	}

	/**
	 * Provides a form for a student that is logged in to change their Index Group.
	 * @param stud The current student in session.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void studentChangeIndexGroupMenu(Student stud, Scanner sc) {
		int currentChoice = 0;
		int newChoice = 0;
		boolean validInput = false;

		System.out.println("Enter your current index group number: ");
		do {
			try {
				currentChoice = sc.nextInt();
			if (currentChoice >= 1) {
				validInput = true;
				sc.nextLine();
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;
		System.out.println("Enter the new index group number: ");
		do {
			try {
				newChoice = sc.nextInt();
			if (newChoice >= 1) {
				validInput = true;
				sc.nextLine();
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;

		stud.changeIndex(currentChoice, newChoice, sc);

	}

	/**
	 * Provides a form for a student that is logged in to check the vacancy of an index group.
	 * @param stud The current student in session.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void studentCheckVacancyMenu(Student stud, Scanner sc) {
		IndexGroup tempGrp = new IndexGroup();
		Integer indexGrp = null;
		boolean validInput = false;

		System.out.println("Enter the index group number: ");
		do {
			try {
				indexGrp = sc.nextInt();
			if (indexGrp >= 1) {
				validInput = true;
				sc.nextLine();
				}
			} catch (InputMismatchException e) {
				System.out.println("Enter a valid integer!");
				sc.nextLine();
			}
		} while (!validInput);
		validInput = false;

		tempGrp = tempGrp.retrieveIndexGroupObject(indexGrp);
		if (tempGrp != null) {
			System.out.println("Number of vacancies: " + tempGrp.calculateVacancy());
			System.out.println("Number of students: " + tempGrp.getCurrentNumOfStudents() + "/" + tempGrp.getMaxNumOfStudents() + "\n");
		} else {
			System.out.println("Index group does not exist!");
		}
	}

	/**
	 * Displays the courses registered for a student that is logged in.
	 * @param stud The current student in session.
	 */
	private static void studentPrintCoursesMenu(Student stud) {
		stud.printCoursesRegistered();
	}

	/**
	 * Provides a form for a student that is logged in to drop a course.
	 * @param stud The current student in session.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void studentDropCourseMenu(Student stud, Scanner sc) {
		stud.dropCourse(sc);
	}

	/**
	 * Provides a form for a student that is logged in to register a course.
	 * @param stud The current student in session.
	 * @param sc The scanner that reads in input from the admin.
	 */
	private static void studentRegistCourseMenu(Student stud, Scanner sc) {
		String courseID;
		Integer indexGroupNum = null;
		boolean belongsToSchool = false;
		boolean validInput = false;

		System.out.println("Enter the course code you want to register: ");
		courseID = sc.nextLine();

		Course tempCourse = new Course();
		tempCourse = tempCourse.retrieveCourseObject(courseID);

		if (tempCourse != null) {
			if (tempCourse.getCourseType().equals("CORE")) {
				for (int i = 0; i < stud.getSchool().size(); i++) {
					if (stud.getSchool().get(i).equals(tempCourse.getSchool())) {
						belongsToSchool = true;
						System.out.println("List of index group numbers in " + tempCourse.getCourseName() + ":");
						for (int j = 0; j < tempCourse.getIndexGroupList().size(); j++) {
							IndexGroup temp = new IndexGroup();
							temp = temp.retrieveIndexGroupObject(tempCourse.getIndexGroupList().get(j));
							System.out.println(temp.getIndexGroupID() + " (" + temp.getCurrentNumOfStudents() + "/"+ temp.getMaxNumOfStudents() + ")");
						}

						System.out.println("Enter the index group number you want to register: ");
						do {
							try {
								indexGroupNum = sc.nextInt();
							if (indexGroupNum >= 1) {
								validInput = true;
								sc.nextLine();
								}
							} catch (InputMismatchException e) {
								System.out.println("Enter a valid integer!");
								sc.nextLine();
							}
						} while (!validInput);
						validInput = false;

						stud.registerCourse(indexGroupNum, sc);
						break;
					}
				}
				if (belongsToSchool == false) {
					System.out.println("You are not eligible for this course!");
				}
			} else {
				System.out.println("List of index group numbers in " + tempCourse.getCourseName() + ":");
				for (int j = 0; j < tempCourse.getIndexGroupList().size(); j++) {
					System.out.println(tempCourse.getIndexGroupList().get(j));
				}

				System.out.println("Enter the index group number you want to register: ");
				do {
					try {
						indexGroupNum = sc.nextInt();
					if (indexGroupNum >= 1) {
						validInput = true;
						sc.nextLine();
						}
					} catch (InputMismatchException e) {
						System.out.println("Enter a valid integer!");
						sc.nextLine();
					}
				} while (!validInput);
				validInput = false;

				stud.registerCourse(indexGroupNum, sc);
			}
		} else {
			System.out.println("Course does not exist!");
		}
	}

	/**
	 * Validates if the student that is logged in is allowed access to MySTARS
	 * @param stud The current student in session.
	 */
	private static void studentTimeout(Student stud) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar currentTime = Calendar.getInstance();
		// System.out.println(sdf.format(currentTime.getTime()));

		if (currentTime.compareTo(stud.getStartTime()) < 0) {
			System.out.println("You are not allowed to access yet!! Try again in the stipulated period\n");
			mainMenu();
		} else if (currentTime.compareTo(stud.getEndTime()) > 0) {
			System.out.println("Your access period is over! Please contact the system administrator\n");
			mainMenu();
		}
	}
}