/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.ControllerCliente;
import Model.Cliente;
import Model.ClientePF;
import Model.ClientePJ;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christian
 */
public class frm_cad_cliente extends javax.swing.JFrame {

    private int quantCad = 0;

    final int MODO_INSERT = 0;
    final int MODO_UPDATE = 1;
    final int MODO_DELETE = 2;
    private int modoTela = -1;

    private void habilitaCampos(boolean habilita) {
        jTextFieldID.setEnabled(false);
        jTextFieldNome.setEnabled(habilita);
        jTextFieldData.setEnabled(habilita);
        jTextCPF.setEnabled(habilita);
        jTextCNPJ.setEnabled(habilita);
        jTextIE.setEnabled(habilita);
        jTextRG.setEnabled(habilita);
        jRadioPF.setEnabled(habilita);
        jRadioPJ.setEnabled(habilita);
        jPanelPF.setEnabled(habilita);
        jPanelPJ.setEnabled(habilita);
    }

    private boolean emModoInsertUpdate() {
        return ((modoTela == 0) || (modoTela == 1));
    }

    public void inicializaControles() {
        limparTela();
        quantCad = carregaClientes();
        preparaBotoes();
    }

    /**
     * Creates new form frm_cad_pessoa
     */
    public frm_cad_cliente() {
        initComponents();
        this.setResizable(false);
        inicializaControles();
    }

