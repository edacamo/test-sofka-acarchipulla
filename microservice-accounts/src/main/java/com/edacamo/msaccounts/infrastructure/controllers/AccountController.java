package com.edacamo.msaccounts.infrastructure.controllers;

import com.edacamo.msaccounts.application.*;
import com.edacamo.msaccounts.domain.model.Account;
import com.edacamo.msaccounts.dto.AccountRegisterReponse;
import com.edacamo.msaccounts.common.exception.ResponseCode;
import com.edacamo.msaccounts.dto.AccountRegisterRequest;
import com.edacamo.msaccounts.dto.ResponseGeneric;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.AccountMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("cuentas")
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "Operaciones relacionadas con la gestión de cuentas")
public class AccountController {

    private final AccountUseCase accountUseCase;

    @Operation(summary = "Listar cuentas", description = "Obtiene un listado de todas las cuentas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<ResponseGeneric<List<AccountRegisterReponse>>> list() {
        List<Account> clients = this.accountUseCase.getAll();
        List<AccountRegisterReponse> list = new ArrayList<>();

        if (!clients.isEmpty()) {
            for (Account client : clients) {
                AccountRegisterReponse accountRegisterReponse = new AccountRegisterReponse();
                accountRegisterReponse.setNumerCuenta(client.getNumeroCuenta());
                accountRegisterReponse.setTipo(client.getTipo());
                accountRegisterReponse.setSaldoInicial(client.getSaldoInicial());
                accountRegisterReponse.setEstado(client.getEstado());
                accountRegisterReponse.setCliente(client.getClient().getNombre());
                list.add(accountRegisterReponse);
            }
        }

        ResponseGeneric<List<AccountRegisterReponse>> response = ResponseGeneric.success(
                HttpStatus.OK.value(),
                ResponseCode.SUCCESS,
                list
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar cuenta", description = "Registra una nueva cuenta en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cuenta nula"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/registrar")
    public ResponseEntity<ResponseGeneric<AccountRegisterReponse>> createAccount(@RequestBody AccountRegisterRequest request) {
        if(request != null) {
            try{
                Account account = AccountMapper.toDomainRequest(request);
                Account created = this.accountUseCase.create(account);
                AccountRegisterReponse accountRegisterReponse = AccountMapper.toResponse(created);

                ResponseGeneric<AccountRegisterReponse> response = ResponseGeneric.success(
                        HttpStatus.OK.value(),
                        ResponseCode.DATA_CREATED,
                        accountRegisterReponse
                );

                return ResponseEntity.ok(response);

            } catch (Exception ex) {
                // En caso de error, lanzamos una excepción personalizada para que sea capturada por el GlobalExceptionHandler
                log.error("Error al registrar la cuenta: ", ex);
                throw new RuntimeException(ex.getMessage().isEmpty() ? "Error al registrar la cuenta" : ex.getMessage());
            }
        } else {
            // Si la cuenta es nulo, lanzamos una excepción de validación
            log.error("Cuenta nula no puede ser nula");
            throw new IllegalArgumentException("Los datos de la cuenta no puede ser nulo");
        }
    }

    @Operation(summary = "Actualizar cuenta", description = "Actualiza los datos de una cuenta existente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cuenta nula"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResponseGeneric<AccountRegisterReponse>> updateAccount(@RequestBody AccountRegisterRequest request, @PathVariable Long id) {
        if(request != null) {
            try{
                Account account = AccountMapper.toDomainRequest(request);
                Account update = this.accountUseCase.update(id, account);

                AccountRegisterReponse accountRegisterReponse = AccountMapper.toResponse(update);

                ResponseGeneric<AccountRegisterReponse> response = ResponseGeneric.success(
                        HttpStatus.OK.value(),
                        ResponseCode.DATA_UPDATED,
                        accountRegisterReponse
                );

                return ResponseEntity.ok(response);

            } catch (Exception ex) {
                log.error("Error al actualizar la cuenta: ", ex);
                throw new RuntimeException(ex.getMessage().isEmpty() ? "Error al actualizar la cuenta" : ex.getMessage());
            }
        } else {
            // Si la cuenta es nulo, lanzamos una excepción de validación
            log.error("Datos de la cuenta no puede ser nula");
            throw new IllegalArgumentException("Los datos de la cuenta no puede ser nulo");
        }
    }

    @Operation(summary = "Eliminar cuenta", description = "Elimina una cuenta del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ResponseGeneric<String>> deleteAccount(@PathVariable Long id) {
        try{
            this.accountUseCase.delete(id);
            ResponseGeneric<String> response = ResponseGeneric.success(
                    HttpStatus.OK.value(),
                    ResponseCode.DATA_DELETE,
                    "Cuenta eliminada correctamente"
            );

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Error al eliminar la cuenta: ", ex);
            throw new RuntimeException(ex.getMessage().isEmpty() ? "Error al eliminar la cuenta" : ex.getMessage());
        }
    }
}
