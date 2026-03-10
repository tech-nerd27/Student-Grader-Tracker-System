// Person.java
// Purpose: Abstract base class for any person-like entity.
// Demonstrates: Abstraction, Encapsulation.

import java.io.Serializable; // Needed if Person objects (or subclasses) are serialized

public abstract class Person implements Serializable {
    // Encapsulated fields
    protected String id;
    protected String name;

    // Constructor
    public Person(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.id = id;
        this.name = name;
    } 

    // Getter for ID
    public String getId() {
        return id;
    }

    // Getter for Name
    public String getName() {
        return name;
    }

    // Setter for Name (ID is typically immutable or managed differently)
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    // Abstract method to be implemented by subclasses
    // Demonstrates: Polymorphism
    public abstract void displayDetails();

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}
