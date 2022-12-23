/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Christian
 */
public class Login {

    private String login;
    private String senha;
    private String nome;
    private String privilegio;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrivilegio(String privilegio) {
        this.privilegio = privilegio;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public String getPrivilegio() {
        return privilegio;
    }

    public Login(String login, String senha, String nome, String privilegio) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.privilegio = privilegio;
    }

}
