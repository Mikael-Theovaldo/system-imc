package java_study.calculadoraimc;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    // Componentes que precisamos acessar dentro dos métodos dos botões
    private TextField txtNome;
    private TextField txtAltura;
    private TextField txtPeso;
    private Label lblImc;
    private Label lblClassificacao;
    private TableView<Pessoa> tabela;

    // Lista "observável": quando mudamos ela, a TableView se atualiza sozinha
    private final ObservableList<Pessoa> dados = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        // ---------- 1) Menu superior ----------
        MenuBar menuBar = criarMenu();

        // ---------- 2) Formulário (lado esquerdo) ----------
        txtNome = new TextField();
        txtAltura = new TextField();
        txtAltura.setPromptText("Ex.: 1.75");
        txtPeso = new TextField();
        txtPeso.setPromptText("Ex.: 70.5");

        VBox form = new VBox(5,
                new Label("Nome:"), txtNome,
                new Label("Altura (m):"), txtAltura,
                new Label("Peso (kg):"), txtPeso);
        HBox.setHgrow(form, Priority.ALWAYS); // o formulário ocupa o espaço sobrando

        // ---------- 3) Resultado (lado direito) ----------
        Label titulo = new Label("Cálculo IMC");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        lblImc = new Label(String.format("%.2f", 0.0));
        lblImc.setStyle("-fx-font-size: 64px;");

        lblClassificacao = new Label("");
        lblClassificacao.setStyle("-fx-font-size: 14px;");

        VBox resultado = new VBox(5, titulo, lblImc, lblClassificacao);
        resultado.setAlignment(Pos.CENTER);
        resultado.setPrefWidth(280);

        HBox topo = new HBox(20, form, resultado);

        // ---------- 4) Botões ----------
        Button btnCalcular = new Button("Calcular IMC");
        Button btnSalvar = new Button("Salvar");
        Button btnCarregar = new Button("Carregar Dados");

        btnCalcular.setOnAction(e -> aoCalcular());
        btnSalvar.setOnAction(e -> aoSalvar());
        btnCarregar.setOnAction(e -> aoCarregar());

        HBox botoes = new HBox(10, btnCalcular, btnSalvar, btnCarregar);

        // ---------- 5) Tabela ----------
        criarTabela();

        // ---------- 6) Montagem final ----------
        VBox centro = new VBox(10, topo, botoes, tabela);
        centro.setPadding(new Insets(10));
        VBox.setVgrow(tabela, Priority.ALWAYS); // a tabela cresce junto com a janela

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(centro);

        // Ao abrir o programa, tenta carregar o que já foi salvo antes.
        // Assim o "Salvar" nunca sobrescreve registros antigos sem querer.
        carregarSilenciosamente();

        stage.setTitle("Cálculo de IMC");
        stage.setScene(new Scene(root, 720, 540));
        stage.show();
    }

    // =====================================================================
    // Construção da interface
    // =====================================================================

    private MenuBar criarMenu() {
        MenuItem itemSair = new MenuItem("Sair");
        itemSair.setOnAction(e -> Platform.exit());
        Menu menuFile = new Menu("File");
        menuFile.getItems().add(itemSair);

        MenuItem itemLimpar = new MenuItem("Limpar campos");
        itemLimpar.setOnAction(e -> limparCampos());
        Menu menuEdit = new Menu("Edit");
        menuEdit.getItems().add(itemLimpar);

        MenuItem itemSobre = new MenuItem("Sobre");
        itemSobre.setOnAction(e -> mostrarAlerta(Alert.AlertType.INFORMATION, "Sobre",
                "Laboratório 01 - Cálculo de IMC com JavaFX."));
        Menu menuHelp = new Menu("Help");
        menuHelp.getItems().add(itemSobre);

        return new MenuBar(menuFile, menuEdit, menuHelp);
    }

    private void criarTabela() {
        tabela = new TableView<>(dados); // liga a tabela à lista observável
        tabela.setPlaceholder(new Label("Não há conteúdo na tabela"));
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Cada coluna recebe uma "receita" de qual valor da Pessoa mostrar
        TableColumn<Pessoa, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Pessoa, String> colNome = new TableColumn<>("NOME");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));

        TableColumn<Pessoa, String> colAltura = new TableColumn<>("ALTURA");
        colAltura.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("%.2f", c.getValue().getAltura())));

        TableColumn<Pessoa, String> colPeso = new TableColumn<>("PESO");
        colPeso.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("%.2f", c.getValue().getPeso())));

        TableColumn<Pessoa, String> colImc = new TableColumn<>("IMC");
        colImc.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("%.2f", c.getValue().getImc())));

        TableColumn<Pessoa, String> colClass = new TableColumn<>("CLASSIFICAÇÃO");
        colClass.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getClassificacao()));

        tabela.getColumns().addAll(colId, colNome, colAltura, colPeso, colImc, colClass);
    }

    // =====================================================================
    // Ações dos botões
    // =====================================================================

    /** Botão "Calcular IMC": só mostra o resultado, não grava nada. */
    private void aoCalcular() {
        try {
            Pessoa p = criarPessoaDosCampos(0); // id 0 = temporário, não será guardado
            exibirResultado(p);
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dados inválidos", e.getMessage());
        }
    }

    /** Botão "Salvar": adiciona na tabela e grava a lista inteira no arquivo. */
    private void aoSalvar() {
        Pessoa p;
        try {
            p = criarPessoaDosCampos(proximoId());
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Dados inválidos", e.getMessage());
            return;
        }

        dados.add(p);
        try {
            ArquivoUtil.salvar(dados);
            exibirResultado(p);
            limparCampos();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso",
                    "Dados salvos em " + ArquivoUtil.NOME_ARQUIVO);
        } catch (IOException e) {
            dados.remove(p); // desfaz: não deixa na tabela algo que não foi gravado
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao salvar", e.getMessage());
        }
    }

    /** Botão "Carregar Dados": lê o arquivo e substitui o conteúdo da tabela. */
    private void aoCarregar() {
        try {
            List<Pessoa> lidas = ArquivoUtil.carregar();
            dados.setAll(lidas);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Carregar",
                    lidas.size() + " registro(s) carregado(s).");
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao carregar", e.getMessage());
        }
    }

    // =====================================================================
    // Métodos auxiliares
    // =====================================================================

    /** Lê os TextFields, converte os números e cria a Pessoa (lança exceção se algo for inválido). */
    private Pessoa criarPessoaDosCampos(int id) {
        double altura = lerNumero(txtAltura.getText(), "Altura");
        double peso = lerNumero(txtPeso.getText(), "Peso");
        return new Pessoa(id, txtNome.getText(), altura, peso);
    }

    /** Converte texto em double aceitando vírgula ou ponto (1,75 ou 1.75). */
    private double lerNumero(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido no campo " + campo + ".");
        }
    }

    private void exibirResultado(Pessoa p) {
        lblImc.setText(String.format("%.2f", p.getImc()));
        lblClassificacao.setText(p.getClassificacao());
    }

    private int proximoId() {
        return dados.stream().mapToInt(Pessoa::getId).max().orElse(0) + 1;
    }

    private void limparCampos() {
        txtNome.clear();
        txtAltura.clear();
        txtPeso.clear();
        txtNome.requestFocus();
    }

    private void carregarSilenciosamente() {
        try {
            dados.setAll(ArquivoUtil.carregar());
        } catch (IOException e) {
            System.err.println("Não foi possível carregar o arquivo: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
