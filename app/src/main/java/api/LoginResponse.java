package api;

public class LoginResponse {
    private String message;
    private User user;
    private String token;

    public LoginResponse() {
        // Default constructor
    }

    public LoginResponse(String message, User user, String token) {
        this.message = message;
        this.user = user;
        this.token = token;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
