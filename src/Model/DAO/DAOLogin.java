/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.DAO.DAOConexaoDB;
import Model.Login;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Christian
 */
public class DAOLogin {

    private DAOConexaoDB conexao = new DAOConexaoDB();

    public ArrayList<Login> getAll() {
        try {
            Statement st = conexao.getConexao().createStatement();
            ResultSet rs = st.executeQuery("select * from tb_login order by login");
            ArrayList<Login> lista = new ArrayList<Login>();
            while (rs.next()) {
                lista.add(new Login(rs.getString("LOGIN"), rs.getString("SENHA"), rs.getString("NOME"), rs.getString("PRIVILEGIO")));
            }
            rs.close();
            st.close();
            conexao.close();
            return lista;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public ArrayList<String> getPrivilegios() {
        try {
            Statement st = conexao.getConexao().createStatement();
            ResultSet rs = st.executeQuery("SELECT p.nome FROM tb_privilegio p order by p.nome");
            ArrayList<String> lista = new ArrayList<String>();
            while (rs.next()) {
                lista.add(rs.getString("nome"));
            }
            rs.close();
            conexao.close();
            return lista;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public Login validaLogin(String login, String senha, String priv) {
        try {
            Login log = null;
            PreparedStatement pst = conexao.getConexao().prepareStatement("select l.*, p.nome as PRIVILEGIO from tb_login l, tb_privilegio p where p.id_privilegio = l.id_privilegio and trim(l.login) = ? and trim(l.senha) = ? and trim(p.nome) = ?");
            pst.setString(1, login.trim());
            pst.setString(2, senha.trim());
            pst.setString(3, priv.trim());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                log = new Login(rs.getString("LOGIN"), rs.getString("SENHA"), rs.getString("NOME"), rs.getString("PRIVILEGIO"));
            }
            rs.close();
            conexao.close();
            return log;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

}
