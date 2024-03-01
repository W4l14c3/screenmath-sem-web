package br.com.alura.screnmatch.service;

public interface IConverteDados {
    //Interface que pede um metodo com acesso padrão(Public) a quem implementa-lo
    <T> T obterDados(String json, Class<T> classe);
}
