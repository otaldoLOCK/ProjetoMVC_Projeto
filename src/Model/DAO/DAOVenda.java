/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.ClienteCompra;
import Model.ClientePF;
import Model.ClientePJ;
import Model.ItemVendido;
import Model.Venda;
import Model.VendaItem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Christian
 */
public class DAOVenda {

    private DAOConexaoDB conexao = new DAOConexaoDB();

    public void insert(Venda venda) {
        try {
            String SQL = "insert into tb_venda (id_venda,data_venda,id_cliente,DESCONTO,VALOR_PAGO,TOTAL_VENDA)"
                    + " values (0,?,?,?,?,?)";
            PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
            pst.setDate(1, new java.sql.Date(venda.getDataVenda().getTime()));
            pst.setInt(2, venda.getCliente().getIdCliente());
            pst.setFloat(3, venda.getDesconto());
            pst.setFloat(4, venda.getValorPago());
            pst.setFloat(5, venda.getTotalVendaLiquida());
            pst.executeUpdate();
            pst.close();
            conexao.close();

            setCodigoBancoVenda(venda);

            DAOVendaItem srv = new DAOVendaItem();
            ArrayList<VendaItem> listaItens = venda.getItens();
            for (VendaItem p : listaItens) {
                srv.insert(p, venda.getIdVenda());
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void update(Venda venda) {
        try {
            String SQL = "update tb_venda set data_venda=?,id_cliente=?,DESCONTO=?,VALOR_PAGO=?,TOTAL_VENDA=? where id_venda = ?";
            PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
            pst.setDate(1, new java.sql.Date(venda.getDataVenda().getTime()));
            pst.setInt(2, venda.getCliente().getIdCliente());
            pst.setFloat(3, venda.getDesconto());
            pst.setFloat(4, venda.getValorPago());
            pst.setFloat(5, venda.getTotalVendaLiquida());
            pst.setInt(6, venda.getIdVenda());
            pst.executeUpdate();
            pst.close();
            conexao.close();

            DAOVendaItem srv = new DAOVendaItem();
            ArrayList<VendaItem> listaItens = venda.getItens();
            for (VendaItem p : listaItens) {
                srv.update(p);
            }
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void delete(Venda venda) {
        try {
            DAOVendaItem srv = new DAOVendaItem();
            ArrayList<VendaItem> listaItens = venda.getItens();
            for (VendaItem p : listaItens) {
                srv.delete(p);
            }
            String SQL = "delete from tb_venda where id_venda = ?";
            PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
            pst.setInt(1, venda.getIdVenda());
            pst.executeUpdate();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void setCodigoBancoVenda(Venda venda) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String SQL = "select id_venda from tb_venda "
                    + "where data_venda = '" + fmt.format(venda.getDataVenda())+"' "
                    + " and id_cliente = " + String.valueOf(venda.getCliente().getIdCliente())
                    + " and DESCONTO=" + String.valueOf(venda.getDesconto())
                    + " and VALOR_PAGO=" + String.valueOf(venda.getValorPago())
                    + " and TOTAL_VENDA=" + String.valueOf(venda.getTotalVendaLiquida());
            PreparedStatement pst = conexao.getConexao().prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            rs.first();
            venda.setIdVenda(rs.getInt("id_venda"));

            rs.close();
            pst.close();
            conexao.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public ArrayList<Venda> getAll() {
        try {
            Statement st = conexao.getConexao().createStatement();
            Statement st2 = conexao.getConexao().createStatement();
            ResultSet rs = st.executeQuery("select c.*, p.* from tb_venda c, tb_cliente p where p.id_cliente = c.id_cliente order by p.nome, c.id_venda");
            ArrayList<Venda> lista = new ArrayList<Venda>();
            int i = 0;
            while (rs.next()) {
                String cpfCnpj = rs.getString("cpf_cnpj");
                if (cpfCnpj.length() <= 11) {
                    ClientePF pf = new ClientePF(rs.getInt("id_cliente"), rs.getDate("data"), rs.getString("nome"), rs.getString("cpf_cnpj"), rs.getInt("identidade"));
                    lista.add(new Venda(rs.getInt("id_venda"), rs.getDate("data_venda"), pf, rs.getFloat("desconto"), rs.getFloat("valor_pago")));
                }
                if (cpfCnpj.length() > 11) {
                    ClientePJ pj = new ClientePJ(rs.getInt("id_cliente"), rs.getDate("data"), rs.getString("nome"), rs.getString("cpf_cnpj"), rs.getInt("inscricao_estadual"));
                    lista.add(new Venda(rs.getInt("id_venda"), rs.getDate("data_venda"), pj, rs.getFloat("desconto"), rs.getFloat("valor_pago")));
                }
                DAOProduto srvProduto = new DAOProduto();
                ResultSet rp = st2.executeQuery("select * from tb_venda_item where id_venda = " + rs.getInt("id_venda") + " order by id_venda_item");
                while (rp.next()) {
                    VendaItem item = new VendaItem(rp.getInt("id_venda_item"), rp.getInt("quantidade"), rp.getFloat("valor_unitario"));
                    item.setProduto(srvProduto.getProduto(rp.getInt("id_produto")));
                    lista.get(i).adicionaItem(item);
                }
                rp.close();
                i++;
            }
            rs.close();
            st.close();
            st2.close();
            conexao.close();
            return lista;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public ArrayList<ClienteCompra> getClientePFMaisComprou() {
        try {
            Statement st = conexao.getConexao().createStatement();
            DAOCliente srvPessoa = new DAOCliente();
            String SQL
                    = "select p.id_cliente, count(*) as qtde_venda "
                    + "  from tb_venda c, tb_cliente p "
                    + "where p.id_cliente = c.id_cliente "
                    + "  and length(p.cpf_cnpj) <= 11 "
                    + "group by p.id_cliente "
                    + "order by 2 desc limit 1";
            ResultSet rs = st.executeQuery(SQL);
            ArrayList<ClienteCompra> lista = new ArrayList<ClienteCompra>();
            while (rs.next()) {
                lista.add(new ClienteCompra(srvPessoa.getCliente(rs.getInt("id_Cliente")), rs.getInt("qtde_venda")));
            }
            rs.close();
            st.close();
            conexao.close();
            return lista;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public ArrayList<ClienteCompra> getClientePJMenosComprou() {
        try {
            Statement st = conexao.getConexao().createStatement();
            DAOCliente srvPessoa = new DAOCliente();
            String SQL
                    = "select p.id_cliente, count(*) as qtde_venda "
                    + "  from tb_venda c, tb_cliente p "
                    + "where p.id_cliente = c.id_cliente "
                    + "  and length(p.cpf_cnpj) > 11 "
                    + "group by p.id_cliente "
                    + "order by 2 limit 1";
            ResultSet rs = st.executeQuery(SQL);
            ArrayList<ClienteCompra> lista = new ArrayList<ClienteCompra>();
            while (rs.next()) {
                lista.add(new ClienteCompra(srvPessoa.getCliente(rs.getInt("id_Cliente")), rs.getInt("qtde_venda")));
            }
            rs.close();
            st.close();
            conexao.close();
            return lista;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public ArrayList<ItemVendido> getProdutosVendidos() {
        try {
            Statement st = conexao.getConexao().createStatement();
            DAOProduto srvProduto = new DAOProduto();
            String SQL
                    = "select p.descricao, p.id_produto, sum(i.quantidade) as qtd "
                    + "  from tb_venda c, tb_venda_item i, tb_produto p "
                    + "where p.id_produto = i.id_produto "
                    + "  and i.id_venda = c.id_venda "
                    + "group by p.descricao, p.id_produto "
                    + "order by 3 desc ";
            ResultSet rs = st.executeQuery(SQL);
            ArrayList<ItemVendido> lista = new ArrayList<ItemVendido>();
            while (rs.next()) {
                lista.add(new ItemVendido(srvProduto.getProduto(rs.getInt("id_produto")), rs.getInt("qtd")));
            }
            rs.close();
            st.close();
            conexao.close();
            return lista;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }
}
