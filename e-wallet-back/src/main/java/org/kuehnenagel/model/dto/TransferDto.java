package org.kuehnenagel.model.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

// TODO add validation messages to messages.properties
public class TransferDto {
    @NotNull(message = "From wallet can't be null")
    @Range(min = 1, message = "Starts with 1")
    private Integer fromWalletId;
    
    @NotNull(message = "To wallet can't be null")
    @Range(min = 1, message = "Starts with 1")
    private Integer toWalletId;
    
    @NotNull(message = "Amount can't be null")
    @DecimalMin(value = "0.1", message = "Min amount for transfer is 0.1")
    private BigDecimal amount;
    
    public Integer getFromWalletId() {
        return fromWalletId;
    }
    
    public void setFromWalletId(Integer fromWalletId) {
        this.fromWalletId = fromWalletId;
    }
    
    public Integer getToWalletId() {
        return toWalletId;
    }
    
    public void setToWalletId(Integer toWalletId) {
        this.toWalletId = toWalletId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
