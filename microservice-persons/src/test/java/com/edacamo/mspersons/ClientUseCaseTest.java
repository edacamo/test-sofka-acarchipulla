package com.edacamo.mspersons;

import com.edacamo.mspersons.application.ClientUseCase;
import com.edacamo.mspersons.application.PublishClientEvent;
import com.edacamo.mspersons.domain.model.Client;
import com.edacamo.mspersons.domain.repositories.ClientRepository;
import com.edacamo.mspersons.dto.RegisterRequest;
import com.edacamo.mspersons.dto.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PublishClientEvent publishClientEvent;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientUseCase clientUseCase;

    @Test
    void shouldRegisterUserSuccessfully() {
        log.info("[Testing Unitary Client] Registering user successfully");

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsuario("jlema");
        request.setPassword("password");
        request.setNombre("Jose lema");
        request.setGenero("M");
        request.setEdad(30);
        request.setIdentificacion("1234567890");
        request.setDireccion("Otavalo sn y principal");
        request.setTelefono("098254785");

        // Simula que el usuario no existe
        when(clientRepository.findByClienteId("jlema")).thenReturn(Optional.empty());

        // Simula el resultado de codificar la contraseña
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        // Simula que el cliente fue registrado y devuelto por el repositorio
        Client savedClient = new Client();
        savedClient.setClienteId("jlema");
        savedClient.setNombre("Jose lema");
        savedClient.setGenero("M");
        savedClient.setEdad(30);
        savedClient.setIdentificacion("1234567890");
        savedClient.setDireccion("Otavalo sn y principal");
        savedClient.setTelefono("098254785");
        savedClient.setEstado(true);
        savedClient.setContrasenia("hashedPassword");

        when(clientRepository.registerUser(any(RegisterRequest.class))).thenReturn(savedClient);

        // Act
        RegisterResponse response = clientUseCase.registerUser(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Usuario registrado correctamente", response.getMessage());

        // Captura el cliente pasado al método publish
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(publishClientEvent).publishClientCreated(clientCaptor.capture());

        Client publishedClient = clientCaptor.getValue();
        assertNotNull(publishedClient);
        assertEquals("jlema", publishedClient.getClienteId());
        assertEquals("Jose lema", publishedClient.getNombre());
        assertEquals("M", publishedClient.getGenero());
        assertEquals(30, publishedClient.getEdad());
        assertEquals("1234567890", publishedClient.getIdentificacion());
        assertEquals("Otavalo sn y principal", publishedClient.getDireccion());
        assertEquals("098254785", publishedClient.getTelefono());
        assertEquals("hashedPassword", publishedClient.getContrasenia());
        assertTrue(publishedClient.getEstado());

        // Verifica también que se hayan invocado los métodos esperados
        verify(clientRepository).findByClienteId("jlema");
        verify(passwordEncoder).encode("password");
        verify(clientRepository).registerUser(any(RegisterRequest.class));
    }

    @Test
    void shouldNotRegisterUserIfAlreadyExists() {
        log.info("[Testing Unitary Client] Trying to register an existing user");

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsuario("jlema");
        request.setPassword("password");

        // Simula que el usuario ya existe en la base
        when(clientRepository.findByClienteId("jlema")).thenReturn(Optional.of(new Client()));

        // Act
        RegisterResponse response = clientUseCase.registerUser(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("El usuario jlema ya existe.", response.getMessage());

        // Verifica que no se intenta codificar contraseña ni registrar
        verify(clientRepository).findByClienteId("jlema");
        verify(passwordEncoder, never()).encode(anyString());
        verify(clientRepository, never()).registerUser(any());
        verify(publishClientEvent, never()).publishClientCreated(any());
    }

}
