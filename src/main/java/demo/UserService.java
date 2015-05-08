package demo;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {

    protected static List<UserInfo> users = Arrays.asList
                                          (
                                            new UserInfo("alex", "alex", "admin"),
                                            new UserInfo("neo", "neo", "superadmin")
                                          );

    public boolean findUser(String username, String password) {
        return users.stream()
                      .filter(u -> u.getUsername().equals(username))
                      .filter(u -> u.getPassword().equals(password))
                      .findAny().isPresent();
    }

    public User findUser(String username) {
        return users.stream().filter(u -> u.getUsername().equals(username))
                      .map(u -> new User(u.getUsername(), u.getRoles()))
                      .findAny().get();
    }

    private static class UserInfo {

        private String username;
        private String password;
        private String[] roles;

        public UserInfo(String username, String password,
                String...roles) {
            super();
            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
        public String[] getRoles() {
            return roles;
        }
    }
}
