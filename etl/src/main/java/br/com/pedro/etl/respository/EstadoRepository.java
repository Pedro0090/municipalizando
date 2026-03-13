package br.com.pedro.etl.respository;

import br.com.pedro.etl.domain.EstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<EstadoEntity, Long> {
}
