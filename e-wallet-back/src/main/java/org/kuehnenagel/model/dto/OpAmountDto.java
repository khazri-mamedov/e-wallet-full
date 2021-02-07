package org.kuehnenagel.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.kuehnenagel.util.BalanceSerializer;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

// TODO add validation messages to messages.properties
public class OpAmountDto {
    @NotNull(message = "Amount is null!")
    @DecimalMin(value = "0.1", message = "Min amount is 0.1")
    @JsonSerialize(using = BalanceSerializer.class)
    private BigDecimal amount;
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
