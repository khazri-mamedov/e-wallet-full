package org.kuehnenagel.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

// TODO add validation messages to messages.properties
public class UserDto {
    @NotNull(message = "username can't be null")
    @NotEmpty(message = "username is empty")
    private String username;
    
    @NotNull(message = "password can't be null")
    @NotEmpty(message = "password is empty")
    private String password;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
