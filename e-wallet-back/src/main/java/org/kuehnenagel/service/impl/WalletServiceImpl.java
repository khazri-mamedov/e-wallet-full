package org.kuehnenagel.service.impl;

import org.kuehnenagel.model.WalletEntity;
import org.kuehnenagel.model.dto.WalletDto;
import org.kuehnenagel.model.mapper.WalletMapper;
import org.kuehnenagel.repository.WalletRepository;
import org.kuehnenagel.service.WalletService;
import org.kuehnenagel.util.BalanceUnderflowException;
import org.kuehnenagel.util.NoSuchEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    
    public WalletServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }
    
    @Override
    public Collection<WalletDto> getAllWallets() {
        return walletRepository.findAll().stream()
                .map(walletMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WalletDto getWallet(int walletId) {
        return walletMapper.toDto(getEntityOrThrow(walletId));
    }
    
    @Override
    public WalletDto createWallet(final WalletDto walletDto) {
        logger.info("Creating wallet: {}", walletDto);
        
        WalletEntity walletEntity = walletMapper.toEntityIgnoreId(walletDto);
        return walletMapper.toDto(walletRepository.save(walletEntity));
    }
    
    /**
     * In case of multiple concurrent withdraws, prefer to use context locking
     * Be aware of PESSIMISTIC_LOCK on found entity
     * <b>Assumption is only one user at a time</b>
     */
    @Override
    @Transactional
    public WalletDto withdrawFrom(BigDecimal amount, final int walletId) {
        logger.info("Withdraw from {}, amount {}", walletId, amount);
        return withdrawFromWallet(amount, walletId);
    }
    
    /**
     * Transaction isolation or context lock is redundant because adding doesn't break any constraint
     * (except overflow). <b>Without transactional PESSIMISTIC_LOCK doesn't work</b>
     */
    @Override
    public WalletDto addTo(final BigDecimal amount, final int walletId) {
        logger.info("Adding to {}, amount {}", walletId, amount);
        return addToWallet(amount, walletId);
    }
    
    /**
     * Self transfer is allowed for testing purposes (integrity)
     */
    @Override
    @Transactional
    public Collection<WalletDto> transferInternal(int fromWallet, int toWallet, BigDecimal amount) {
        logger.info("Transfer from wallet id {}, to wallet id {} amount {}", fromWallet, toWallet, amount);
        return List.of(
                withdrawFromWallet(amount, fromWallet),
                addToWallet(amount, toWallet)
        );
    }
    
    
    @Override
    public void deleteById(Integer id) {
        logger.info("Deleting wallet with id {}", id);
        if (walletRepository.existsById(id)) {
            walletRepository.deleteById(id);
            return;
        }
        throw new NoSuchEntityException(id);
    }
    
    /**
     * Redundant refactoring for eliminating unnecessary public to public call
     */
    private WalletDto withdrawFromWallet(BigDecimal amount, final int walletId) {
        WalletEntity walletEntity = getEntityOrThrow(walletId);
        if (walletEntity.getBalance().compareTo(amount) < 0) {
            throw new BalanceUnderflowException(walletEntity.getBalance());
        }
        walletEntity.setBalance(walletEntity.getBalance().subtract(amount));
        return walletMapper.toDto(walletRepository.save(walletEntity));
    }
    
    /**
     * Redundant refactoring for eliminating unnecessary public to public call
     */
    private WalletDto addToWallet(final BigDecimal amount, final int walletId) {
        WalletEntity walletEntity = getEntityOrThrow(walletId);
        walletEntity.setBalance(walletEntity.getBalance().add(amount));
        return walletMapper.toDto(walletRepository.save(walletEntity));
    }
    
    /**
     * PESSIMISTIC_LOCK works under the hood
     */
    private WalletEntity getEntityOrThrow(final int walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new NoSuchEntityException(walletId));
    }
}
