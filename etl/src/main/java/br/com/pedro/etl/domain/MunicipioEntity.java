package br.com.pedro.etl.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "municipio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MunicipioEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "populacao")
    private Integer populacao;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "nome_normalizado")
    private String nomeNormalizado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private EstadoEntity estadoEntity;

}
