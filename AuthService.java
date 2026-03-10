
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> users;
    

    public AuthService() {
        this.users = new ArrayList<>();
        this.users.add(new User("admin", "1234", "ADMIN"));
        this.users.add(new User("teacher", "1234", "TEACHER"));

    
        this.users.add(new User("Nascr/4000/16", "1234", "STUDENT"));
        this.users.add(new User("Nascr/1891/16", "1234", "STUDENT"));
        this.users.add(new User("Nascr/2958/16", "1234", "STUDENT"));
        this.users.add(new User("Nascr/2675/16", "1234", "STUDENT"));
        this.users.add(new User("Nascr/2042/16", "1234", "STUDENT"));

        
    }


    
    public User login(String username, String password, String expectedRole) throws LoginException, EmptyInputException {
        if (username == null || username.trim().isEmpty()) {
            throw new EmptyInputException("Username cannot be empty.");
        }
        if (password == null || password.isEmpty()) {
            throw new EmptyInputException("Password cannot be empty.");
        }
         if (expectedRole == null || expectedRole.trim().isEmpty()) {
             throw new IllegalArgumentException("Expected role cannot be null or empty.");
         }

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username.trim())) { 
                if (user.checkPassword(password)) {
                   
                    if (user.getRole().equalsIgnoreCase(expectedRole.trim())) {
                        System.out.println("Login successful. Welcome, " + user.getUsername() + " (" + user.getRole() + ")!");
                        return user; 
                    } else {
                        throw new LoginException("Login failed: Role mismatch."); 
                    }
                } else {
                    throw new LoginException("Invalid password."); 
                }
            }
        }
        throw new LoginException("Username not found."); 
    }
}
