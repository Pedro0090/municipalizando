package br.com.pedro.etl.respository;

import br.com.pedro.etl.domain.MunicipioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends JpaRepository<MunicipioEntity, Long> {
}
