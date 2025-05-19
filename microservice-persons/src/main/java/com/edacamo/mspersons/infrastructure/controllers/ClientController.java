package com.edacamo.mspersons.infrastructure.controllers;

import com.edacamo.mspersons.application.ClientUseCase;
import com.edacamo.mspersons.domain.model.Client;
import com.edacamo.mspersons.dto.GetClientByIdRequest;
import com.edacamo.mspersons.dto.RegisterRequest;
import com.edacamo.mspersons.dto.RegisterResponse;
import com.edacamo.mspersons.dto.ResponseGeneric;
import com.edacamo.mspersons.infrastructure.persistence.entity.ClientEntity;
import com.edacamo.mspersons.common.exception.ResponseCode;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("clientes")
public class ClientController {

    private final ClientUseCase clientUseCase;

    @Operation(summary = "Listar todos los clientes", description = "Obtiene una lista de todos los clientes registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("")
    public ResponseEntity<ResponseGeneric<List<Client>>> list() {
        List<Client> clients = this.clientUseCase.findAll();

        ResponseGeneric<List<Client>> response = ResponseGeneric.success(
                HttpStatus.OK.value(),
                ResponseCode.SUCCESS,
                clients
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener cliente por ID", description = "Busca y retorna un cliente usando su ID.")
    @PostMapping("/id")
    public ResponseEntity<ResponseGeneric<Client>> findById(@RequestBody GetClientByIdRequest request) {
        log.info("Obteniendo el cliente con id {}", request.getClienteId());

        return this.clientUseCase.findByClienteId(request.getClienteId())
                .map(client -> {
                    ResponseGeneric<Client> response = ResponseGeneric.success(
                            HttpStatus.OK.value(),
                            ResponseCode.SUCCESS,
                            client
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new EmptyResultDataAccessException("Cliente con ID " + request.getClienteId() + " no encontrado", 1));
    }

    @Operation(summary = "Registrar nuevo cliente", description = "Registra un nuevo cliente en el sistema.")
    @PostMapping("/crear")
    public ResponseEntity<ResponseGeneric<RegisterResponse>> save(@RequestBody RegisterRequest request) {

       if(request != null) {
           try{
               RegisterResponse register = this.clientUseCase.registerUser(request);
               ResponseGeneric<RegisterResponse> response = ResponseGeneric.success(
                       HttpStatus.OK.value(),
                       ResponseCode.DATA_CREATED,
                       register
               );

               return ResponseEntity.ok(response);

           } catch (Exception ex) {
               // En caso de error, lanzamos una excepción personalizada para que sea atrapada por el GlobalExceptionHandler
               log.error("Error al registrar el cliente: ", ex);
               throw new RuntimeException("Error al registrar el cliente");  // Aquí puedes lanzar una excepción personalizada si lo prefieres
           }
       } else {
           // Si el cliente es nulo, lanzamos una excepción de validación
           log.error("Cliente nulo recibido");
           throw new IllegalArgumentException("Los datos del cliente no puede ser nulo");
       }
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente.")
    @PutMapping("/actualizar")
    public ResponseEntity<ResponseGeneric<RegisterResponse>> update(@RequestBody RegisterRequest request) {

        if(request != null) {
            try{
                RegisterResponse register = this.clientUseCase.updateUser(request);
                ResponseGeneric<RegisterResponse> response = ResponseGeneric.success(
                        HttpStatus.OK.value(),
                        ResponseCode.DATA_UPDATED,
                        register
                );

                return ResponseEntity.ok(response);

            } catch (Exception ex) {
                log.error("Error al actualizar el cliente: ", ex);
                throw new RuntimeException("Error al actualizar el cliente.");
            }
        } else {
            log.error("No se puede actualizar el cliente, por favor intente nuevamente");
            throw new IllegalArgumentException("Los datos del cliente no puede ser nulo.");
        }
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente existente usando su nombre de Usuario.")
    @DeleteMapping("/eliminar/{usuario}")
    public ResponseEntity<ResponseGeneric<RegisterResponse>> delete(@PathVariable String usuario) {
        try {
            RegisterResponse responseDelete = this.clientUseCase.deleteUserByClientId(usuario);
            ResponseGeneric<RegisterResponse> response = ResponseGeneric.success(
                    HttpStatus.OK.value(),
                    ResponseCode.DATA_DELETE,
                    responseDelete
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error al eliminar el cliente: ", ex);
            throw new RuntimeException("Error al eliminar el cliente.");
        }
    }
}
