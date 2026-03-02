package com.example;

public class ProdutoNaoPerecivel extends Produto {

    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
        super(desc, precoCusto, margemLucro);
    }

    public ProdutoNaoPerecivel(String desc, double precoCusto) {
        super(desc, precoCusto);
    }

    @Override
    public double valorVenda() {
        return precoCusto * (1 + margemLucro);
    }

    @Override
    public String gerarDadosTexto() {
        //Tipo 1 para não pereciveis
        return String.format("1;%s;%.2f;%.2f", descricao, precoCusto, margemLucro); 
    }
}
