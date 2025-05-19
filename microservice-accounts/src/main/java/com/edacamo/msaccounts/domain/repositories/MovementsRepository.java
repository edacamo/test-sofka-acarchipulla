package com.edacamo.msaccounts.domain.repositories;

import com.edacamo.msaccounts.domain.model.Movements;
import com.edacamo.msaccounts.dto.TransactionReportRequest;
import com.edacamo.msaccounts.dto.TransactionReportResponse;

import java.util.List;

public interface MovementsRepository {

    Movements create(Movements movement);

    List<Movements> getMovimientosByCuenta(Long movimientoId);

    int deleteById(Long movimientoId);

    Movements update(Long id, Movements movimientoDetails);

    List<TransactionReportResponse> getMovementsReport(TransactionReportRequest request);
}
