/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Cliente;
import Model.ClientePF;
import Model.ClientePJ;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Christian
 */
public class DAOCliente {

    private DAOConexaoDB conexao = new DAOConexaoDB();

    public void insert(Cliente pessoa) {
        try {
            if (pessoa instanceof ClientePF) {
                ClientePF pf = (ClientePF) pessoa;
                String SQL
                        = "insert into tb_cliente (id_cliente,data,idade,nome,cpf_cnpj,identidade)"
                        + " values (0,?,?,?,?,?)";
                PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
                pst.setDate(1, new java.sql.Date(pf.getData().getTime()));
                pst.setInt(2, pf.getIdade());
                pst.setString(3, pf.getNome());
                pst.setString(4, pf.getCPF());
                pst.setInt(5, pf.getIdentidade());

                pst.executeUpdate();
                pst.close();
                conexao.close();
            }
            if (pessoa instanceof ClientePJ) {
                ClientePJ pj = (ClientePJ) pessoa;
                String SQL
                        = "insert into tb_cliente (id_cliente,nome,cpf_cnpj,inscricao_estadual,data,idade)"
                        + " values (0,?,?,?,?,?)";
                PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
                pst.setString(1, pj.getNome());
                pst.setString(2, pj.getCnpj());
                pst.setInt(3, pj.getInscricaoEstadual());
                pst.setDate(4, new java.sql.Date(pj.getData().getTime()));
                pst.setInt(5, pj.getIdade());

                pst.executeUpdate();
                pst.close();
                conexao.close();
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void update(Cliente pessoa) {
        try {
            if (pessoa instanceof ClientePF) {
                ClientePF pf = (ClientePF) pessoa;
                String SQL = "update tb_cliente "
                        + "set data = ?,idade=?,nome=?,cpf_cnpj=?,identidade=?"
                        + " where id_cliente = ?";
                PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
                pst.setDate(1, new java.sql.Date(pf.getData().getTime()));
                pst.setInt(2, pf.getIdade());
                pst.setString(3, pf.getNome());
                pst.setString(4, pf.getCPF());
                pst.setInt(5, pf.getIdentidade());
                pst.setInt(6, pf.getIdCliente());
                pst.executeUpdate();
                pst.close();
                conexao.close();
            }

            if (pessoa instanceof ClientePJ) {
                ClientePJ pj = (ClientePJ) pessoa;
                String SQL = "update tb_cliente "
                        + "set nome=?,cpf_cnpj=?,inscricao_estadual=?,idade=?,data = ? "
                        + " where id_cliente = ?";
                PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
                pst.setString(1, pj.getNome());
                pst.setString(2, pj.getCnpj());
                pst.setInt(3, pj.getInscricaoEstadual());
                pst.setInt(4, pj.getIdade());
                pst.setDate(5, new java.sql.Date(pj.getData().getTime()));
                pst.setInt(6, pj.getIdCliente());
                pst.executeUpdate();
                pst.close();
                conexao.close();
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(Cliente pessoa) {
        try {
            PreparedStatement pst = conexao.getConexao().prepareStatement("delete from tb_cliente where id_cliente = ?");
            pst.setInt(1, pessoa.getIdCliente());
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void setCodigoBancoCliente(Cliente pessoa) {
        try {
            if (pessoa instanceof ClientePF) {
                ClientePF pf = (ClientePF) pessoa;
                String SQL
                        = "select id_cliente from tb_cliente "
                        + "where data = ? "
                        + " and idade = ? "
                        + " and nome =? "
                        + " and cpf_cnpj = ? "
                        + " and identidade = ?";
                PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
                pst.setDate(1, new java.sql.Date(pf.getData().getTime()));
                pst.setInt(2, pf.getIdade());
                pst.setString(3, pf.getNome());
                pst.setString(4, pf.getCPF());
                pst.setInt(5, pf.getIdentidade());

                ResultSet rs = pst.executeQuery();
                rs.first();
                int qtde = rs.getRow();
                rs.first();
                if (qtde > 0) {
                    pessoa.setIdCliente(rs.getInt("id_cliente"));
                }
                rs.close();
                pst.close();
                conexao.close();
            }
            if (pessoa instanceof ClientePJ) {
                ClientePJ pj = (ClientePJ) pessoa;
                String SQL
                        = "select id_cliente from tb_cliente "
                        + "where nome =? "
                        + " and cpf_cnpj = ? "
                        + " and inscricao_estadual = ? "
                        + " and idade = ? "
                        + " and data = ?";
                PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
                pst.setString(1, pj.getNome());
                pst.setString(2, pj.getCnpj());
                pst.setInt(3, pj.getInscricaoEstadual());
                pst.setInt(4, pj.getIdade());
                pst.setDate(5, new java.sql.Date(pj.getData().getTime()));

                ResultSet rs = pst.executeQuery();
                rs.last();
                int qtde = rs.getRow();
                rs.first();
                if (qtde > 0) {
                    pessoa.setIdCliente(rs.getInt("id_cliente"));
                }
                rs.close();
                pst.close();
                conexao.close();
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

    }

    public ArrayList<Cliente> getAll() {
        try {
            Statement st = conexao.getConexao().createStatement();
            ResultSet rs = st.executeQuery("select * from tb_cliente");
            ArrayList<Cliente> lista = new ArrayList<Cliente>();
            while (rs.next()) {
                String cpfCnpj = rs.getString("cpf_cnpj");
                if (cpfCnpj.length() <= 11) {
                    ClientePF pf = new ClientePF(rs.getInt("id_cliente"), rs.getDate("data"), rs.getString("nome"), rs.getString("cpf_cnpj"), rs.getInt("identidade"));
                    lista.add(pf);
                }
                if (cpfCnpj.length() > 11) {
                    ClientePJ pj = new ClientePJ(rs.getInt("id_cliente"), rs.getDate("data"), rs.getString("nome"), rs.getString("cpf_cnpj"), rs.getInt("inscricao_estadual"));
                    lista.add(pj);
                }
            }
            rs.close();
            st.close();
            conexao.close();
            return lista;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public Cliente getCliente(int id) {
        try {
            Cliente p = null;
            String SQL = "select * from tb_cliente where id_cliente = " + String.valueOf(id);
            Statement st = conexao.getConexao().createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                String cpfCnpj = rs.getString("cpf_cnpj");
                if (cpfCnpj.length() <= 11) {
                    p = new ClientePF(rs.getInt("id_cliente"), rs.getDate("data"), rs.getString("nome"), rs.getString("cpf_cnpj"), rs.getInt("identidade"));
                }
                if (cpfCnpj.length() > 11) {
                    p = new ClientePJ(rs.getInt("id_cliente"), rs.getDate("data"), rs.getString("nome"), rs.getString("cpf_cnpj"), rs.getInt("inscricao_estadual"));
                }
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
