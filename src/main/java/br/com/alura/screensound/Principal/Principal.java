package br.com.alura.screensound.Principal;

import br.com.alura.screensound.Model.Artista;
import br.com.alura.screensound.Model.Musica;
import br.com.alura.screensound.Model.TipoArtista;
import br.com.alura.screensound.Repository.ArtistaRepository;
import br.com.alura.screensound.Service.ConsultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class Principal {
    private final ArtistaRepository repositorio;
    private Scanner leitura = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao!=9){
            var menu = """
                    ########### Screen Sound Music ###########
                    
                    1- Cadastrar Artistas
                    2- Cadastrar Musicas
                    3- Listar Musicas
                    4- Listar Musicas por Artistas
                    5- Pesquisas dados sobre um artista
                    
                    9- Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao){
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtistas();
                    break;
                case 5:
                    pesquisarDadosPorArtista();
                    break;
                case 9:
                    System.out.println("Finalizando Aplicação");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        }

    }

    private void pesquisarDadosPorArtista() {
        System.out.println("Sobre qual artista deseja pesquisar dados?");
        var nome = leitura.nextLine();
        var resposta = ConsultaChatGPT.obterInformacao(nome);
        System.out.println(resposta.trim());
    }

    private void buscarMusicasPorArtistas() {
        System.out.println("Qual Artista deseja pesquisar as musicas");
        var nome = leitura.nextLine();
        List<Musica> musicas = repositorio.buscaMusicasPorArtista(nome);
        musicas.forEach(System.out::println);
    }

    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a -> a.getMusicas().forEach(System.out::println));
    }

    private void cadastrarMusicas() {
        System.out.println("Cadastrar musica de qual artista?");
        var nome = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nome);
        if(artista.isPresent()){
            System.out.println("Digite o nome da musica: ");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());
        }else{
            System.out.println("Artista não encontrado");
        }
    }

    private void cadastrarArtistas() {
        var cadastrarNovo = "S";

        while (cadastrarNovo.equalsIgnoreCase("s")){
            System.out.println("Digite o nome do Artista");
            var nome = leitura.nextLine();
            System.out.println("Informe o Tipo do Artista: (solo, dupla, banda)");
            var tipo = leitura.nextLine();

            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
            Artista artista = new Artista(nome, tipoArtista);
            repositorio.save(artista);
            System.out.println("Cadastrar um novo artista? S/N");
            cadastrarNovo = leitura.nextLine();
        }

    }
}
