package br.com.pedro.api.respository;

import br.com.pedro.api.domain.EstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<EstadoEntity, Long> {

    List<EstadoEntity> findByNomeNormalizado(String nomeNormalizado);
}
