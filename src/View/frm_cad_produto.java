/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.ControllerProduto;
import Model.Produto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christian
 */
public class frm_cad_produto extends javax.swing.JFrame {

    private int quantCad = 0;

    final int MODO_INSERT = 0;
    final int MODO_UPDATE = 1;
    final int MODO_DELETE = 2;
    private int modoTela = -1;

    /**
     * Creates new form frm_cad_produto
     */
    private boolean emModoInsertUpdate() {
        return ((modoTela == 0) || (modoTela == 1));
    }

    public void inicializaControles() {
        limparTela();
        quantCad = carregaProdutos();
        preparaBotoes();
    }

    public frm_cad_produto() {
        initComponents();
        this.setResizable(false);
        inicializaControles();
    }

    private void limparTela() {
        jTextFieldID.setText("");
        jTextFieldDescricao.setText("");
        jTextFieldValor.setText("");
    }

    private void habilitaCampos(boolean habilita) {
        jTextFieldID.setEnabled(false);
        jTextFieldDescricao.setEnabled(habilita);
        jTextFieldValor.setEnabled(habilita);
    }

    private void mostraDadosSelecionando() {
        jTextFieldID.setText(String.valueOf(jTProdutos.getValueAt(jTProdutos.getSelectedRow(), 0)));
        jTextFieldDescricao.setText(String.valueOf(jTProdutos.getValueAt(jTProdutos.getSelectedRow(), 1)));
        jTextFieldValor.setText(String.valueOf(jTProdutos.getValueAt(jTProdutos.getSelectedRow(), 2)));
    }

    private void preparaBotoes() {
        jBNOVO.setEnabled(!emModoInsertUpdate());
        jBALTERAR.setEnabled((!emModoInsertUpdate()) && (quantCad > 0));
        jBEXCLUIR.setEnabled((!emModoInsertUpdate()) && (quantCad > 0));
        jBSALVAR.setEnabled(emModoInsertUpdate());
        habilitaCampos(emModoInsertUpdate());
    }

    private void modoInclusao() {
        modoTela = MODO_INSERT;
        preparaBotoes();
        limparTela();
    }

    private void modoAltercao() {
        modoTela = MODO_UPDATE;
        preparaBotoes();
    }

