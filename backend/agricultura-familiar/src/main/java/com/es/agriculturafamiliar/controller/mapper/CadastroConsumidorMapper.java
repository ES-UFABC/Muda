package com.es.agriculturafamiliar.controller.mapper;

import com.es.agriculturafamiliar.dto.request.CadastroConsumidorRequest;
import com.es.agriculturafamiliar.dto.response.CadastroConsumidorResponse;
import com.es.agriculturafamiliar.models.domain.cadastroconsumidor.CadastroConsumidorDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CadastroConsumidorMapper {
    CadastroConsumidorDomain toModel(CadastroConsumidorRequest requestDTO);
    CadastroConsumidorResponse toDto(CadastroConsumidorDomain domainOut);
}