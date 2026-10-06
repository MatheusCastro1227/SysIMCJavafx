package com.example.sysimc.controller;

import com.example.sysimc.model.Pessoa;
import com.example.sysimc.util.ArquivoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TextField txtNome;
    @FXML private TextField txtAltura;
    @FXML private TextField txtPeso;

    @FXML private Label lblIMC;
    @FXML private Label lblclassificacao;

    @FXML private TableView<Pessoa> tbPessoa;
    @FXML private TableColumn<Pessoa, Integer> colID;
    @FXML private TableColumn<Pessoa, String>  colNome;
    @FXML private TableColumn<Pessoa, Double>  colAltura;
    @FXML private TableColumn<Pessoa, Double>  colPeso;
    @FXML private TableColumn<Pessoa, Double>  colIMC;

    private Pessoa pessoa = new Pessoa();
    private final List<Pessoa> listaPessoa = new ArrayList<>();
    private ObservableList<Pessoa> observableListPessoa;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (this.pessoa == null) this.pessoa = new Pessoa();
        iniciarGUI();
    }

    private void iniciarGUI() {
        this.colID    .setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colNome  .setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        this.colPeso  .setCellValueFactory(new PropertyValueFactory<>("peso"));
        this.colIMC   .setCellValueFactory(new PropertyValueFactory<>("imc"));
    }

    // -------------------- Eventos --------------------

    @FXML
    public void onClickCalcularIMC() {
        try {
            lerFormulario();
        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
            return;
        }
        this.pessoa.classificacaoIMC();
        exibirClassificacaoIMC();
    }

    @FXML
    public void onClickSalvarIMC() {
        try {
            lerFormulario();
        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
            return;
        }

        this.pessoa.classificacaoIMC();
        this.pessoa.setId(this.listaPessoa.size() + 1);
        this.listaPessoa.add(this.pessoa);
        atualizarTableView();

        try {
            ArquivoUtil.salvar(this.listaPessoa);
        } catch (IOException e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }

        this.pessoa = new Pessoa();
    }

    @FXML
    public void onClickNovo() {
        this.pessoa = new Pessoa();
        txtNome.setText("");
        txtPeso.setText("");
        txtAltura.setText("");
        lblIMC.setText("");
        lblclassificacao.setText("");
    }

    @FXML
    public void onClickCarregar() {
        try {
            List<Pessoa> carregadas = ArquivoUtil.carregar();
            this.listaPessoa.clear();
            this.listaPessoa.addAll(carregadas);
            atualizarTableView();
        } catch (IOException e) {
            mostrarErro("Erro ao carregar: " + e.getMessage());
        }
    }

    // -------------------- Helpers --------------------

    private void lerFormulario() {
        if (this.pessoa == null) this.pessoa = new Pessoa();

        String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Informe o nome.");
        }

        this.pessoa.setNome(nome);
        this.pessoa.setAltura(parseDouble(txtAltura.getText(), "altura"));
        this.pessoa.setPeso(parseDouble(txtPeso.getText(), "peso"));

        if (this.pessoa.getAltura() <= 0) {
            throw new IllegalArgumentException("Altura deve ser maior que zero.");
        }
        if (this.pessoa.getPeso() <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero.");
        }
    }

    /** Aceita "1.75" ou "1,75". Lança IllegalArgumentException em texto vazio/inválido. */
    private Double parseDouble(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Preencha o campo " + campo + ".");
        }
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido no campo " + campo + ": " + texto);
        }
    }

    private void exibirClassificacaoIMC() {
        Double imc = this.pessoa.getImc();
        if (imc == null || imc.isNaN()) {
            lblIMC.setText("--");
            lblclassificacao.setText("");
            return;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        lblIMC.setText(df.format(imc));
        lblclassificacao.setText(this.pessoa.getClassificacao());
    }

    private void atualizarTableView() {
        this.observableListPessoa = FXCollections.observableList(this.listaPessoa);
        this.tbPessoa.setItems(this.observableListPessoa);
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}