    private Cliente clienteSelecionada() {
        int idCliente = 0;
        if (modoTela != MODO_INSERT) {
            idCliente = Integer.parseInt(jTextFieldID.getText());
        }
        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dt = formatter.parse(jTextFieldData.getText());
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, erro.toString());
        }

        Cliente p = null;
        if (jRadioPF.isSelected()) {
            p = new ClientePF(idCliente, dt, jTextFieldNome.getText(), jTextCPF.getText(), Integer.parseInt(jTextRG.getText()));
        }

        if (jRadioPJ.isSelected()) {
            p = new ClientePJ(idCliente, dt, jTextFieldNome.getText(), jTextCNPJ.getText(), Integer.parseInt(jTextIE.getText()));
        }
        return p;
    }

    private void limparTela() {
        jTextFieldID.setText("");
        jTextFieldNome.setText("");
        jTextFieldData.setText("");
        jTextCPF.setText("");
        jTextCNPJ.setText("");
        jTextIE.setText("");
        jTextRG.setText("");
        jRadioPF.setSelected(false);
        jRadioPJ.setSelected(false);
        jPanelPF.setVisible(false);
        jPanelPJ.setVisible(false);
    }

    private void mostraDadosSelecionando() {
        jTextFieldID.setText(String.valueOf(jTPessoas.getValueAt(jTPessoas.getSelectedRow(), 0))); // ID
        jTextFieldNome.setText(String.valueOf(jTPessoas.getValueAt(jTPessoas.getSelectedRow(), 2))); // NOME/RAZAO SOCIAL

        ControllerCliente srv = new ControllerCliente();
        Cliente p = srv.getCliente(Integer.parseInt(String.valueOf(jTPessoas.getValueAt(jTPessoas.getSelectedRow(), 0))));
        if (p instanceof ClientePJ) {
            jRadioPJ.setSelected(true);
            jRadioPF.setSelected(false);
            jPanelPJ.setVisible(true);
            jPanelPF.setVisible(false);
            ClientePJ pj = (ClientePJ) p;
            jTextCNPJ.setText(pj.getCnpj());
            jTextIE.setText(String.valueOf(pj.getInscricaoEstadual()));
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            jTextFieldData.setText(df.format(pj.getData()));
        }
        if (p instanceof ClientePF) {
            jRadioPF.setSelected(true);
            jRadioPJ.setSelected(false);
            jPanelPF.setVisible(true);
            jPanelPJ.setVisible(false);
            ClientePF pf = (ClientePF) p;
            jTextCPF.setText(pf.getCPF());
            jTextRG.setText(String.valueOf(pf.getIdentidade()));
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            jTextFieldData.setText(df.format(pf.getData()));
        }
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
        ControllerCliente srv = new ControllerCliente();
        int reply = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o registro?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            Cliente p = clienteSelecionada();
            srv.persistirBanco(p, false, false, true);
            quantCad = carregaClientes();
        }
    }

    private boolean validaCampos() {
        if (jTextFieldNome.getText().equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(null, "Entre com o Nome/Razão Social!");
            jTextFieldNome.requestFocus();
            return false;
        }

        if (jTextFieldData.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Entre com a Data de Nascimento/Fundação");
            jTextFieldData.requestFocus();
            return false;
        }

        if (jRadioPF.isSelected()) {

            if ((jTextCPF.getText().isEmpty()) || (jTextCPF.getText().length() != 11)) {
                JOptionPane.showMessageDialog(null, "Entre com CPF");
                jTextCPF.requestFocus();
                return false;
            }
            if (jTextRG.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Entre com RG");
                jTextRG.requestFocus();
                return false;
            }
        }

        if (jRadioPJ.isSelected()) {

            if ((jTextCNPJ.getText().isEmpty()) || (jTextCNPJ.getText().length() != 14)) {
                JOptionPane.showMessageDialog(null, "Entre com CNPJ");
                jTextCNPJ.requestFocus();
                return false;
            }
            if (jTextIE.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Entre com Inscrição Estadual");
                jTextIE.requestFocus();
                return false;
            }
        }
        return true;
    }

    private void modoSalva() {
        if (!validaCampos()) {
            return;
        }
        try {
            ControllerCliente srv = new ControllerCliente();
            Cliente p = clienteSelecionada();
            srv.persistirBanco((Cliente) p, modoTela == MODO_INSERT, modoTela == MODO_UPDATE, false);
            modoTela = -1;
            preparaBotoes();
            quantCad = carregaClientes();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, erro.toString());
        }
    }

    private int carregaClientes() {
        int retorno = 0;
        ControllerCliente srvBancoPessoa = new ControllerCliente();

        DefaultTableModel modelo = new DefaultTableModelPersonal();
        jTPessoas.setModel(modelo);
        modelo.addColumn("ID");
        modelo.addColumn("CNPJ/CPF");
        modelo.addColumn("Nome");
        jTPessoas.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTPessoas.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTPessoas.getColumnModel().getColumn(2).setPreferredWidth(20);
        modelo.setNumRows(0);

        ArrayList<Cliente> lista = srvBancoPessoa.getClientes();
        retorno = lista.size();
        for (Cliente p : lista) {
            if (p instanceof ClientePF) {
                ClientePF pes = (ClientePF) p;
                modelo.addRow(new Object[]{pes.getIdCliente(), pes.getCPF(), pes.getNome()});
            }
            if (p instanceof ClientePJ) {
                ClientePJ pes = (ClientePJ) p;
                modelo.addRow(new Object[]{pes.getIdCliente(), pes.getCnpj(), pes.getNome()});
            }
        }
        return retorno;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldID = new javax.swing.JTextField();
        jTextFieldNome = new javax.swing.JTextField();
        jTextFieldData = new javax.swing.JTextField();
        jRadioPF = new javax.swing.JRadioButton();
        jRadioPJ = new javax.swing.JRadioButton();
        jPanelPF = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextCPF = new javax.swing.JTextField();
        jTextRG = new javax.swing.JTextField();
        jPanelPJ = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextCNPJ = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextIE = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jBNOVO = new javax.swing.JButton();
        jBALTERAR = new javax.swing.JButton();
        jBSALVAR = new javax.swing.JButton();
        jBEXCLUIR = new javax.swing.JButton();
        jButtonFechar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTPessoas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cadastro de Cliente");

        jPanel1.setEnabled(false);

        jLabel1.setText("ID :");

        jLabel2.setText("Nome/Razão Social :");

        jLabel3.setText("Dt. Nascimento/Fundação :");

        jTextFieldID.setText("jTextField1");

        jTextFieldNome.setText("jTextField2");

        jTextFieldData.setText("jTextField3");

        jRadioPF.setText("Física");
        jRadioPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioPFActionPerformed(evt);
            }
        });

        jRadioPJ.setText("Jurídica");
        jRadioPJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioPJActionPerformed(evt);
            }
        });

        jLabel4.setText("CPF :");

        jLabel5.setText("RG :");

        jTextCPF.setText("jTextField1");

        jTextRG.setText("jTextField2");

        javax.swing.GroupLayout jPanelPFLayout = new javax.swing.GroupLayout(jPanelPF);
        jPanelPF.setLayout(jPanelPFLayout);
        jPanelPFLayout.setHorizontalGroup(
            jPanelPFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPFLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextRG, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanelPFLayout.setVerticalGroup(
            jPanelPFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPFLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jTextRG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPFLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jTextCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setText("CNPJ :");

        jTextCNPJ.setText("jTextField1");

        jLabel7.setText("IE :");

        jTextIE.setText("jTextField2");

        javax.swing.GroupLayout jPanelPJLayout = new javax.swing.GroupLayout(jPanelPJ);
        jPanelPJ.setLayout(jPanelPJLayout);
        jPanelPJLayout.setHorizontalGroup(
            jPanelPJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPJLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextCNPJ, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextIE, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanelPJLayout.setVerticalGroup(
            jPanelPJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPJLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jTextIE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jTextCNPJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldData, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioPF)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioPJ))
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelPJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(635, 635, 635))
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
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioPF)
                    .addComponent(jRadioPJ))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelPJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBNOVO)
                    .addComponent(jBALTERAR)
                    .addComponent(jBSALVAR)
                    .addComponent(jBEXCLUIR)
                    .addComponent(jButtonFechar)))
        );

        jTPessoas.setModel(new javax.swing.table.DefaultTableModel(
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
        jTPessoas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTPessoasMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTPessoasMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTPessoas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 808, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 808, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBNOVOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBNOVOMouseClicked
        modoInclusao();
    }//GEN-LAST:event_jBNOVOMouseClicked

    private void jBALTERARMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBALTERARMouseClicked
        modoAltercao();
    }//GEN-LAST:event_jBALTERARMouseClicked

    private void jBSALVARMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBSALVARMouseClicked
        modoSalva();
    }//GEN-LAST:event_jBSALVARMouseClicked

    private void jBEXCLUIRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBEXCLUIRMouseClicked
        modoExclusao();
    }//GEN-LAST:event_jBEXCLUIRMouseClicked

    private void jButtonFecharMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFecharMouseClicked
        this.setVisible(false);
    }//GEN-LAST:event_jButtonFecharMouseClicked

    private void jTPessoasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTPessoasMouseClicked
        // TODO add your handling code here:
        mostraDadosSelecionando();
    }//GEN-LAST:event_jTPessoasMouseClicked

    private void jTPessoasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTPessoasMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTPessoasMouseEntered

    private void jRadioPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioPFActionPerformed
        jPanelPF.setVisible(jRadioPF.isSelected());
        jRadioPJ.setSelected(!jRadioPF.isSelected());
        jPanelPJ.setVisible(!jRadioPF.isSelected());
    }//GEN-LAST:event_jRadioPFActionPerformed

    private void jRadioPJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioPJActionPerformed
        jPanelPJ.setVisible(jRadioPJ.isSelected());
        jRadioPF.setSelected(!jRadioPJ.isSelected());
        jPanelPF.setVisible(!jRadioPJ.isSelected());
    }//GEN-LAST:event_jRadioPJActionPerformed

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed

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
            java.util.logging.Logger.getLogger(frm_cad_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frm_cad_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frm_cad_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frm_cad_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frm_cad_cliente().setVisible(true);
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelPF;
    private javax.swing.JPanel jPanelPJ;
    private javax.swing.JRadioButton jRadioPF;
    private javax.swing.JRadioButton jRadioPJ;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTPessoas;
    private javax.swing.JTextField jTextCNPJ;
    private javax.swing.JTextField jTextCPF;
    private javax.swing.JTextField jTextFieldData;
    private javax.swing.JTextField jTextFieldID;
    private javax.swing.JTextField jTextFieldNome;
    private javax.swing.JTextField jTextIE;
    private javax.swing.JTextField jTextRG;
    // End of variables declaration//GEN-END:variables
}
