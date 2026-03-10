// Subject.java
// Purpose: Represents a single subject taken by a student, including its code, name, and grade.
// Demonstrates: Encapsulation, Data Validation, Grading Logic.

import java.io.Serializable;

public class Subject implements Serializable {
    private String subjectCode;
    private String subjectName;
    private double grade; // Numerical grade (0-100)
    // Removed creditHours

    // Constructor - Removed creditHours parameter
    public Subject(String subjectCode, String subjectName, double initialGrade) throws InvalidGradeException {
        if (subjectCode == null || subjectCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code cannot be empty.");
        }
         if (subjectName == null || subjectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty.");
        }
         if (initialGrade < 0 || initialGrade > 100) {
             throw new InvalidGradeException("Initial grade must be between 0 and 100.");
         }
         // Removed creditHours validation


        this.subjectCode = subjectCode.trim();
        this.subjectName = subjectName.trim();
        this.grade = initialGrade;
        // Removed creditHours initialization
    }

    // Getters
    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public double getGrade() {
        return grade;
    }

    // Removed getCreditHours()


    // Setter for grade with validation
    public void setGrade(double grade) throws InvalidGradeException {
        if (grade < 0 || grade > 100) {
            throw new InvalidGradeException("Grade must be between 0 and 100.");
        }
        this.grade = grade;
    }

    // Removed setCreditHours()


    /**
     * Determines the letter grade based on the numerical grade (0-100)
     * using the specified grading system.
     *
     * @return The corresponding letter grade (e.g., A+, B, F).
     */
    public String getGradeLetter() {
        if (grade >= 90) {
            return "A+";
        } else if (grade >= 85) {
            return "A";
        } else if (grade >= 80) {
            return "A-";
        } else if (grade >= 75) {
            return "B+";
        } else if (grade >= 70) {
            return "B";
        } else if (grade >= 65) {
            return "B-";
        } else if (grade >= 60) {
            return "C+";
        } else if (grade >= 55) {
            return "C";
        } else if (grade >= 50) {
            return "C-";
        } else if (grade >= 40) {
            return "D";
        } else {
            return "F";
        }
    }

    @Override
    public String toString() {
        return "Subject{" +
               "subjectCode='" + subjectCode + '\'' +
               ", subjectName='" + subjectName + '\'' +
               ", grade=" + grade +
               // Removed creditHours from toString
               '}';
    }
}
