package com.example.testefadesp.repositories;

import com.example.testefadesp.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long>, JpaSpecificationExecutor<Pagamento> {

}
