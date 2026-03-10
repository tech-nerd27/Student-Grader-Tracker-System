

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentFileSerializer implements IDataPersistence {
    private String fileName;

    
    public StudentFileSerializer(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Saves the list of students to a file using Java Serialization.
     *
     * @param students The list of Student objects to save.
     * @throws DataStorageException if an error occurs during the save operation.
     */
    @Override 
    public void save(List<Student> students) throws DataStorageException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(students);
            
        } catch (IOException e) {
            
            throw new DataStorageException("Failed to save student data to " + fileName + ": " + e.getMessage(), e);
        }
    }

    /**
     * Loads the list of students from a file using Java Deserialization.
     *
     * @return The list of loaded Student objects.
     * @throws DataStorageException if an error occurs during the load operation.
     */
    @Override 
    @SuppressWarnings("unchecked") 
    public List<Student> load() throws DataStorageException {
        List<Student> students = new ArrayList<>(); 
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            
            Object obj = ois.readObject();
            if (obj instanceof List) {
                
                students = (List<Student>) obj;
                
            } else {
                 
                 System.err.println("Warning: File content is not a List of Students. Starting with an empty list.");
                 students = new ArrayList<>(); 
            }
        } catch (IOException e) {
            
            if (e instanceof java.io.FileNotFoundException) {
                System.out.println("Data file not found. Starting with an empty student list.");
                return new ArrayList<>(); 
            }
            
            throw new DataStorageException("Failed to load student data from " + fileName + ": " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
           
            throw new DataStorageException("Failed to load student data: Class definition not found. " + e.getMessage(), e);
        } catch (ClassCastException e) {
            
             throw new DataStorageException("Failed to load student data: Invalid data format in file. " + e.getMessage(), e);
        }
        return students;
    }
}
