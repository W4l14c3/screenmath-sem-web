package br.com.alura.screnmatch.service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    public String obterDados(String endereco) {
        HttpClient client = HttpClient.newHttpClient();//É quem inicia a comunicação com o server.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client
                    //O metodo send pode gerar uma IOException, pois é um fluxo de dados
                    //E também pode gerar uma Interrupção.
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        // retorna a requisição em json
        return response.body();
    }

}
