/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author Christian
 */
public class Venda {

    private int idVenda;
    private Date dataVenda;
    private Cliente cliente;

    private float desconto;
    private float valorPago;

    private ArrayList<VendaItem> itens = new ArrayList<VendaItem>();

    public Venda(int id, Date data, Cliente pes, float desconto, float valorPago) {
        this.idVenda = id;
        this.dataVenda = data;
        this.cliente = pes;

        this.desconto = desconto;
        this.valorPago = valorPago;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public float getValorPago() {
        return valorPago;
    }

    public void setValorPago(float valorPago) {
        this.valorPago = valorPago;
    }

    public float getValorTroco() {
        if (getValorPago() > getTotalVendaLiquida()) {
            return getValorPago() - getTotalVendaLiquida();
        } else {
            return 0.0f;
        }
    }

    public Venda() {
        this.idVenda = 0;
        this.dataVenda = null;
        this.cliente = null;
    }

    public float getTotalVenda() {
        float total = 0.0f;
        for (VendaItem i : itens) {
            total += (i.getValorUnitario() * i.getQuantidade());
        }
        return total;
    }

    public float getTotalVendaLiquida() {
        float total = this.getTotalVenda();
        total = total - this.getDesconto();
        return total;
    }

    public void setIdVenda(int id) {
        this.idVenda = id;
    }

    public int getIdVenda() {
        return this.idVenda;
    }

    public void setDataVenda(Date data) {
        this.dataVenda = data;
    }

    public Date getDataVenda() {
        return this.dataVenda;
    }

    public void setPessoa(Cliente pes) {
        this.cliente = pes;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void adicionaItem(VendaItem item) {
        this.itens.add(item);
    }

    public void removeItem(int index) {
        this.itens.remove(index);
    }

    /* retorna a lista de Itens da venda ordenada pelo ID do Item */
    public ArrayList<VendaItem> getItens() {
        Collections.sort(this.itens, new ComparadorItensVenda());
        return this.itens;
    }

    /* retorna o próximo número disponivel para o Item na venda */
    public int getNextItemCodigo() {
        int codigo = 1;
        int i = 0;
        while ((i < itens.size()) && (itens.size() > 0)) {
            VendaItem item = itens.get(i);
            if (item.getIdVendaItem() == codigo) {
                codigo++;
                i = -1;
            }
            i++;
        }
        return codigo;
    }

    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat fdf = new DecimalFormat("#,##0.00");
        String str = "Código Venda: " + this.getIdVenda() + " - Data: " + df.format(this.getDataVenda());
        str += "\n      Cliente:  " + this.getCliente().getIdCliente();
        str += " - " + this.getCliente().getNome();
        str += " - " + df.format(this.getCliente().getData());
        str += "\n      Total: " + fdf.format(this.getTotalVenda());
        str += "\n      Desconto: " + fdf.format(this.getDesconto());
        str += "\n      Total Venda: " + fdf.format(this.getTotalVenda() - this.getDesconto());
        return str;
    }

}
