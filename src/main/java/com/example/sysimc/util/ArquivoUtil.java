package com.example.sysimc.util;

import com.example.sysimc.model.Pessoa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {

    private static final String NOME_ARQUIVO = "dados_pessoas.txt";
    private static final String SEPARADOR = ";";

    public static void salvar(List<Pessoa> pessoas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Pessoa p : pessoas) {
                writer.write(
                        p.getId()            + SEPARADOR +
                                texto(p.getNome())   + SEPARADOR +
                                texto(p.getClassificacao()) + SEPARADOR +
                                texto(p.getAltura()) + SEPARADOR +
                                texto(p.getPeso())   + SEPARADOR +
                                texto(p.getImc())
                );
                writer.newLine();
            }
        }
    }

    public static List<Pessoa> carregar() throws IOException {
        List<Pessoa> pessoas = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return pessoas;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.isBlank()) continue;

                // -1 mantém campos vazios no fim (ex.: imc nulo)
                String[] dados = linha.split(SEPARADOR, -1);

                if (dados.length == 6) {
                    try {
                        Pessoa p = new Pessoa(
                                Integer.parseInt(dados[0].trim()),
                                dados[1],
                                vazioParaNull(dados[2]),
                                parseDoubleOuNull(dados[3]),
                                parseDoubleOuNull(dados[4]),
                                parseDoubleOuNull(dados[5])
                        );
                        pessoas.add(p);
                    } catch (NumberFormatException e) {
                        // linha corrompida — ignora e continua
                        System.err.println("Linha inválida ignorada: " + linha);
                    }
                }
            }
        }
        return pessoas;
    }

    // ---------- helpers ----------

    private static String texto(Object o) {
        return o == null ? "" : o.toString();
    }


    private static String vazioParaNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }


    private static Double parseDoubleOuNull(String s) {
        s = s.trim();
        return s.isEmpty() ? null : Double.parseDouble(s);
    }
}