// IDataPersistence.java
// Purpose: Defines the interface for data persistence operations (saving and loading student data).
// Demonstrates: Interfaces.

import java.util.List;

public interface IDataPersistence {

    /**
     * Saves the list of students to a persistent storage.
     *
     * @param students The list of Student objects to save.
     * @throws DataStorageException if an error occurs during the save operation.
     */
    void save(List<Student> students) throws DataStorageException;

    /**
     * Loads the list of students from a persistent storage.
     *
     * @return The list of loaded Student objects.
     * @throws DataStorageException if an error occurs during the load operation.
     */
    List<Student> load() throws DataStorageException;
}
