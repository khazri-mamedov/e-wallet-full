package org.kuehnenagel.util;

import java.math.BigDecimal;

public class BalanceUnderflowException extends RuntimeException {
    private final BigDecimal currentBalance;
    
    public BalanceUnderflowException(BigDecimal currentBalance) {
        super("Not enough money in your wallet!");
        this.currentBalance = currentBalance;
    }
    
    public BigDecimal getCurrentBalance()
    {
        return currentBalance;
    }
}
