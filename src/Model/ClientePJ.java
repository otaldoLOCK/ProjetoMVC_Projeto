/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author Christian
 */
public class ClientePJ extends Cliente {

    private String cnpj;
    private int inscricaoEstadual;

    public ClientePJ(int id, Date dataFundacao, String razaoSocial, String cnpj, int inscricaoEstadual) {
        this.setIdCliente(id);
        this.setData(dataFundacao);
        this.setNome(razaoSocial);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }    
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public void setInscricaoEstadual(int inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public int getInscricaoEstadual() {
        return this.inscricaoEstadual;
    }

}
