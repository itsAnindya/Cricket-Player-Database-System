import java.io.Serializable;

public class LoginDTO implements Serializable {
    
    public LoginDTO (String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername () {
        return username;
    }
    
    public void setUsername (String username) {
        this.username = username;
    }
    
    public String getPassword () {
        return password;
    }
    
    public void setPassword (String password) {
        this.password = password;
    }
    
    public boolean isStatus () {
        return status;
    }
    
    public void setStatus (boolean status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "LoginDTO [username=" + username + ", password=" + password + ", status=" + status + "]";
    }
    
    private String username;
    private String password;
    private boolean status;
}
