package org.kuehnenagel.util;

import java.util.Collections;
import java.util.List;

public class ApiError {
    
    private final String message;
    private final List<String> errors;
    
    public ApiError(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }
    
    public ApiError(String message, String error) {
        this.message = message;
        errors = Collections.singletonList(error);
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public List<String> getErrors()
    {
        return errors;
    }
}
