package com.example.sysimc.model;

public class Pessoa {
    private int id;
    private String nome;
    private String classificacao;
    private Double altura;
    private Double peso;
    private Double imc;

    public Pessoa() {
    }

    public Pessoa(int id, String nome, String classificacao, Double altura, Double peso, Double imc) {
        this.id = id;
        this.nome = nome;
        this.classificacao = classificacao;
        this.altura = altura;
        this.peso = peso;
        this.imc = imc;
    }

    // ---------------- getters / setters ----------------

    public Double getImc() { return imc; }
    public void setImc(Double imc) { this.imc = imc; }

    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    // ---------------- lógica ----------------

    public Double calcularIMC() {
        if (this.peso == null || this.altura == null || this.altura == 0.0) {
            this.imc = null;
        } else {
            this.imc = this.peso / (this.altura * this.altura);
        }
        return this.imc;
    }

    public String classificacaoIMC() {
        this.imc = this.calcularIMC();

        if (this.imc == null || this.imc.isNaN()) {
            this.classificacao = "";
            return this.classificacao;
        }

        if (this.imc < 18.5) {
            this.classificacao = "Abaixo do peso";
        } else if (this.imc < 25.0) {
            this.classificacao = "Peso normal";
        } else if (this.imc < 30.0) {
            this.classificacao = "Sobrepeso";
        } else if (this.imc < 35.0) {
            this.classificacao = "Obesidade Grau 1";
        } else if (this.imc < 40.0) {
            this.classificacao = "Obesidade Grau 2";
        } else {
            this.classificacao = "Obesidade Grau 3";
        }

        return this.classificacao;
    }

    @Override
    public String toString() {
        return "Pessoa{id=" + id + ", nome='" + nome + "', altura=" + altura
                + ", peso=" + peso + ", imc=" + imc + ", classificacao='" + classificacao + "'}";
    }
}