package com.example;

import java.time.LocalDate;

public class ProdutoPerecivel extends Produto {

    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;

    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);

        if (validade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Produto esta vencido!");
        }

        dataDeValidade = validade;
    }

    @Override
    public double valorVenda() {

        double desconto = 0d;

        long diasValidade = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dataDeValidade);

        if (diasValidade <= PRAZO_DESCONTO) {
            desconto = DESCONTO;
        }

        return (precoCusto * (1 + margemLucro)) * (1 - desconto);
    }

    @Override
    public String toString() {

        java.time.format.DateTimeFormatter formato = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dados = super.toString();
        dados += "\nValidade ate " + formato.format(dataDeValidade);

        return dados;
    }
}