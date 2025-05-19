package com.edacamo.msaccounts.application;

import com.edacamo.msaccounts.domain.model.Movements;
import com.edacamo.msaccounts.domain.repositories.MovementsRepository;
import com.edacamo.msaccounts.dto.MovementResponse;
import com.edacamo.msaccounts.dto.TransactionReportRequest;
import com.edacamo.msaccounts.dto.TransactionReportResponse;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.MovementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MovementsUseCase {

    private final MovementsRepository repository;

    public Movements create(Movements entity){

        return this.repository.create(entity);
    }

    public Movements update(Long id, Movements entity){
        entity.setId(id);
        return this.repository.update(id, entity);
    }

    public void delete(Long id){
        int deleted =  this.repository.deleteById(id);
        if (deleted == 0) {
            throw new NoSuchElementException("Movimiento no encontrado o ya eliminado");
        }
    }

    public List<TransactionReportResponse> getReport(TransactionReportRequest request) {
        return this.repository.getMovementsReport(request);
    }

    public List<MovementResponse> getMovimientosCuenta(Long id) {
        List<Movements> movements = this.repository.getMovimientosByCuenta(id);

        List<MovementResponse> responses = new ArrayList<>();

        for (Movements movement : movements) {
            responses.add(MovementMapper.toResponse(movement));
        }

        return responses;
    }
}
