/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.text.DecimalFormat;

/**
 *
 * @author Christian
 */
public class ItemVendido {

    private Produto produto;
    private int quantidade;

    public void setProduto(Produto p) {
        this.produto = p;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setQuantidade(int qtd) {
        this.quantidade = qtd;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public ItemVendido(Produto p, int qtd) {
        this.produto = p;
        this.quantidade = qtd;
    }
    
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0");
        return "Produto: " + this.getProduto().toString() + " - Quantidade: " + df.format(this.getQuantidade());
    }
}
