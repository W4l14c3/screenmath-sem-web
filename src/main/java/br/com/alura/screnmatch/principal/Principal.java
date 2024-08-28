package br.com.alura.screnmatch.principal;

import br.com.alura.screnmatch.model.DadosSeries;
import br.com.alura.screnmatch.model.DadosTemporada;
import br.com.alura.screnmatch.model.Episodio;
import br.com.alura.screnmatch.service.ConsumoAPI;
import br.com.alura.screnmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    //Constants.
    private final String EDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=36ee2d78";
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    //Instance converse of json to class generic
    private final ConverteDados conversor = new ConverteDados();



    public void exibeMenu(){
        System.out.println("Digite o nome da série para buscar");
        var nomeSerie  = leitura.nextLine();


        var json = consumoApi.obterDados(EDERECO + nomeSerie.replace(" ", "_") + API_KEY);
        //Converter o json na Record Dados_Series, create an object dadosSe with the search date.
        DadosSeries dadosSeries = conversor.obterDados(json, DadosSeries.class);
        System.out.println(dadosSeries);//

        // Lista que vai ser carregada com as temporadas
		List<DadosTemporada> temporadas = new ArrayList<>();

        //Necessario criar um try para caso a buscar retorne NullPointerException.
        //for vai realizar buscas de temporadas de acordo com a quantidade de temporadas na Record dadosSe
        try{
            for(int i = 1; i <= dadosSeries.totalTemporadas(); i ++){
                // vai buscar uma temporada diferente a cada incremento, e carrega a response na var json.
                json = consumoApi.obterDados(EDERECO + nomeSerie.replace(" ", "_") + "&season=" + i + API_KEY);
                // Tenta converter a busca para a classe daodos temporada a cada incremento
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                //adiciona um objeto dadosTemparada a cada incremento.
                temporadas.add(dadosTemporada);

                //System.out.println(dadosTemporada);
            }
        } catch (NullPointerException ex){
            System.out.println("Não foi possivel encontrar a Serie...");
        }


        //ForEach que vai percorrer a lista temporadas, e faz um callBack method reference.
		temporadas.forEach(System.out::println);
        //Exemplo usando Lambda.

        //temporadas.forEach(t -> System.out.println(t));

//        for (int i = 0; i < dadosSeries.totalTemporadas(); i++){
//            List<DadosEpisodio> episodeosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodeosTemporada.size(); j ++){
//                System.out.println(episodeosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .forEach(System.out::println);

        //Stream cria uma coleção de dados.
//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                //Cria uma stream dos episodios de cada temporada.
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
                //.toList();// Cria uma lista imutavel

        //Exibe um top 5 dos episodios.
//        System.out.println("\nTop 10 episodios");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("\nOrdenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("\nLimite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("\nMapeamento " + e))
//                .forEach(System.out::println);
//        System.out.println("\n");


        System.out.println("\nEpisodios e suas temporadas: ");
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()//Stream dos episodios de cada temporada.
                        .map(d -> new Episodio(t.numero(), d))//Stream de novos objetos Episodios com o numero da temporada
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);

        //Make a search with snippet from the title
        System.out.println("Digite um trecho do titulo do episódeo");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase())) // filter all episodes titles what contains snippet of title
                .findFirst(); //Return the first recurrence of filtered element
        if(episodioBuscado.isPresent()){
            System.out.println("Episódeo encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get());
        }
        else {
            System.out.println("Episódeo não encontrado!");
        }

//        //Filtra os episodios a partir do ano.
//        System.out.println("A partir de que ano você deseja ver os episódios?");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null &&  e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                "Data lançamento: " + e.getDataLancamento().format(formatador)
//                        ));
        //A map with the average rating of all episodes

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e ->  e.getAvaliacao() > 0.0) //Eliminate rating equal 0.0
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics  est = episodios.stream()
                .filter(e ->  e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.printf("""
                Media: %.1f
                Melhor episódio: %.1f
                Pior episódio: %.1f
                Quantidade: %d
                %n""", est.getAverage(), est.getMax(), est.getMin(), est.getCount());

    }

}
