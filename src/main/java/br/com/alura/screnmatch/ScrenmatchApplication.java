package br.com.alura.screnmatch;

import br.com.alura.screnmatch.model.DadosSeries;
import br.com.alura.screnmatch.service.ConsumoAPI;
import br.com.alura.screnmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScrenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=36ee2d78");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSeries dados = conversor.obterDados(json, DadosSeries.class);
	}
}
