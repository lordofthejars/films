package demo;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User implements Principal {

    private String username; 

    private List<String> roles = new ArrayList<String>();

    public User(String username, String... roles) {
      super();
      this.username = username;
      this.roles = Arrays.asList(roles);
    }

    @Override
    public String getName() { 
      return username;
    }

    public boolean isUserInRole(String role) { 
      return this.roles.contains(role);
    }

}
