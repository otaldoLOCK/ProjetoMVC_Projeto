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
public class Produto {

    private int idProduto;
    private String descricao;
    private float valor;

    public void setIdProduto(int id) {
        this.idProduto = id;
    }

    public int getIdProduto() {
        return this.idProduto;
    }

    public void setDescricao(String desc) {
        this.descricao = desc;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getValor() {
        return this.valor;
    }
    
    public Produto (int id, String desc, float valor) {
        this.setIdProduto(id);
        this.setDescricao(desc);
        this.setValor(valor);
    }
    
    @Override
    public String toString() {
        return this.getIdProduto() + " - " + this.getDescricao();
    }
}