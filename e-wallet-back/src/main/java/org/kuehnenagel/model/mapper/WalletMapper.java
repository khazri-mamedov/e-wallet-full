package org.kuehnenagel.model.mapper;

import org.kuehnenagel.model.WalletEntity;
import org.kuehnenagel.model.dto.WalletDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletDto toDto(WalletEntity walletEntity);
    WalletEntity toEntity(WalletDto walletDto);
    
    @Mapping(ignore = true, target = "id")
    WalletEntity toEntityIgnoreId(WalletDto walletDto);
}
