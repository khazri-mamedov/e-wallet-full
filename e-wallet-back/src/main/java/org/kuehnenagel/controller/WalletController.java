package org.kuehnenagel.controller;

import org.hibernate.validator.constraints.Range;
import org.kuehnenagel.model.dto.OpAmountDto;
import org.kuehnenagel.model.dto.TransferDto;
import org.kuehnenagel.model.dto.WalletDto;
import org.kuehnenagel.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

// CrossOrigin only for dev purposes
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/wallets")
@Validated
public class WalletController {
    private final WalletService walletService;
    
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }
    
    @GetMapping
    public ResponseEntity<Collection<WalletDto>> getAllWallets() {
        return ResponseEntity.ok(walletService.getAllWallets());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WalletDto> getWallet(@PathVariable int id) {
        return ResponseEntity.ok(walletService.getWallet(id));
    }
    
    @PostMapping
    public ResponseEntity<WalletDto> createWallet(@Valid @RequestBody WalletDto walletDto) {
        WalletDto createdWallet = walletService.createWallet(walletDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdWallet.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdWallet);
    }
    
    /**
     * Idempotent with 204 in case of proper deletion, and 404 in case of retry
     * @param id of resource to delete
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Range(min = 1, message = "Negative and zero forbidden") @PathVariable int id) {
        walletService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    /**
     * Using PATCH instead of PUT for partial update because operation is not idempotent
     */
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<WalletDto> withdrawAmount(
            @Valid @RequestBody OpAmountDto opAmountDto, @PathVariable int id) {
        return ResponseEntity.ok(walletService.withdrawFrom(opAmountDto.getAmount(), id));
    }
    
    /**
     * Using PATCH instead of PUT for partial update because operation is not idempotent
     */
    @PatchMapping("/{id}/add")
    public ResponseEntity<WalletDto> addAmount(
            @Valid @RequestBody OpAmountDto opAmountDto, @PathVariable int id) {
        return ResponseEntity.ok(walletService.addTo(opAmountDto.getAmount(), id));
    }
    
    /**
     * Transfers internally from one wallet to another for the same user
     */
    @PatchMapping("/transfer")
    public ResponseEntity<Collection<WalletDto>> transferInternally(
            @Valid @RequestBody TransferDto transferDto) {
        return ResponseEntity.ok(walletService.transferInternal(
                transferDto.getFromWalletId(),
                transferDto.getToWalletId(),
                transferDto.getAmount())
        );
    }
}
