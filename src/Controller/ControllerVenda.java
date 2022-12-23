/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ClienteCompra;
import Model.DAO.DAOVenda;
import Model.ItemVendido;
import Model.Venda;
import java.util.ArrayList;

/**
 *
 * @author Christian
 */
public class ControllerVenda {

    public ArrayList<Venda> getVendas() {
        DAOVenda srv = new DAOVenda();
        return srv.getAll();
    }

    public void persistirBanco(Venda venda) {
        DAOVenda srv = new DAOVenda();
        srv.insert(venda);
    }

    public ArrayList<ClienteCompra> getClientePFMaisComprou() {
        DAOVenda srv = new DAOVenda();
        return srv.getClientePFMaisComprou();
    }

    public ArrayList<ClienteCompra> getClientePJMenosComprou() {
        DAOVenda srv = new DAOVenda();
        return srv.getClientePJMenosComprou();
    }

    public ArrayList<ItemVendido> getProdutosVendidos() {
        DAOVenda srv = new DAOVenda();
        return srv.getProdutosVendidos();
    }

    public String toString() {
        String res = "";
        for (Venda o : this.getVendas()) {
            res += o.toString() + "\n";
        }
        return res;
    }

}
