// Student.java
// Purpose: Represents a single student with their ID, name, and a list of subjects and grades.
// Demonstrates: Encapsulation, Data Structures (ArrayList), Object Relationships, Simple Average Calculation.

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Import Optional

public class Student implements Serializable {
    private String studentId;
    private String name;
    private List<Subject> subjects;

    // Constructor
    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.subjects = new ArrayList<>();
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public List<Subject> getSubjects() {
        return new ArrayList<>(subjects); // Return a copy to prevent external modification
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    // Add a subject to the student's list
    public void addSubject(Subject subject) {
        // Optional: Add check for duplicate subjects by code
        if (subjects.stream().noneMatch(s -> s.getSubjectCode().equalsIgnoreCase(subject.getSubjectCode()))) {
            this.subjects.add(subject);
        } else {
            System.err.println("Subject with code '" + subject.getSubjectCode() + "' already exists for student '" + this.name + "'.");
        }
    }

    /**
     * Finds a subject by its code within the student's list of subjects.
     *
     * @param subjectCode The code of the subject to find.
     * @return An Optional containing the Subject if found, otherwise an empty Optional.
     */
    public Optional<Subject> findSubject(String subjectCode) {
        if (subjectCode == null || subjectCode.trim().isEmpty()) {
            return Optional.empty(); // Return empty for null or empty input
        }
        return subjects.stream()
                       .filter(s -> s.getSubjectCode().equalsIgnoreCase(subjectCode.trim()))
                       .findFirst();
    }


    // Update the grade for an existing subject
    public void updateSubjectGrade(String subjectCode, double newGrade) throws SubjectNotFoundException, InvalidGradeException {
        Optional<Subject> subjectToUpdate = findSubject(subjectCode); // Use the new findSubject method

        if (subjectToUpdate.isPresent()) {
             if (newGrade < 0 || newGrade > 100) {
                 throw new InvalidGradeException("Grade must be between 0 and 100.");
             }
            subjectToUpdate.get().setGrade(newGrade);
        } else {
            throw new SubjectNotFoundException("Subject with code '" + subjectCode + "' not found for student '" + this.name + "'.");
        }
    }

    // Remove a subject from the student's list
    public void removeSubject(String subjectCode) throws SubjectNotFoundException {
         boolean removed = subjects.removeIf(subject -> subject.getSubjectCode().equalsIgnoreCase(subjectCode));
         if (!removed) {
             throw new SubjectNotFoundException("Subject with code '" + subjectCode + "' not found for student '" + this.name + "'.");
         }
    }


    /**
     * Calculates the simple average of numerical grades (0-100) for the student.
     *
     * @return The calculated average grade (0-100 scale). Returns 0.0 if the student has no subjects.
     */
    public double calculateAverageGrade() {
        if (subjects.isEmpty()) {
            return 0.0; // Return 0 if no subjects
        }

        double totalGrade = 0;
        int numberOfSubjects = 0;

        for (Subject subject : subjects) {
            totalGrade += subject.getGrade();
            numberOfSubjects++;
        }

        // Avoid division by zero
        if (numberOfSubjects == 0) {
            return 0.0;
        }

        return totalGrade / numberOfSubjects; // Calculate simple average
    }

    // Removed convertNumericalGradeToGpa()


    // Display student details, including subjects and grades/GPA
    public void displayDetails() {
        System.out.println("\n--- Student Details ---");
        System.out.println("ID: " + studentId);
        System.out.println("Name: " + name);
        System.out.println("Subjects:");

        if (subjects.isEmpty()) {
            System.out.println("  No subjects added yet.");
        } else {
            // Display each subject with its numerical grade and letter grade
            System.out.printf("  %-15s | %-25s | %-10s | %s%n", "Code", "Name", "Grade (0-100)", "Letter Grade"); // Removed GPA Points and Credit Hours
            System.out.println("  ------------------------------------------------------------------"); // Adjusted separator
            for (Subject subject : subjects) {
                System.out.printf("  %-15s | %-25s | %-10.2f | %s%n",
                        subject.getSubjectCode(),
                        subject.getSubjectName(),
                        subject.getGrade(),
                        subject.getGradeLetter()); // Assuming Subject has getGradeLetter()
                        // Removed GPA points and credit hours display
            }
        }

        // Display the simple average grade
        System.out.printf("\nAverage Grade: %.2f%n", calculateAverageGrade()); // Changed to Average Grade
    }

    @Override
    public String toString() {
        return "Student{" +
               "studentId='" + studentId + '\'' +
               ", name='" + name + '\'' +
               ", subjects=" + subjects.size() + " subjects" +
               '}';
    }
}
