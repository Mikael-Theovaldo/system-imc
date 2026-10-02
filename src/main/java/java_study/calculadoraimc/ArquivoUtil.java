package java_study.calculadoraimc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArquivoUtil {

    public static final String NOME_ARQUIVO = "dados_pessoas.txt";

    private ArquivoUtil() { }

    // Grava TODA a lista no arquivo (sobrescreve o conteúdo anterior).
    public static void salvar(List<Pessoa> pessoas) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(NOME_ARQUIVO, StandardCharsets.UTF_8))) {
            for (Pessoa p : pessoas) {
                String linha = String.format(Locale.US, "%d,%s,%.2f,%.2f,%.2f",
                        p.getId(), p.getNome(), p.getAltura(), p.getPeso(), p.getImc());
                writer.write(linha);
                writer.newLine();
            }
        }
    }

    // Lê o arquivo e devolve a lista de pessoas. Se o arquivo não existir, devolve lista vazia.
    public static List<Pessoa> carregar() throws IOException {
        List<Pessoa> pessoas = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);

        if (!arquivo.exists()) {
            return pessoas;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(arquivo, StandardCharsets.UTF_8))) {
            String linha;
            int numeroLinha = 0;

            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                if (linha.isBlank()) {
                    continue; // ignora linhas em branco
                }
                try {
                    pessoas.add(converterLinha(linha));
                } catch (IllegalArgumentException e) {
                    // Linha corrompida não derruba o programa: apenas é ignorada
                    System.err.println("Linha " + numeroLinha + " ignorada: " + e.getMessage());
                }
            }
        }
        return pessoas;
    }

    // Transforma uma linha CSV em um objeto Pessoa.
    private static Pessoa converterLinha(String linha) {
        String[] campos = linha.split(",");
        if (campos.length < 4) {
            throw new IllegalArgumentException("formato inválido: " + linha);
        }
        // NumberFormatException (campo não numérico) é um tipo de IllegalArgumentException
        int id = Integer.parseInt(campos[0].trim());
        String nome = campos[1].trim();
        double altura = Double.parseDouble(campos[2].trim());
        double peso = Double.parseDouble(campos[3].trim());
        // O IMC (campo 5) é ignorado na leitura: é recalculado pela classe Pessoa
        return new Pessoa(id, nome, altura, peso);
    }
}
