package com.edacamo.mspersons.infrastructure.persistence.adapter;

import com.edacamo.mspersons.application.PublishClientEvent;
import com.edacamo.mspersons.domain.model.Client;
import com.edacamo.mspersons.domain.repositories.ClientRepository;
import com.edacamo.mspersons.dto.RegisterRequest;
import com.edacamo.mspersons.dto.RegisterResponse;
import com.edacamo.mspersons.infrastructure.persistence.entity.ClientEntity;
import com.edacamo.mspersons.infrastructure.persistence.jpa.JpaClientRepository;
import com.edacamo.mspersons.infrastructure.persistence.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {

    private final JpaClientRepository jpaClientRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByClienteId(String clientId) {
        ClientEntity clientEntity = jpaClientRepository.findByClienteId(clientId);
        if (clientEntity == null) {
            return Optional.empty();
        }
        return Optional.of(ClientMapper.toDomain(clientEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return jpaClientRepository.findAll()
                .stream()
                .map(ClientMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        if (!jpaClientRepository.existsById(id)) return 0;
        jpaClientRepository.deleteById(id);
        return 1;
    }

    @Override
    @Transactional
    public Client registerUser(RegisterRequest request) {
        ClientEntity entity = ClientMapper.toRequestSaveClient(request);
        ClientEntity save = jpaClientRepository.save(entity);
        return ClientMapper.toDomain(save);
    }

    @Override
    @Transactional
    public Client updateUser(RegisterRequest request) {
        ClientEntity entity = ClientMapper.toRequestSaveClient(request);

        //Se obtiene datos desde BD
        ClientEntity clientEntityDB = jpaClientRepository.findByClienteId(request.getUsuario());
        entity.setId(clientEntityDB.getId());

        ClientEntity update = jpaClientRepository.save(entity);
        return ClientMapper.toDomain(update);
    }

    @Override
    @Transactional
    public int deleteUser(String clientId) {
        ClientEntity clientEntity = jpaClientRepository.findByClienteId(clientId);

        if (!this.jpaClientRepository.existsById(clientEntity.getId())) return 0;

        // Esto elimina cliente + persona (por herencia JOINED)
        this.jpaClientRepository.delete(clientEntity);
        return 1;
    }
}
