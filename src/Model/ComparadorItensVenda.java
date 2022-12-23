/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Comparator;

/**
 *
 * @author Christian
 * Classe resposável pela ordenação (Comparação) dos Itens de uma venda.
 */
public class ComparadorItensVenda implements Comparator<VendaItem> {

    public int compare(VendaItem o1, VendaItem o2) {
        if (o1.getIdVendaItem() < o2.getIdVendaItem()) {
            return -1;
        } else if (o1.getIdVendaItem() > o2.getIdVendaItem()) {
            return +1;
        } else {
            return 0;
        }
    }

}
