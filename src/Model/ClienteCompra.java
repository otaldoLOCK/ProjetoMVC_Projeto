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
public class ClienteCompra {
    private Cliente cliente;
    private int quantidade;

    public void setCliente(Cliente c) {
        this.cliente = c;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setQuantidade(int qtd) {
        this.quantidade = qtd;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public ClienteCompra(Cliente c, int qtd) {
        this.cliente = c;
        this.quantidade = qtd;
    }
    
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0");
        return "Cliente: " + this.getCliente().toString() + " - Quantidade Venda(s): " + df.format(this.getQuantidade());
    }
}
