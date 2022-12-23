/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Christian
 */
public class Cliente {

    private int idCliente;
    private Date data;
    private String nome;

    /* public Pessoa(int id, Date dataNasc, String nome) {
        this.idPessoa = id;
        this.dataNascimento = dataNasc;
        this.nome = nome;
    }*/
    public int getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(int id) {
        this.idCliente = id;
    }

    public int getIdade() {
        if (data != null) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            int anoNascimento = Integer.parseInt(df.format(data));
            return (cal.get(Calendar.YEAR) - anoNascimento);
        }
        return 0;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getData() {
        return this.data;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }
    
    @Override
    public String toString() {
        return this.getIdCliente() + " - " + this.getNome();
    }
}