    private void modoExclusao() {
        ControllerProduto srv = new ControllerProduto();
        int reply = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o registro?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            Produto p = new Produto(Integer.parseInt(String.valueOf(jTProdutos.getValueAt(jTProdutos.getSelectedRow(), 0))), String.valueOf(jTProdutos.getValueAt(jTProdutos.getSelectedRow(), 1)), Float.valueOf(jTextFieldValor.getText().replace(".", "").replace(",", ".")));
            srv.persistirBanco(p, false, false, true);
            quantCad = carregaProdutos();
        }
    }

    private boolean validaCampos() {
        if (jTextFieldDescricao.getText().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(null, "Entre com a descrição do produto!");
            jTextFieldDescricao.requestFocus();
            return false;
        }

        if (jTextFieldValor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Entre com o valor do produto!");
            jTextFieldValor.requestFocus();
            return false;
        }
        return true;
    }

    private void modoSalva() {
        if (!validaCampos()) {
            return;
        }
        ControllerProduto srv = new ControllerProduto();

        int idProduto = 0;
        if (modoTela != MODO_INSERT) {
            idProduto = Integer.parseInt(jTextFieldID.getText());
        }
        String desc = jTextFieldDescricao.getText();
        float valor = Float.valueOf(jTextFieldValor.getText().replace(".", "").replace(",", "."));

        Produto p = new Produto(idProduto, desc, valor);
        srv.persistirBanco(p, modoTela == MODO_INSERT, modoTela == MODO_UPDATE, false);
        modoTela = -1;
        preparaBotoes();
        quantCad = carregaProdutos();
    }

    private int carregaProdutos() {
        int retorno = 0;
        ControllerProduto srvBancoProduto = new ControllerProduto();

        DefaultTableModel modelo = new DefaultTableModelPersonal();
        jTProdutos.setModel(modelo);
        modelo.addColumn("ID");
        modelo.addColumn("Descrição");
        modelo.addColumn("Valor");
        /*jTProdutos.getColumnModel().getColumn(0).setMinWidth(0);
        jTProdutos.getColumnModel().getColumn(0).setMaxWidth(0);*/
        jTProdutos.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTProdutos.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTProdutos.getColumnModel().getColumn(2).setPreferredWidth(20);
        modelo.setNumRows(0);

        DecimalFormat df = new DecimalFormat("#,##0.00");
        ArrayList<Produto> lista = srvBancoProduto.getProdutos();
        retorno = lista.size();
        for (Produto p : lista) {
            modelo.addRow(new Object[]{p.getIdProduto(), p.getDescricao(), df.format(p.getValor())});
        }
        return retorno;
    }

    /**
     * This method is called from within the constructors to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldID = new javax.swing.JTextField();
        jTextFieldDescricao = new javax.swing.JTextField();
        jTextFieldValor = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jBNOVO = new javax.swing.JButton();
        jBALTERAR = new javax.swing.JButton();
        jBSALVAR = new javax.swing.JButton();
        jBEXCLUIR = new javax.swing.JButton();
        jButtonFechar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTProdutos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cadastro de produtos");

        jPanel1.setEnabled(false);

        jLabel1.setText("ID :");

        jLabel2.setText("Descrição :");

        jLabel3.setText("Valor :");

        jTextFieldID.setText("jTextField1");

        jTextFieldDescricao.setText("jTextField2");

        jTextFieldValor.setText("jTextField3");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldDescricao)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 374, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBNOVO.setText("NOVO");
        jBNOVO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBNOVOMouseClicked(evt);
            }
        });

        jBALTERAR.setText("ALTERAR");
        jBALTERAR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBALTERARMouseClicked(evt);
            }
        });

        jBSALVAR.setText("SALVAR");
        jBSALVAR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBSALVARMouseClicked(evt);
            }
        });

        jBEXCLUIR.setText("EXCLUIR");
        jBEXCLUIR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBEXCLUIRMouseClicked(evt);
            }
        });

        jButtonFechar.setText("Fechar");
        jButtonFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonFecharMouseClicked(evt);
            }
        });
        jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jBNOVO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBALTERAR)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBEXCLUIR)
                .addGap(45, 45, 45)
                .addComponent(jBSALVAR)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonFechar)
                .addGap(39, 39, 39))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 9, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBNOVO)
                    .addComponent(jBALTERAR)
                    .addComponent(jBSALVAR)
                    .addComponent(jBEXCLUIR)
                    .addComponent(jButtonFechar)))
        );

        jTProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTProdutosMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTProdutosMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTProdutos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTProdutosMouseClicked
        // TODO add your handling code here:
        mostraDadosSelecionando();
    }//GEN-LAST:event_jTProdutosMouseClicked

    private void jTProdutosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTProdutosMouseEntered
        // TODO add your handling code here:

    }//GEN-LAST:event_jTProdutosMouseEntered

    private void jBNOVOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBNOVOMouseClicked
        modoInclusao();
    }//GEN-LAST:event_jBNOVOMouseClicked

    private void jBEXCLUIRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBEXCLUIRMouseClicked
        modoExclusao();
    }//GEN-LAST:event_jBEXCLUIRMouseClicked

    private void jBALTERARMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBALTERARMouseClicked
        modoAltercao();
    }//GEN-LAST:event_jBALTERARMouseClicked

    private void jBSALVARMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBSALVARMouseClicked
        modoSalva();
    }//GEN-LAST:event_jBSALVARMouseClicked

    private void jButtonFecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFecharMouseClicked
        this.setVisible(false);
    }//GEN-LAST:event_jButtonFecharMouseClicked

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonFecharActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frm_cad_produto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_cad_produto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_cad_produto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_cad_produto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_cad_produto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBALTERAR;
    private javax.swing.JButton jBEXCLUIR;
    private javax.swing.JButton jBNOVO;
    private javax.swing.JButton jBSALVAR;
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTProdutos;
    private javax.swing.JTextField jTextFieldDescricao;
    private javax.swing.JTextField jTextFieldID;
    private javax.swing.JTextField jTextFieldValor;
    // End of variables declaration//GEN-END:variables
}
