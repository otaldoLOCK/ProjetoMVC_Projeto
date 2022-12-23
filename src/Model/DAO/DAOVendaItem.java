/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.VendaItem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Christian
 */
public class DAOVendaItem {

    private DAOConexaoDB conexao = new DAOConexaoDB();

    public void insert(VendaItem item, int idVenda) {
        try {
            String SQL = "insert into tb_venda_item "
                    + "(id_venda_item,quantidade,valor_unitario,id_venda,id_produto)"
                    + " values (0,?,?,?,?)";
            PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
            pst.setInt(1, item.getQuantidade());
            pst.setFloat(2, item.getValorUnitario());
            pst.setInt(3, idVenda);
            pst.setInt(4, item.getProduto().getIdProduto());
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void update(VendaItem item) {
        try {
            String SQL = "update tb_venda_item "
                    + "set quantidade=?,valor_unitario=?, id_produto=? "
                    + " where id_venda_item = ?";
            PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
            pst.setInt(1, item.getQuantidade());
            pst.setFloat(2, item.getValorUnitario());
            pst.setFloat(3, item.getProduto().getIdProduto());
            pst.setInt(4, item.getIdVendaItem());
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(VendaItem item) {
        try {
            String SQL = "delete from tb_venda_item where id_venda_item = ?";
            PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
            pst.setInt(1, item.getIdVendaItem());
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void setCodigoBancoVendaItem(VendaItem item, int idVenda) {
        try {
            String SQL = "select id_venda_item from tb_venda_item where quantidade=?,valor_unitario=?,id_venda=?,id_produto=?";
            PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
            pst.setInt(1, item.getQuantidade());
            pst.setFloat(2, item.getValorUnitario());
            pst.setInt(3, idVenda);
            pst.setInt(4, item.getProduto().getIdProduto());

            ResultSet rs = pst.executeQuery();
            rs.first();
            item.setIdVendaItem(rs.getInt("id_venda_item"));

            rs.close();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
