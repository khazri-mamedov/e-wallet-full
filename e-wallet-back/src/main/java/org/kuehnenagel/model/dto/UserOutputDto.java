package org.kuehnenagel.model.dto;

public class UserOutputDto {
    private String username;
    private String token;
    
    public UserOutputDto(String token, String username) {
        this.token = token;
        this.username = username;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getToken()
    {
        return token;
    }
    
    public void setToken(String token)
    {
        this.token = token;
    }
}
