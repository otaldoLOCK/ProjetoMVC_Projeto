/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

;
import Controller.ControllerCliente;
import Controller.ControllerProduto;
import Controller.ControllerVenda;
import Model.Cliente;
import Model.Produto;
import Model.Venda;
import Model.VendaItem;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christian
 */


public class frm_venda extends javax.swing.JFrame {
    
    private Venda venda = new Venda();
    
    public void inicializaControles() {
        txtDesconto.setText("0,00");
        jLabelTotalVenda.setText("Total Venda: 0,00");
        jLabelTotalVendaLiquida.setText("Total Venda Final: 0,00");
        jTextDataVenda.setSize(100, 20);
        jTextDataVenda.setText((new SimpleDateFormat("dd/MM/yyyy")).format(new Date()));
        jTextDataVenda.setEnabled(false);
        
        txtTroco.setText("0,00");
        txtTroco.setEditable(false);
        
        txtValorPago.setText("0,00");
        
        limpaCamposProduto();
        carregaClientes();
        carregaProdutos();
        carregaItens();
        createPopupMenu();
    }

    /**
     * Creates new form frm_venda
     */
    public frm_venda() {
        initComponents();
        this.setResizable(false);
        inicializaControles();
    }
    
    private void atualizaTotais() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        jLabelTotalVenda.setText("Total Venda: " + df.format(venda.getTotalVenda()));
        jLabelTotalVendaLiquida.setText("Total Venda Final: " + df.format(venda.getTotalVendaLiquida()));
        txtTroco.setText(df.format(venda.getValorTroco()));
    }
    
    private void carregaClientes() {
        jComboBoxPessoas.removeAllItems();
        ControllerCliente srv = new ControllerCliente();
        ArrayList<Cliente> clientes = srv.getClientes();
        for (int i = 0; i < clientes.size(); i++) {
            jComboBoxPessoas.addItem(clientes.get(i).toString());
        }
        jComboBoxPessoas.setSelectedItem(null);
    }
    
    private void carregaProdutos() {
        jComboBoxProdutos.removeAllItems();
        ControllerProduto srv = new ControllerProduto();
        ArrayList<Produto> produtos = srv.getProdutos();
        for (int i = 0; i < produtos.size(); i++) {
            jComboBoxProdutos.addItem(produtos.get(i).toString());
        }
        jComboBoxProdutos.setSelectedItem(null);
    }
    
    private void limpaCamposProduto() {
        jComboBoxProdutos.setSelectedItem(null);
        jTextQTD.setText("01");
    }
    
    private void adicionaItem() {
        String itemSelecionado = jComboBoxProdutos.getSelectedItem().toString();
        ControllerProduto srvProduto = new ControllerProduto();
        int posfinal = itemSelecionado.indexOf("-");
        itemSelecionado = itemSelecionado.substring(0, posfinal - 1);
        Produto p = srvProduto.getProduto(Integer.parseInt(itemSelecionado));
        VendaItem item = new VendaItem(venda.getNextItemCodigo(), Integer.parseInt(jTextQTD.getText()), p.getValor());
        item.setProduto(p);
        venda.adicionaItem(item);
        
        limpaCamposProduto();
        carregaItens();
        atualizaTotais();
    }
    
    private int carregaItens() {
        int retorno = 0;
        DefaultTableModel modelo = new DefaultTableModelPersonal();
        jTableItens.setModel(modelo);
        modelo.addColumn("ID");
        modelo.addColumn("Descrição");
        modelo.addColumn("Valor");
        modelo.addColumn("Quantidade");
        modelo.addColumn("Total");
        jTableItens.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTableItens.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTableItens.getColumnModel().getColumn(2).setPreferredWidth(20);
        jTableItens.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTableItens.getColumnModel().getColumn(4).setPreferredWidth(20);
        modelo.setNumRows(0);
        retorno = venda.getItens().size();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        for (VendaItem p : venda.getItens()) {
            modelo.addRow(new Object[]{p.getIdVendaItem(), p.getProduto().getDescricao(), df.format(p.getValorUnitario()), p.getQuantidade(), df.format(p.getValorUnitario() * p.getQuantidade())});
        }
        return retorno;
    }
    
    private void efetivaPedidoCompra() {
        ControllerVenda srv = new ControllerVenda();
        
        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dt = formatter.parse(jTextDataVenda.getText());
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, erro.toString());
        }
        
        String itemSelecionado = jComboBoxPessoas.getSelectedItem().toString();
        ControllerCliente srvCliente = new ControllerCliente();
        int posfinal = itemSelecionado.indexOf("-");
        itemSelecionado = itemSelecionado.substring(0, posfinal - 1);
        
        Cliente p = srvCliente.getCliente(Integer.parseInt(itemSelecionado));
        venda.setPessoa(p);
        venda.setDataVenda(dt);
        srv.persistirBanco(venda);
        
        JOptionPane.showMessageDialog(null, "Venda efetuada com sucesso!");
        venda = new Venda();
        inicializaControles();
    }
    
    private void createPopupMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem jMenuItemExcluir = new JMenuItem();
        jMenuItemExcluir.setText("Excluir");
        jMenuItemExcluir.addActionListener(
                new java.awt.event.ActionListener() {
            // Importe a classe java.awt.event.ActionEvent
            public void actionPerformed(ActionEvent e) {
                int index = jTableItens.getSelectedRow();
                venda.removeItem(index);
                carregaItens();
                atualizaTotais();
            }
        });
        jPopupMenu.add(jMenuItemExcluir);
        jTableItens.addMouseListener(
                new java.awt.event.MouseAdapter() {
            //Importe a classe java.awt.event.MouseEvent
            public void mouseClicked(MouseEvent e) {
                // Se o botão direito do mouse foi pressionado
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // Exibe o popup menu na posição do mouse.
                    jPopupMenu.show(jTableItens, e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelDataVenda = new javax.swing.JLabel();
        jTextDataVenda = new javax.swing.JTextField();
        jComboBoxPessoas = new javax.swing.JComboBox<>();
        jLabelPessoa = new javax.swing.JLabel();
        jComboBoxProdutos = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jButtonLancar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextQTD = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableItens = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButtonFechar = new javax.swing.JButton();
        jButtonVender = new javax.swing.JButton();
        jLabelTotalVendaLiquida = new javax.swing.JLabel();
        jLabelTotalVenda = new javax.swing.JLabel();
        jLabelDescontoVenda = new javax.swing.JLabel();
        txtDesconto = new javax.swing.JTextField();
        txtValorPago = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTroco = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Venda");

        jLabelDataVenda.setText("Data Venda :");

        jTextDataVenda.setText("23/12/2000");

        jComboBoxPessoas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabelPessoa.setText("Cliente :");

        jComboBoxProdutos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Produto :");

        jButtonLancar.setText("Lançar...");
        jButtonLancar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonLancarMouseClicked(evt);
            }
        });
        jButtonLancar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLancarActionPerformed(evt);
            }
        });

        jLabel2.setText("QTD: ");

        jTextQTD.setText("566");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelDataVenda)
                    .addComponent(jLabelPessoa)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxPessoas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBoxProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextQTD, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonLancar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDataVenda)
                    .addComponent(jTextDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxPessoas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPessoa))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonLancar)
                    .addComponent(jLabel2)
                    .addComponent(jTextQTD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableItens.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableItens.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableItensMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableItens);

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

        jButtonVender.setText("Efetivar Venda");
        jButtonVender.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonVenderMouseClicked(evt);
            }
        });
        jButtonVender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVenderActionPerformed(evt);
            }
        });

        jLabelTotalVendaLiquida.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelTotalVendaLiquida.setForeground(new java.awt.Color(51, 51, 255));
        jLabelTotalVendaLiquida.setText("total venda liquida");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButtonVender)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFechar))
                    .addComponent(jLabelTotalVendaLiquida, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabelTotalVendaLiquida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonFechar)
                    .addComponent(jButtonVender)))
        );

        jLabelTotalVenda.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelTotalVenda.setText("total Venda");

        jLabelDescontoVenda.setText("Desconto :");

        txtDesconto.setText("jTextField1");
        txtDesconto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDescontoFocusLost(evt);
            }
        });
        txtDesconto.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtDescontoInputMethodTextChanged(evt);
            }
        });
        txtDesconto.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtDescontoPropertyChange(evt);
            }
        });

        txtValorPago.setText("jTextField2");
        txtValorPago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValorPagoFocusLost(evt);
            }
        });

        jLabel3.setText("Valor Pago :");

        jLabel4.setForeground(new java.awt.Color(51, 0, 204));
        jLabel4.setText("Troco :");

        txtTroco.setForeground(new java.awt.Color(0, 0, 153));
        txtTroco.setText("jTextField3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabelDescontoVenda)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValorPago, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelTotalVenda)
                        .addContainerGap())
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelDescontoVenda)
                        .addComponent(txtValorPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(txtTroco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelTotalVenda))
                .addGap(9, 9, 9)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed

    }//GEN-LAST:event_jButtonFecharActionPerformed

    private void jButtonFecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFecharMouseClicked
        this.setVisible(false);
    }//GEN-LAST:event_jButtonFecharMouseClicked

    private void jButtonLancarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonLancarMouseClicked
        adicionaItem();
    }//GEN-LAST:event_jButtonLancarMouseClicked

    private void jButtonVenderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonVenderMouseClicked
        efetivaPedidoCompra();
    }//GEN-LAST:event_jButtonVenderMouseClicked

    private void jButtonLancarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLancarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLancarActionPerformed

    private void txtDescontoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtDescontoPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_txtDescontoPropertyChange

    private void txtDescontoInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtDescontoInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescontoInputMethodTextChanged

    private void txtDescontoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescontoFocusLost
        // TODO add your handling code here:
        try {
            if (evt.getSource() == txtDesconto) {
                String valor = txtDesconto.getText();
                valor = valor.replace(".", "");
                valor = valor.replace(",", ".");
                if (Float.parseFloat(valor) > 0) {
                    venda.setDesconto(Float.parseFloat(valor));
                } else {
                    venda.setDesconto(0);
                }
            }
        } catch (Exception u) {
            venda.setDesconto(0);
        }
        atualizaTotais();
    }//GEN-LAST:event_txtDescontoFocusLost

    private void txtValorPagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorPagoFocusLost
        // TODO add your handling code here:
        try {
            if (evt.getSource() == txtValorPago) {
                String valor = txtValorPago.getText();
                valor = valor.replace(".", "");
                valor = valor.replace(",", ".");
                if (Float.parseFloat(valor) > 0) {
                    venda.setValorPago(Float.parseFloat(valor));
                } else {
                    venda.setValorPago(0);
                }
            }
        } catch (Exception u) {
            venda.setValorPago(0);
        }
        atualizaTotais();
    }//GEN-LAST:event_txtValorPagoFocusLost

    private void jButtonVenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonVenderActionPerformed

    private void jTableItensMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableItensMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableItensMouseClicked

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
            java.util.logging.Logger.getLogger(frm_venda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_venda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_venda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_venda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_venda().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JButton jButtonLancar;
    private javax.swing.JButton jButtonVender;
    private javax.swing.JComboBox<String> jComboBoxPessoas;
    private javax.swing.JComboBox<String> jComboBoxProdutos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelDataVenda;
    private javax.swing.JLabel jLabelDescontoVenda;
    private javax.swing.JLabel jLabelPessoa;
    private javax.swing.JLabel jLabelTotalVenda;
    private javax.swing.JLabel jLabelTotalVendaLiquida;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableItens;
    private javax.swing.JTextField jTextDataVenda;
    private javax.swing.JTextField jTextQTD;
    private javax.swing.JTextField txtDesconto;
    private javax.swing.JTextField txtTroco;
    private javax.swing.JTextField txtValorPago;
    // End of variables declaration//GEN-END:variables
}
