

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StudentGradeApp {
    private static final String DATA_FILE_NAME = "student_grades.dat";
    private static final String PROJECT_TITLE = "Student Grade Tracker";
    private static final String[] GROUP_MEMBERS = {"Yosef Zekarias Tesfaye", "Ephrem Wogayehu Wolde", "Mihret Argaw Gebremeskel", "Lensa Habtamu Bekele", "Eyerusalem Zewde Ageze"};

    private AuthService authService;
    private StudentService studentService;
    private Scanner scanner;
    private User loggedInUser;

    public StudentGradeApp() {
        this.authService = new AuthService();
        IDataPersistence dataPersistence = new StudentFileSerializer(DATA_FILE_NAME);
        this.studentService = new StudentService(dataPersistence);
        this.scanner = new Scanner(System.in);
        this.loggedInUser = null;
    }

    public void start() {
        System.out.println("Welcome to the " + PROJECT_TITLE + "!");

        if (!performLogin()) {
            System.out.println("Login failed. Exiting application.");
           
            return;
        }

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getUserChoice();
            if (scanner.hasNextLine()) {
                 scanner.nextLine();
            }

            switch (choice) {
                case 1:
                    showHomePage();
                    break;
                case 2:
                    showAboutPage();
                    break;
                case 3:
                    if (loggedInUser != null && (loggedInUser.isAdmin() || loggedInUser.isTeacher())) {
                        manageStudentsMenu(); 
                    } else if (loggedInUser != null && loggedInUser.isStudent()) {
                        viewOwnGrades(); 
                    } else {
                         System.out.println("You must be logged in to access this feature.");
                    }
                    break;
                case 4:
                    running = false;
                    System.out.println("Saving data...");
                    try {
                        studentService.saveData(); 
                    } catch (DataStorageException e) {
                        System.err.println("Error: Failed to save data before exiting. " + e.getMessage());
                    }
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

             if (running) {
                 System.out.println("\nPress Enter to continue...");
                 scanner.nextLine(); 
             }
        }
        scanner.close();
    }

    private boolean performLogin() {
        int loginAttempts = 0;
        final int MAX_LOGIN_ATTEMPTS = 3;

        while (loginAttempts < MAX_LOGIN_ATTEMPTS) {
            try {
                System.out.println("\n--- Login ---");
                System.out.println("Select your role:");
                System.out.println("1. Admin");
                System.out.println("2. Teacher");
                System.out.println("3. Student");
                System.out.print("Enter role choice (1-3): ");

                int roleChoice = getUserChoice();
                 
                 if (scanner.hasNextLine()) {
                     scanner.nextLine();
                 }

                String selectedRole = null;
                switch (roleChoice) {
                    case 1: selectedRole = "ADMIN"; break;
                    case 2: selectedRole = "TEACHER"; break;
                    case 3: selectedRole = "STUDENT"; break;
                    default:
                        System.err.println("Invalid role choice. Please enter 1, 2, or 3.");
                        loginAttempts++;
                        System.err.println("Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - loginAttempts));
                        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                            return false;
                        }
                        continue; 
                }

                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

              
                loggedInUser = authService.login(username, password, selectedRole); 
                return true; 

            } catch (LoginException | EmptyInputException e) {
                System.err.println("Login Error: " + e.getMessage());
                loginAttempts++;
                System.err.println("Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - loginAttempts));
                if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                    return false;
                }
            } catch (InputMismatchException e) {
                 System.err.println("Invalid input. Please enter a number for the role choice.");
                 scanner.nextLine(); 
                 loginAttempts++;
                 System.err.println("Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - loginAttempts));
                 if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                     return false;
                 }
            }
            catch (Exception e) { 
                System.err.println("An unexpected error occurred during login: " + e.getMessage());
                e.printStackTrace(); 
                return false; 
            }
        }
        return false;
    }

    private void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("Logged in as: " + (loggedInUser != null ? loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")" : "Guest"));
        System.out.println("1. Home");
        System.out.println("2. About");

        if (loggedInUser != null && (loggedInUser.isAdmin() || loggedInUser.isTeacher())) {
            System.out.println("3. Manage Students");
        } else if (loggedInUser != null && loggedInUser.isStudent()) {
            System.out.println("3. View My Grades (Student)");
        } else {
             System.out.println("3. (Login Required)");
        }

        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private void showHomePage() {
        System.out.println("\n--- Home ---");
        System.out.println("Welcome to the " + PROJECT_TITLE + " main dashboard.");
        if (loggedInUser != null) {
             System.out.println("You are logged in as a " + loggedInUser.getRole() + ".");
             if (loggedInUser.isAdmin() || loggedInUser.isTeacher()) {
                 System.out.println("You can manage student records, track grades, and more.");
             } else if (loggedInUser.isStudent()) {
                 System.out.println("You can view your own academic results.");
             }
        } else {
             System.out.println("Please log in to access full features.");
        }
    }

    private void showAboutPage() {
        System.out.println("\n--- About ---");
        System.out.println("The Student Grade Tracker is a console-based Java application designed to manage student academic records, track subjects, and store grades. Developed using Object-Oriented Programming (OOP) principles, this application provides a structured way to handle student data, subject information, and their corresponding grades.");
        System.out.println("\nProject Title: " + PROJECT_TITLE); 
        System.out.println("Developed by:"); 
        for (String member : GROUP_MEMBERS) {
            System.out.println("  - " + member);
        }

    }

    private void manageStudentsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Student Management Menu (" + loggedInUser.getRole() + ") ---");

            
            System.out.println("1. Add New Student" + (loggedInUser.isAdmin() ? "" : " (Admin Only)"));
            System.out.println("2. View Student Details (and their grades)");
            System.out.println("3. Update Student Name" + (loggedInUser.isAdmin() ? "" : " (Admin Only)"));
            System.out.println("4. Delete Student" + (loggedInUser.isAdmin() ? "" : " (Admin Only)"));
            System.out.println("5. Add Subject to Student" + (loggedInUser.isAdmin() ? "" : " (Admin Only)")); 
            System.out.println("6. Update Subject Grade for Student");
            System.out.println("7. Remove Subject from Student" + (loggedInUser.isAdmin() ? "" : " (Admin Only)"));
            System.out.println("8. View All Students (Summary)");
            System.out.println("9. Calculate Average Grade for a Student"); 
            System.out.println("10. Back to Main Menu"); 

            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
             
             if (scanner.hasNextLine()) {
                 scanner.nextLine();
             }

            try {
                switch (choice) {
                    case 1:
                        if (loggedInUser.isAdmin()) addStudent();
                        else System.out.println("Access denied. Admin only."); 
                        break;
                    case 2: viewStudentDetails(); break;
                    case 3:
                        if (loggedInUser.isAdmin()) updateStudentName();
                        else System.out.println("Access denied. Admin only."); 
                        break;
                    case 4:
                        if (loggedInUser.isAdmin()) deleteStudent();
                        else System.out.println("Access denied. Admin only."); 
                        break;
                    case 5:
                        if (loggedInUser.isAdmin()) addSubjectToStudent();
                        else System.out.println("Access denied. Admin only."); 
                        break;
                    case 6: // Available to Teacher
                         if (loggedInUser.isAdmin() || loggedInUser.isTeacher()) updateStudentSubjectGrade();
                         else System.out.println("Access denied. Only administrators and teachers can update subject grades.");
                         break;
                    case 7:
                        if (loggedInUser.isAdmin()) removeSubjectFromStudent();
                        else System.out.println("Access denied. Admin only."); 
                        break;
                    case 8: viewAllStudents(); break; 
                    case 9: 
                        if (loggedInUser.isAdmin() || loggedInUser.isTeacher()) viewStudentAverageGrade();
                        else System.out.println("Access denied. Only administrators and teachers can calculate average grades."); // Updated message
                        break;
                    case 10: back = true; break;
                    default: System.out.println("Invalid choice. Please try again.");
                }
    
                 if (!back && choice >=1 && choice <=9 &&
                     (loggedInUser.isAdmin() || (loggedInUser.isTeacher() && (choice == 2 || choice == 6 || choice == 8 || choice == 9)))) {
                
                     studentService.saveData();
                     System.out.println("\nData saved.");
                 }


            } catch (StudentNotFoundException | DuplicateStudentException | SubjectNotFoundException |
                     InvalidGradeException | EmptyInputException | DataStorageException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.err.println("Invalid input type. Please enter a valid number or text as required.");
                 
            } catch (Exception e) { 
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); 
            }
             
             if (!back && (choice < 1 || choice > 10 || (choice >=1 && choice <=9 && !(loggedInUser.isAdmin() || (loggedInUser.isTeacher() && (choice == 2 || choice == 6 || choice == 8 || choice == 9)))))) {
                 
                 System.out.println("\nPress Enter to continue in Student Management...");
                 scanner.nextLine(); 
             } else if (!back && choice >=1 && choice <=9 && (loggedInUser.isAdmin() || (loggedInUser.isTeacher() && (choice == 2 || choice == 6 || choice == 8 || choice == 9)))) {
                 
                 System.out.println("\nPress Enter to continue in Student Management...");
                 scanner.nextLine(); 
             }
        }
    }

    
    private void viewOwnGrades() {
         System.out.println("\n--- View My Grades ---"); 
         if (loggedInUser == null || !loggedInUser.isStudent()) {
             System.out.println("Access denied. This feature is for students only.");
             return;
         }

         try {
            
             String studentIdToView = loggedInUser.getUsername();

             Student student = studentService.findStudent(studentIdToView);
             student.displayDetails(); 

         } catch (StudentNotFoundException e) {
             System.err.println("Error: Your student record was not found. Please contact an administrator.");
         } catch (EmptyInputException e) {
             
             System.err.println("Internal Error: Student ID cannot be empty.");
         } catch (Exception e) {
             System.err.println("An unexpected error occurred while viewing grades: " + e.getMessage());
             e.printStackTrace();
         }
    }


    private int getUserChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
    
            return -1; 
        }
    }

    private String promptString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private double promptDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                double value = scanner.nextDouble();
                scanner.nextLine(); 
                return value;
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine(); 
            }
        }
    }


    
    private void addStudent() throws DuplicateStudentException, EmptyInputException {
        System.out.println("\n--- Add New Student ---");
        String studentId = promptString("Enter Student ID: ");
        String name = promptString("Enter Student Name: ");
        studentService.addStudent(studentId, name);
    }

    private void viewStudentDetails() throws StudentNotFoundException, EmptyInputException {
        System.out.println("\n--- View Student Details ---");
        String studentId = promptString("Enter Student ID to view details: ");
        Student student = studentService.findStudent(studentId);
        student.displayDetails(); 
    }

    private void updateStudentName() throws StudentNotFoundException, EmptyInputException {
        System.out.println("\n--- Update Student Name ---");
        String studentId = promptString("Enter Student ID to update: ");
        String newName = promptString("Enter new name for the student: ");
        studentService.updateStudentName(studentId, newName);
    }

    private void deleteStudent() throws StudentNotFoundException, EmptyInputException {
        
        System.out.println("\n--- Delete Student ---");
        String studentId = promptString("Enter Student ID to delete: ");
       
        System.out.print("Are you sure you want to delete student ID " + studentId + "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("yes")) {
            studentService.deleteStudent(studentId);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void viewAllStudents() {
        System.out.println("\n--- All Students Summary ---");
        List<Student> students = studentService.getAllStudents();
        if (!students.isEmpty()) {
            
            System.out.printf("%-15s | %-25s | %-12s | %s%n", "Student ID", "Name", "Avg. Grade", "Subjects");
            System.out.println("----------------------------------------------------------------------------");
            for (Student student : students) {
                
                System.out.printf("%-15s | %-25s | %-12.2f | %d%n",
                        student.getStudentId(),
                        student.getName(),
                        student.calculateAverageGrade(),
                        student.getSubjects().size());
            }
        }
        
    }

    private void viewStudentAverageGrade() throws StudentNotFoundException, EmptyInputException {
        System.out.println("\n--- Calculate Student Average Grade (GPA) ---"); 
        String studentId = promptString("Enter Student ID: ");
        double avgGrade = studentService.getStudentAverageGrade(studentId);
        Student student = studentService.findStudent(studentId); 
        System.out.printf("Average GPA for %s (ID: %s): %.2f%n", student.getName(), studentId, avgGrade); 
    }


    
    private void addSubjectToStudent() throws StudentNotFoundException, EmptyInputException, InvalidGradeException {
        
        System.out.println("\n--- Add Subject to Student (Admin Only) ---");
        String studentId = promptString("Enter Student ID to add subject to: ");
        String subjectCode = promptString("Enter Subject Code: ");
        String subjectName = promptString("Enter Subject Name: ");
        studentService.addSubjectToStudent(studentId, subjectCode, subjectName, 0.0); 
    }

    private void updateStudentSubjectGrade() throws StudentNotFoundException, SubjectNotFoundException, InvalidGradeException, EmptyInputException {
        System.out.println("\n--- Update Subject Grade ---");
        String studentId = promptString("Enter Student ID: ");
        String subjectCode = promptString("Enter Subject Code to update grade for: ");

        
        Student student = studentService.findStudent(studentId);
        student.findSubject(subjectCode)
            .ifPresentOrElse(
                subj -> System.out.println("Current grade for " + subj.getSubjectName() + " ("+ subj.getSubjectCode() +"): " + subj.getGrade()),
                () -> System.out.println("Subject " + subjectCode + " not found for this student. Proceeding to update if it exists...")
            );

        double newGrade = promptDouble("Enter new Grade (0-100): "); 
        studentService.updateStudentSubjectGrade(studentId, subjectCode, newGrade);
    }

    private void removeSubjectFromStudent() throws StudentNotFoundException, SubjectNotFoundException, EmptyInputException {
         
        System.out.println("\n--- Remove Subject from Student ---");
        String studentId = promptString("Enter Student ID: ");
        String subjectCode = promptString("Enter Subject Code to remove: ");
       
        System.out.print("Are you sure you want to remove subject " + subjectCode + " from student " + studentId + "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("yes")) {
            studentService.deleteSubjectFromStudent(studentId, subjectCode);
        } else {
            System.out.println("Removal cancelled.");
        }
    }


    public static void main(String[] args) {
        StudentGradeApp app = new StudentGradeApp();
        app.start();
    }
}
