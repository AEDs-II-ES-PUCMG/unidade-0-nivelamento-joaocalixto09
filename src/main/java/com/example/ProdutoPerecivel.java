package main.java.com.example;

import java.time.LocalDate;

public class ProdutoPerecivel extends Produto {

    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;

    private LocalDate dataValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataValidade) {
        super(desc, precoCusto, margemLucro);

        if(dataValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("data de validade invalida");

        }

        this.dataValidade = dataValidade;
    }

    public ProdutoPerecivel(String desc, double precoCusto, LocalDate dataValidade) {
        super(desc, precoCusto);

        if(dataValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("data de validade invalida");

        }

        this.dataValidade = dataValidade;
    }

    @Override
    public double valorDeVenda() {

        LocalDate hoje = LocalDate.now();

        if (hoje.isAfter(dataValidade)) {
            throw new IllegalStateException("Produto Vencido");
        }

        double valor = super.valorDeVenda();

         long dias = java.time.temporal.ChronoUnit.DAYS.between(hoje, dataValidade);

         if (dias <= PRAZO_DESCONTO) {
            valor = valor * (1-DESCONTO);
         }

         return valor; 

    }

     @Override
    public String toString() {
        return super.toString() +
               " | Validade: " + dataValidade;
    }
    
}
