package br.com.pedro.api.respository;

import br.com.pedro.api.domain.MunicipioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipioRepository extends JpaRepository<MunicipioEntity, Long> {

    List<MunicipioEntity> findByNomeNormalizado(String nomeNormalizado);

    List<MunicipioEntity> findByNomeNormalizadoAndEstadoSigla(String nomeNormalizado, String sigla);

    List<MunicipioEntity> findByNomeNormalizadoAndEstadoNomeNormalizado(String MunicipioNomeNormalizado,
                                                                        String EstadoNomeNormalizado);

    List<MunicipioEntity> findByEstadoId(Long estadoId);
}
