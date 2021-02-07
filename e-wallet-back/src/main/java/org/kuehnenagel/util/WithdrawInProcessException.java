package org.kuehnenagel.util;

public class WithdrawInProcessException extends RuntimeException {
    private final int walletId;
    
    public WithdrawInProcessException(final int walletId) {
        super("Withdraw is in process!");
        this.walletId = walletId;
    }
}
