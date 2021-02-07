package org.kuehnenagel.model.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

// TODO add validation messages to messages.properties
public class WalletDto {
    private Integer id;
    
    @NotNull(message = "Name can't be null")
    @NotEmpty(message = "Name is empty")
    private String name;
    
    @NotNull(message = "Balance is null")
    @DecimalMin(value = "0.0", message = "Balance is negative!")
    private BigDecimal balance;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "WalletDto{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
