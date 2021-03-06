package com.es.agriculturafamiliar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import com.es.agriculturafamiliar.entity.ConfirmacaoCadastro;
import com.es.agriculturafamiliar.entity.User;
import com.es.agriculturafamiliar.entity.produtor.Produtor;
import com.es.agriculturafamiliar.enums.TipoProdutor;
import com.es.agriculturafamiliar.exception.ResourceNotFoundException;
import com.es.agriculturafamiliar.repository.ProdutorRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class ProdutorServiceTests {

    @InjectMocks
    private ProdutorService produtorService;

    @Mock
    private ProdutorRepository produtorRepository;

    @Mock
    private JwtUserDetailsManager customUserDetailsService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private ConfirmacaoCadastroService confirmacaoCadastroService;


    @Test
    public void saveProdutor_shouldReturnSavedProdutor_whenSuccessful(){
        User user = new User();
        Produtor produtor = new Produtor();
        produtor.setCpfOuCnpj("43292043742");
        produtor.setNome("Prod Teste");
        produtor.setNomeFantasia("Prod Teste");
        produtor.setRegiaoDeProducao("região");
        produtor.setAtendeNoEnderecoDeProducao(true);
        produtor.setCadastroEntidade(true);
        produtor.setTipoProdutor(TipoProdutor.COLETIVO);
        produtor.setRegistroOuCertificacao(false);
        produtor.setAgroecologico(true);
        produtor.setCertificacaoAgroecologico(true);
        produtor.setOrganico("SIM");
        produtor.setGeolocalizacao("");
        produtor.getTelefones().addAll(Arrays.asList("1234512", "1574869"));

        Produtor returnedSavedProdutor = new Produtor();
        returnedSavedProdutor.setCpfOuCnpj("43292043742");
        returnedSavedProdutor.setNome("Prod Teste");
        returnedSavedProdutor.setNomeFantasia("Prod Teste");
        returnedSavedProdutor.setRegiaoDeProducao("região");
        returnedSavedProdutor.setAtendeNoEnderecoDeProducao(true);
        returnedSavedProdutor.setCadastroEntidade(true);
        returnedSavedProdutor.setTipoProdutor(TipoProdutor.COLETIVO);
        returnedSavedProdutor.setRegistroOuCertificacao(false);
        returnedSavedProdutor.setAgroecologico(true);
        returnedSavedProdutor.setCertificacaoAgroecologico(true);
        returnedSavedProdutor.setOrganico("SIM");
        returnedSavedProdutor.setGeolocalizacao("");
        returnedSavedProdutor.getTelefones().addAll(Arrays.asList("1234512", "1574869"));
        returnedSavedProdutor.setUser(user);

        when(customUserDetailsService.createUser(any())).thenReturn(user);
        when(produtorRepository.save(any(Produtor.class))).thenReturn(returnedSavedProdutor);
        when(confirmacaoCadastroService.createConfirmacaoCadastro()).thenReturn(ConfirmacaoCadastro.builder().codigo("123456").build());

        var savedProdutor = produtorService.saveProdutor(produtor, user);

        assertEquals(produtor, savedProdutor);
    }

    @Test
    public void searchProdutorById_shouldThrowsObjectNotFoundException_whenResourceIsNotFound(){

        when(produtorRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtorService.findProdutorById(1L));
    }

    @Test
    public void findProdutorById_shouldReturnSavedProdutor_whenSuccessful(){
        Produtor produtor = new Produtor();
        produtor.setCpfOuCnpj("43292043742");
        produtor.setNome("Prod Teste");
        produtor.setNomeFantasia("Prod Teste");
        produtor.setRegiaoDeProducao("região");
        produtor.setAtendeNoEnderecoDeProducao(true);
        produtor.setCadastroEntidade(true);
        produtor.setTipoProdutor(TipoProdutor.COLETIVO);
        produtor.setRegistroOuCertificacao(false);
        produtor.setAgroecologico(true);
        produtor.setCertificacaoAgroecologico(true);
        produtor.setOrganico("SIM");
        produtor.setGeolocalizacao("");
        produtor.getTelefones().addAll(Arrays.asList("1234512", "1574869"));

        when(produtorRepository.findById(any(Long.class))).thenReturn(Optional.of(produtor));

        var returnedProdutor = produtorService.findProdutorById(1L);

        assertEquals(produtor.getNome(), returnedProdutor.getNome());
        assertEquals(produtor.getNomeFantasia(), returnedProdutor.getNomeFantasia());
        assertEquals(produtor.getCpfOuCnpj(), returnedProdutor.getCpfOuCnpj());
        assertEquals(produtor.getTelefones(), returnedProdutor.getTelefones());
    }

    @Test
    public void deleteProdutorById_shouldThrowObjectNotFoundException_whenResourceIsNotFound(){
        when(produtorRepository.findById(any(Long.class)))
                .thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> produtorService.deleteProdutorById(1L));
    }

    @Test
    public void updateProdutor_shouldThrowObjectNotFoundException_whenResourceIsNotFound() {
        Produtor produtor = new Produtor();
        produtor.setCpfOuCnpj("43292043742");
        produtor.setNome("Atualizado");

        assertThrows(ResourceNotFoundException.class, () -> produtorService.updateProdutor(produtor, 1345L));
    }

    @Test
    public void updateProdutor_shouldUpdateEntity_whenSuccessful() {
        var produtor = new Produtor();
        produtor.setId(1L);
        produtor.setOrganico("SIM");
        produtor.setNomeFantasia("teste");
        produtor.setRegiaoDeProducao("");
        produtor.setAtendeNoEnderecoDeProducao(true);
        produtor.setCadastroEntidade(true);
        produtor.setTipoProdutor(TipoProdutor.INDIVIDUAL);
        produtor.setRegistroOuCertificacao(true);
        produtor.setCertificacaoAgroecologico(true);
        produtor.setAgroecologico(true);
        produtor.setOrganico("SIM");
        produtor.setEntidadesAtendidas(null);
        produtor.setTiposProducao(null);
        produtor.setTelefones(null);
        produtor.setFormasPagamento(null);
        produtor.setRegistrosOuCertificacoes(null);
        produtor.setPaginasExternas(null);

        when(produtorRepository.findById(any(Long.class))).thenReturn(Optional.of(produtor));
        when(produtorRepository.save(any())).thenReturn(produtor);

        var response = produtorService.updateProdutor(produtor, 1L);
        assertEquals(response.getId(), produtor.getId());
    }

    @Test
    public void updateProdutor_shouldDeleteEntity_whenSuccessful(){
        when(produtorRepository.findById(any(Long.class))).thenReturn(Optional.of(new Produtor()));
        doNothing().when(produtorRepository).delete(any());
        produtorService.deleteProdutorById(1L);
        verify(produtorRepository, times(1)).delete(any());
    }

    @Test
    public void findAll_shouldReturnProdutorList_whenSuccessful(){
        when(produtorRepository.findAll()).thenReturn(new ArrayList<Produtor>());
        var response = produtorService.findAll();
        assertEquals(response.size(), 0);
        assertNotNull(response);
    }
}
