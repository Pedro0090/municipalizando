package br.com.pedro.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sigla")
    private String sigla;

    @Column(name = "nome_normalizado")
    private String nomeNormalizado;

    @OneToMany(mappedBy = "estado", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MunicipioEntity> municipios;
}
