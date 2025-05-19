package com.edacamo.mspersons.application;

import com.edacamo.mspersons.domain.model.Client;
import com.edacamo.mspersons.domain.repositories.ClientRepository;
import com.edacamo.mspersons.dto.RegisterRequest;
import com.edacamo.mspersons.dto.RegisterResponse;
import com.edacamo.mspersons.infrastructure.persistence.entity.ClientEntity;
import com.edacamo.mspersons.infrastructure.persistence.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientUseCase {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final PublishClientEvent publishClientEvent;

    public Optional<Client> findByClienteId(String clientId){
        return this.clientRepository.findByClienteId(clientId);
    }

    public List<Client> findAll(){
        return this.clientRepository.findAll();
    }

    public int deleteClientById(Long id){
        return this.clientRepository.deleteById(id);
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest){

        if (clientRepository.findByClienteId(registerRequest.getUsuario()).isPresent()) {
            return new RegisterResponse(String.format("El usuario %s ya existe.", registerRequest.getUsuario()), false);
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(hashedPassword);
        registerRequest.setEstado(true);

        Client saved = this.clientRepository.registerUser(registerRequest);
        this.publishClientEvent.publishClientCreated(saved);//Produce el mensaje Kafka

       return new RegisterResponse("Usuario registrado correctamente", true);
    }

    public RegisterResponse updateUser(RegisterRequest request){
        //Se obtiene datos desde BD
        Optional<Client> clientDB = this.clientRepository.findByClienteId(request.getUsuario());

        if (clientDB.isEmpty()) return new RegisterResponse(
                String.format("La información del usuario %s no existe.", request.getUsuario()),
                false
        );

        request.setPassword(request.getPassword().isEmpty() ?
                clientDB.get().getContrasenia() :
                passwordEncoder.encode(request.getPassword()));

        request.setEstado(clientDB.get().getEstado());

        Client updateUser = this.clientRepository.updateUser(request);

        return new RegisterResponse("La información del usuario fue actualizada correctamente.", true);
    }

    public RegisterResponse deleteUserByClientId(String clientId){

        Optional<Client> client = this.clientRepository.findByClienteId(clientId);
        if (client.isEmpty()) {
            return new RegisterResponse("El cliente no existe.", false);
        }

        Client clientDB = client.get();

        int deleted = this.clientRepository.deleteUser(clientId);

        if (deleted == 0) {
            return new RegisterResponse("El cliente no existe o ya fue eliminado.", false);
        }

        //Notifica mensaje de eliminacion
        this.publishClientEvent.publishClientDeleted(clientId);

        return new RegisterResponse("Cliente eliminado correctamente.", true);
    }
}
