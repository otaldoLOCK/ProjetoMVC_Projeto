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
public class VendaItem {

    private int idVendaItem;
    private int quantidade;
    private float valorUnitario;
    private Produto produto;

    public VendaItem(int id, int qtde, float valor) {
        this.idVendaItem = id;
        this.quantidade = qtde;
        this.valorUnitario = valor;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String str = "      Código Item: " + this.getIdVendaItem();
        str += "\n                  Produto: " + this.getProduto().getDescricao();
        str += "\n                  Valor Unitário: " + df.format(this.getValorUnitario());
        str += "\n                  Quantidade: " + String.valueOf(this.getQuantidade());
        str += "\n                  Total: " + df.format(this.getValorUnitario() * this.getQuantidade());
        return str;
    }

    public void setProduto(Produto p) {
        this.produto = p;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setIdVendaItem(int id) {
        this.idVendaItem = id;
    }

    public int getIdVendaItem() {
        return this.idVendaItem;
    }

    public void setQuantidade(int qtd) {
        this.quantidade = qtd;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setValorUnitario(float valor) {
        this.valorUnitario = valor;
    }

    public float getValorUnitario() {
        return this.valorUnitario;
    }
}
