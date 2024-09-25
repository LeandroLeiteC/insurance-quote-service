package br.com.itau.insurance_quote.infrastructure.quote.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteJpaEntity, Long> {

    @Query("Select nextval(pg_get_serial_sequence('quotes', 'id'))")
    Long nextVal();
}
