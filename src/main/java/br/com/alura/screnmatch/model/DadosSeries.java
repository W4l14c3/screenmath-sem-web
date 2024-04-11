package br.com.alura.screnmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true ) //Ignora as propriedades desconhecidas
public record DadosSeries(@JsonAlias("Title") String titulo,
                          @JsonAlias("totalSeasons") Integer totalTemporadas,
                          @JsonAlias("imdbRating") String avaliacao) {
}
