package org.kuehnenagel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuehnenagel.model.WalletEntity;
import org.kuehnenagel.model.dto.WalletDto;
import org.kuehnenagel.model.mapper.WalletMapperImpl;
import org.kuehnenagel.repository.WalletRepository;
import org.kuehnenagel.service.impl.WalletServiceImpl;
import org.kuehnenagel.util.BalanceUnderflowException;
import org.kuehnenagel.util.NoSuchEntityException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Main business logic test class
 */
@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    
    @Mock
    private WalletRepository walletRepository;
    
    private WalletServiceImpl walletService;
    
    private final WalletMapperImpl walletMapper = new WalletMapperImpl();
    
    // @BeforeAll works with static context
    @BeforeEach
    public void setUp() {
        walletService = new WalletServiceImpl(walletRepository, walletMapper);
    }
    
    @Test
    public void getAllWallets_AllWallets_ReturnsNonEmptyCollection() {
        WalletEntity walletEntity = new WalletEntity();
        
        when(walletRepository.findAll()).thenReturn(Collections.singletonList(walletEntity));
        
        Collection<WalletDto> allWallets = walletService.getAllWallets();
        
        assertThat(allWallets, is(not(empty())));
    }
    
    @Test
    public void getWallet_OneWalletById_ThrowsNoSuchEntity() {
        when(walletRepository.findById(1)).thenReturn(Optional.empty());
        
        assertThrows(NoSuchEntityException.class, () -> walletService.getWallet(1));
    }
    
    @Test
    public void getWallet_OneWalletById_ReturnsWallet() {
        when(walletRepository.findById(1)).thenReturn(Optional.of(new WalletEntity()));
        
        assertNotNull(walletService.getWallet(1));
    }
    
    @Test
    public void createWallet_WalletCreated_EqualsDto() {
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setId(1);
        walletEntity.setName("Some");
        
        WalletDto walletDto = walletMapper.toDto(walletEntity);
        
        when(walletRepository.save(any())).thenReturn(walletEntity);
        
        assertEquals(walletService.createWallet(walletDto).getName(), walletEntity.getName());
    }
    
    @Test
    public void withdrawFrom_AmountMinus_ExpectedBalanceAfter() {
        BigDecimal initialBalance = new BigDecimal("10.0");
        BigDecimal amountToWithdraw = new BigDecimal("5.5");
        
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setBalance(initialBalance);
    
        when(walletRepository.findById(1)).thenReturn(Optional.of(walletEntity));
        when(walletRepository.save(walletEntity)).thenReturn(walletEntity);
    
        WalletDto walletDto = walletService.withdrawFrom(amountToWithdraw, 1);
        assertEquals(walletDto.getBalance(), initialBalance.subtract(amountToWithdraw));
    }
    
    @Test
    public void withdrawFrom_NotEnoughBalance_ThrowsBalanceUnderflow() {
        BigDecimal initialBalance = new BigDecimal("10.0");
        BigDecimal amountToWithdraw = new BigDecimal("11.2");
    
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setBalance(initialBalance);
    
        when(walletRepository.findById(1)).thenReturn(Optional.of(walletEntity));
    
        assertThrows(BalanceUnderflowException.class,
                () -> walletService.withdrawFrom(amountToWithdraw, 1));
    }
    
    @Test
    public void addTo_AmountPlussed_ExpectedBalanceAfter() {
        BigDecimal initialBalance = new BigDecimal("10.0");
        BigDecimal amountToAdd = new BigDecimal("2.2");
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setBalance(initialBalance);
    
        when(walletRepository.findById(1)).thenReturn(Optional.of(walletEntity));
        when(walletRepository.save(walletEntity)).thenReturn(walletEntity);
        
        assertEquals(walletService.addTo(amountToAdd, 1).getBalance(),
                initialBalance.add(amountToAdd));
    }
    
    @Test
    public void transferInternal_TransferCorrectly_ExpectedBalances() {
        WalletEntity fromWalletEntity = new WalletEntity();
        fromWalletEntity.setId(1);
        fromWalletEntity.setBalance(new BigDecimal("10.0"));
        
        when(walletRepository.findById(1)).thenReturn(Optional.of(fromWalletEntity));
        when(walletRepository.save(fromWalletEntity)).thenReturn(fromWalletEntity);
        
        WalletEntity toWalletEntity = new WalletEntity();
        toWalletEntity.setId(2);
        toWalletEntity.setBalance(new BigDecimal("5.5"));
    
        when(walletRepository.findById(2)).thenReturn(Optional.of(toWalletEntity));
        when(walletRepository.save(toWalletEntity)).thenReturn(toWalletEntity);
        
        WalletDto[] wallets = walletService
                .transferInternal(1, 2, new BigDecimal("2.2"))
                .toArray(WalletDto[]::new);
        assertEquals(wallets[0].getBalance(), new BigDecimal("7.8"));
        assertEquals(wallets[1].getBalance(), new BigDecimal("7.7"));
    }
    
    @Test
    public void deleteById_DeletedNoContent_ReturnsVoid() {
        when(walletRepository.existsById(1)).thenReturn(true);
        
        walletService.deleteById(1);
        
        verify(walletRepository).existsById(1);
        verify(walletRepository).deleteById(1);
    }
    
    @Test
    public void deleteById_NoneExists_ThrowsNoSuchEntity() {
        when(walletRepository.existsById(1)).thenReturn(false);
        
        assertThrows(NoSuchEntityException.class, () -> walletService.deleteById(1));
    }
}