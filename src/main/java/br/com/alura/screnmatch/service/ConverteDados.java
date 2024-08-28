package br.com.alura.screnmatch.service;

import br.com.alura.screnmatch.model.DadosSeries;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados{
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * vai converter um json para {@code classe} passada como par&acirc;metro <p>
     * T para classes genericas.
     * @return convers&atilde;o do json para uma classe generica.
     */


    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
