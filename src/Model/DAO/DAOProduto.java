/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Produto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Christian
 */
public class DAOProduto {

    private DAOConexaoDB conexao = new DAOConexaoDB();

    public void insert(Produto produto) {
        try {
            PreparedStatement pst = conexao.getConexao().prepareStatement("insert into tb_produto (id_produto,descricao,valor)"
                    + " values (0,?,?)");
            pst.setString(1, produto.getDescricao());
            pst.setFloat(2, produto.getValor());
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void update(Produto produto) {
        try {
            PreparedStatement pst = conexao.getConexao().prepareStatement("update tb_produto set descricao=?,valor=? where id_produto = ?");
            pst.setString(1, produto.getDescricao());
            pst.setFloat(2, produto.getValor());
            pst.setInt(3, produto.getIdProduto());
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(Produto produto) {
        try {
            PreparedStatement pst = conexao.getConexao().prepareStatement("delete from tb_produto where id_produto = ?");
            pst.setInt(1, produto.getIdProduto());;
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void setCodigoBancoProduto(Produto produto) {
        try {
            PreparedStatement pst = conexao.getConexao().prepareStatement("select id_produto from tb_produto where descricao = ? and valor = ?");
            pst.setString(1, produto.getDescricao());
            pst.setFloat(2, produto.getValor());

            ResultSet rs = pst.executeQuery();
            rs.first();
            int qtde = rs.getRow();
            rs.first();
            if (qtde > 0) {
                produto.setIdProduto(rs.getInt("id_produto"));
            }
            rs.close();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public ArrayList<Produto> getAll() {
        try {
            Statement st = conexao.getConexao().createStatement();
            ResultSet rs = st.executeQuery("select * from tb_produto");
            ArrayList<Produto> lista = new ArrayList<Produto>();
            while (rs.next()) {
                lista.add(new Produto(rs.getInt("id_produto"), rs.getString("descricao"), rs.getFloat("valor")));
            }
            rs.close();
            st.close();
            conexao.close();
            return lista;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public Produto getProduto(int id) {
        try {
            Produto p = null;
            String SQL = "select * from tb_produto where id_produto = " + String.valueOf(id);
            Statement st = conexao.getConexao().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                p = new Produto(rs.getInt("id_produto"), rs.getString("descricao"), rs.getFloat("valor"));
            }
            rs.close();
            st.close();
            conexao.close();
            return p;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

}
