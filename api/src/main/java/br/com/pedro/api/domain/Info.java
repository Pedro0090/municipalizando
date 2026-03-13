package br.com.pedro.api.domain;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Info {

    private Map<String,String> atribuicoes;
}
