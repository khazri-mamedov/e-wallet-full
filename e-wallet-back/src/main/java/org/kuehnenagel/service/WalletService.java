package org.kuehnenagel.service;

import org.kuehnenagel.model.dto.WalletDto;

import java.math.BigDecimal;
import java.util.Collection;

public interface WalletService {
    Collection<WalletDto> getAllWallets();
    WalletDto getWallet(final int walletId);
    WalletDto createWallet(final WalletDto walletDto);
    
    WalletDto withdrawFrom(final BigDecimal amount, final int walletId);
    
    WalletDto addTo(final BigDecimal amount, final int walletId);
    
    /**
     * Transfer works only for internal wallets (e.g same user)
     */
    Collection<WalletDto> transferInternal(int fromWallet, int toWallet, BigDecimal amount);
    
    void deleteById(final Integer id);
}
