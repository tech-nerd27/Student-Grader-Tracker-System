
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.Serializable; 

public class StudentService implements Serializable {
    private List<Student> students;
    private IDataPersistence dataPersistence; 

    // Constructor
    public StudentService(IDataPersistence dataPersistence) {
        this.dataPersistence = dataPersistence;
        this.students = loadData(); 
        if (this.students == null) {
            this.students = new ArrayList<>();
            
        }
    }

    
    public void addStudent(String studentId, String name) throws DuplicateStudentException, EmptyInputException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new EmptyInputException("Student ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyInputException("Student name cannot be empty.");
        }

        if (students.stream().anyMatch(s -> s.getStudentId().equalsIgnoreCase(studentId.trim()))) {
            throw new DuplicateStudentException("Student with ID '" + studentId + "' already exists.");
        }

    
        students.add(new Student(studentId.trim(), name.trim()));
        System.out.println("Student '" + name.trim() + "' with ID '" + studentId.trim() + "' added successfully.");
    }

   
    public Student findStudent(String studentId) throws StudentNotFoundException, EmptyInputException {
         if (studentId == null || studentId.trim().isEmpty()) {
            throw new EmptyInputException("Student ID cannot be empty.");
        }
        
        Optional<Student> foundStudent = students.stream()
                .filter(s -> s.getStudentId().equalsIgnoreCase(studentId.trim()))
                .findFirst();

        if (foundStudent.isPresent()) {
            return foundStudent.get();
        } else {
            throw new StudentNotFoundException("Student with ID '" + studentId.trim() + "' not found.");
        }
    }

    
    public void updateStudentName(String studentId, String newName) throws StudentNotFoundException, EmptyInputException {
        Student student = findStudent(studentId); 
         if (newName == null || newName.trim().isEmpty()) {
            throw new EmptyInputException("New student name cannot be empty.");
        }
        student.setName(newName.trim());
        System.out.println("Student ID '" + studentId + "' name updated to '" + newName.trim() + "'.");
    }

   
    public void deleteStudent(String studentId) throws StudentNotFoundException, EmptyInputException {
        Student studentToRemove = findStudent(studentId); 
        students.remove(studentToRemove);
        System.out.println("Student ID '" + studentId + "' deleted successfully.");
    }

    
    public List<Student> getAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found in the system.");
        }
        return new ArrayList<>(students); 
    }

    
    public double getStudentAverageGrade(String studentId) throws StudentNotFoundException, EmptyInputException {
        Student student = findStudent(studentId); 
        return student.calculateAverageGrade();
    }

   
    public void addSubjectToStudent(String studentId, String subjectCode, String subjectName, double grade) throws StudentNotFoundException, EmptyInputException, InvalidGradeException {
        Student student = findStudent(studentId); 

         if (subjectCode == null || subjectCode.trim().isEmpty()) {
             throw new EmptyInputException("Subject Code cannot be empty.");
         }
         if (subjectName == null || subjectName.trim().isEmpty()) {
             throw new EmptyInputException("Subject Name cannot be empty.");
         }
         if (grade < 0 || grade > 100) {
             throw new InvalidGradeException("Grade must be between 0 and 100.");
         }

        student.addSubject(new Subject(subjectCode.trim(), subjectName.trim(), grade));
        System.out.println("Subject '" + subjectName.trim() + "' added to student '" + student.getName() + "'.");
    }

    
    public void updateStudentSubjectGrade(String studentId, String subjectCode, double newGrade) throws StudentNotFoundException, SubjectNotFoundException, InvalidGradeException, EmptyInputException {
        Student student = findStudent(studentId); 

         if (subjectCode == null || subjectCode.trim().isEmpty()) {
             throw new EmptyInputException("Subject Code cannot be empty.");
         }
         if (newGrade < 0 || newGrade > 100) {
             throw new InvalidGradeException("Grade must be between 0 and 100.");
         }

        student.updateSubjectGrade(subjectCode.trim(), newGrade); 
        System.out.println("Grade for subject '" + subjectCode.trim() + "' updated for student '" + student.getName() + "'.");
    }

    
    public void deleteSubjectFromStudent(String studentId, String subjectCode) throws StudentNotFoundException, SubjectNotFoundException, EmptyInputException {
        Student student = findStudent(studentId); 

         if (subjectCode == null || subjectCode.trim().isEmpty()) {
             throw new EmptyInputException("Subject Code cannot be empty.");
         }

        student.removeSubject(subjectCode.trim()); 
        System.out.println("Subject '" + subjectCode.trim() + "' removed from student '" + student.getName() + "'.");
    }


    
    public void saveData() throws DataStorageException {
        dataPersistence.save(students);
    }

   
    private List<Student> loadData() {
        try {
            return dataPersistence.load();
        } catch (DataStorageException e) {
            System.err.println("Warning: Could not load student data. Starting with an empty list. " + e.getMessage());
            return new ArrayList<>(); 
        }
    }
}
