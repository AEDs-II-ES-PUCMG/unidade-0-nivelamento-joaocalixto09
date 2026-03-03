package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Comercio {
/** Para inclusão de novos produtos no vetor */
static final int MAX_NOVOS_PRODUTOS = 10;
/** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do
projeto */
static String nomeArquivoDados;
/** Scanner para leitura do teclado */
static Scanner teclado;
/** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a
cada execução */
static Produto[] produtosCadastrados;
/** Quantidade produtos cadastrados atualmente no vetor */
static int quantosProdutos;
/** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
static void pausa(){
System.out.println("Digite enter para continuar...");
teclado.nextLine();
}
/** Cabeçalho principal da CLI do sistema */
static void cabecalho(){
System.out.println("AEDII COMÉRCIO DE COISINHAS");
System.out.println("===========================");
}
/** Imprime o menu principal, lê a opção do usuário e a retorna (int).
* Perceba que poderia haver uma melhor modularização com a criação de uma
classe Menu.
* @return Um inteiro com a opção do usuário.
*/
static int menu(){
cabecalho();
System.out.println("1 - Listar todos os produtos");
System.out.println("2 - Procurar e listar um produto");
System.out.println("3 - Cadastrar novo produto");
System.out.println("0 - Sair");
System.out.print("Digite sua opção: ");
return Integer.parseInt(teclado.nextLine());
}
/** Lista todos os produtos cadastrados, numerados, um por linha */
static void listarTodosOsProdutos(){
cabecalho();
System.out.println("\nPRODUTOS CADASTRADOS:");
for (int i = 0; i < produtosCadastrados.length; i++) {
if(produtosCadastrados[i]!=null)
System.out.println(String.format("%02d - %s",
(i+1),produtosCadastrados[i].toString()));
}
}

public static void main(String[] args) throws Exception {
teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
nomeArquivoDados = "dadosProdutos.csv";
produtosCadastrados = lerProdutos(nomeArquivoDados);
int opcao = -1;
do{
opcao = menu();
if(opcao == 1) listarTodosOsProdutos();
else if(opcao == 2) localizarProdutos();
else if(opcao == 3) cadastrarProduto();
pausa();
}while(opcao !=0);
salvarProdutos(nomeArquivoDados);
teclado.close();
}

static Produto[] lerProdutos(String nomeArquivoDados) {
    Produto[] vetorProdutos = null;
    try {
        Scanner leitor = new Scanner(new File(nomeArquivoDados));
        if (leitor.hasNextLine()) {
            // 1. Lê a quantidade N de produtos [cite: 58]
            int n = Integer.parseInt(leitor.nextLine());
            // 2. Instancia o vetor com espaço reserva [cite: 130, 167]
            vetorProdutos = new Produto[n + MAX_NOVOS_PRODUTOS];
            quantosProdutos = 0;

            // 3. Lê cada linha e converte em objeto [cite: 168, 169]
            while (leitor.hasNextLine() && quantosProdutos < n) {
                String linha = leitor.nextLine();
                vetorProdutos[quantosProdutos] = Produto.criarDoTexto(linha);
                quantosProdutos++;
            }
        }
        leitor.close();
    } catch (FileNotFoundException e) {
        // Se o arquivo não existir, cria o vetor vazio para começar do zero
        vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
        quantosProdutos = 0;
    }
    return vetorProdutos;
}

static void localizarProdutos() {
    System.out.print("Digite o nome (descrição) do produto: ");
    String busca = teclado.nextLine();
    boolean achou = false;

    for (int i = 0; i < quantosProdutos; i++) {
        // Comparamos a descrição ignorando maiúsculas/minúsculas [cite: 175]
        if (produtosCadastrados[i].toString().toLowerCase().contains(busca.toLowerCase())) {
            System.out.println("Encontrado: " + produtosCadastrados[i].toString());
            achou = true;
        }
    }
    if (!achou) System.out.println("Produto não encontrado.");
}


static void cadastrarProduto() {
    System.out.println("Tipo de produto (1-Não Perecível, 2-Perecível): ");
    int tipo = Integer.parseInt(teclado.nextLine());

    System.out.print("Descrição: ");
    String desc = teclado.nextLine();
    System.out.print("Preço de Custo: ");
    double preco = Double.parseDouble(teclado.nextLine());
    System.out.print("Margem de Lucro (ex: 0.2 para 20%): ");
    double margem = Double.parseDouble(teclado.nextLine());

    if (tipo == 1) {
        produtosCadastrados[quantosProdutos] = new ProdutoNaoPerecivel(desc, preco, margem);
    } else {
        System.out.print("Data de Validade (dd/mm/aaaa): ");
        String dataStr = teclado.nextLine();
        LocalDate validade = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        produtosCadastrados[quantosProdutos] = new ProdutoPerecivel(desc, preco, margem, validade);
    }
    quantosProdutos++; // Incrementa o contador do vetor [cite: 185]
    System.out.println("Produto cadastrado com sucesso!");
}


public static void salvarProdutos(String nomeArquivo) {
    try {
        FileWriter escritor = new FileWriter(new File(nomeArquivo));
        // Primeira linha: quantidade total [cite: 56]
        escritor.write(quantosProdutos + "\n");

        for (int i = 0; i < quantosProdutos; i++) {
            // Usa o polimorfismo para gerar a linha correta (tipo 1 ou 2) [cite: 83]
            escritor.write(produtosCadastrados[i].gerarDadosTexto() + "\n");
        }
        escritor.close();
        System.out.println("Dados salvos com sucesso!");
    } catch (IOException e) {
        System.out.println("Erro ao salvar o arquivo.");
    }
}

}