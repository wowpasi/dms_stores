package com.orien.dms.gui;

import com.formdev.flatlaf.ui.FlatListCellBorder;
import com.orien.dms.main.Login;
import com.orien.dms.model.InvoiceJasp;
import com.orien.dms.model.MySQL;
import java.awt.Color;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Asus
 */
public class CashierDashboard extends javax.swing.JPanel {

    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private static JPanel jPanel;

    int stock_Id = 0;

    public CashierDashboard(JPanel jPanel) {
        initComponents();
//         jTextField1.requestFocus();
        this.jPanel = jPanel;
        setSize(jPanel.getSize());
        revalidate();
        repaint();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jTextField1.requestFocus();
            }
        });

    }

    public void clearData() {
        jTextField1.setText("");
        jTextField2.setText("");
        jLabel3.setText("None");
        jLabel4.setText("0.00");
        jLabel8.setText("None");
        jLabel10.setText("0000-00-00");
        jLabel11.setText("0000-00-00");
        jLabel19.setText("0.00");
        jLabel6.setText("Quantity :");
    }

    public void setData() {
        String code = jTextField1.getText();

        if (code.length() > 0) {
            try {
                ResultSet rs = MySQL.search("SELECT *FROM `stock`INNER JOIN `product` ON `stock`.`product_id`=`product`.`id`INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id`INNER JOIN `category` ON `product`.`category_id`=`category`.`id` WHERE `product`.`bar_code`='" + code + "' AND `stock`.`qty`>0 ORDER BY `stock`.`exd` ASC");

                if (rs.next()) {
                    jLabel3.setText(rs.getString("product.name"));
                    jLabel4.setText(decimalFormat.format(Double.parseDouble(rs.getString("stock.selling_price"))));
                    jLabel8.setText(rs.getString("brand.name"));
                    jLabel10.setText(rs.getString("stock.mfd"));
                    jLabel11.setText(rs.getString("stock.exd"));
                    stock_Id = Integer.parseInt(rs.getString("stock.id"));

                    if (rs.getString("brand.name").equalsIgnoreCase("no brand")) {
                        jLabel6.setText("Weight (g):");
                    } else {
                        jLabel6.setText("Quantity :");
                    }

                } else {

                    ResultSet rs1 = MySQL.search("SELECT *FROM `product` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id`INNER JOIN `category` ON `product`.`category_id`=`category`.`id` WHERE `product`.`bar_code`='" + code + "'");;
                    if (rs1.next()) {
                        JOptionPane.showMessageDialog(this, "Out of Stock.... : " + rs1.getString("product.name"), "warning", JOptionPane.WARNING_MESSAGE);
                        clearData();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadDataWithQty(java.awt.event.KeyEvent evt) {

        String qty = jTextField2.getText();
        String text = qty + evt.getKeyChar();
        Double price = Double.valueOf(jLabel4.getText());
        Double total = 0.00;
        String brand = jLabel8.getText();
        String label = jLabel6.getText();

        if (qty.isEmpty()) {
            jLabel19.setText("0.00");
            JOptionPane.showMessageDialog(this, "This value cannot be zero", "warning", JOptionPane.WARNING_MESSAGE);

        } else {
            if (!jLabel3.getText().equalsIgnoreCase("none")) {
                try {
                    String code = jTextField1.getText();
                    ResultSet rs = MySQL.search("SELECT *FROM `stock`INNER JOIN `product` ON `stock`.`product_id`=`product`.`id`INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id`INNER JOIN `category` ON `product`.`category_id`=`category`.`id`WHERE `product`.`bar_code`='" + code + "'");

                    Long quantity = Long.parseLong(qty);

                    if (label.equalsIgnoreCase("weight (g):")) {
                        System.out.println("Done");
                        if (rs.next()) {
                            if (evt.getKeyCode() == 8) {
                                if (qty.length() > 0) {
                                    total = (quantity * price) / 1000;
                                    jLabel19.setText(decimalFormat.format(total));
                                } else {
                                    clearData();
                                }
                            } else {
                                int stQty = rs.getInt("stock.qty");
                                total = (quantity * price) / 1000;
                                if (stQty < quantity) {
                                    JOptionPane.showMessageDialog(this, "The stock have maximum " + stQty + " products.", "Warning", JOptionPane.WARNING_MESSAGE);
                                    jTextField2.setText("100");
                                    total = (1 * price) / 10;
                                }
                                jLabel19.setText(decimalFormat.format(total));
                            }
                        }

                    } else {

                        if (rs.next()) {
                            if (evt.getKeyCode() == 8) {
                                if (qty.length() > 0) {
                                    total = quantity * price;
                                    jLabel19.setText(decimalFormat.format(total));
                                } else {
                                    clearData();
                                }
                            } else {

                                int stQty = rs.getInt("stock.qty");
                                total = quantity * price;
                                if (stQty < quantity) {
                                    JOptionPane.showMessageDialog(this, "The stock have maximum " + stQty + " products.", "Warning", JOptionPane.WARNING_MESSAGE);
                                    jTextField2.setText("1");
                                    total = 1 * price;
                                }
                                jLabel19.setText(decimalFormat.format(total));
                            }

                        } else {
                            jTextField2.setText("");
                            JOptionPane.showMessageDialog(this, "Error", "warning", JOptionPane.WARNING_MESSAGE);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                jTextField2.setText("");
                JOptionPane.showMessageDialog(this, "Firstly, Please set the product", "warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void loadTotal() {
        Double total = 0.00;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            total += Double.parseDouble(jTable1.getValueAt(i, 5).toString());
        }
        jLabel13.setText(decimalFormat.format(total));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Barcode :");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField1InputMethodTextChanged(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Product :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("None");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setText("0.00");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Per Price :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("Quantity :");

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Brand :");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setText("None");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setText("MF Date:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("0000-00-00");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setText("0000-00-00");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setText("EX Date :");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jButton1.setText("Add to Invoice");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setText("Total Price :");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setText("0.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 198, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18))))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Id", "Barcode", "Product", "Brand", "Quantity", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
        }

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 153, 51));
        jLabel13.setText("0.00");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel14.setText("Item Total :");

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jButton2.setText("Print Invoice");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        if (jTextField1.getText().length() == 0) {
            clearData();
        } else {
            setData();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

// should check same product with selling & mfd & exd and update same from the table
// table need sellingprice mxd exd
        String code = jTextField1.getText();
        String qty = jTextField2.getText();
        String product = jLabel3.getText();
        String value = jLabel4.getText();
        String total = jLabel19.getText();

        int trc = jTable1.getRowCount();

        String stid = String.valueOf(stock_Id);

        String dupInv = null;

        int i;
        int j = 0;

        for (i = 0; i < trc; i++) {

            String tbcvalue = jTable1.getModel().getValueAt(i, 0).toString();

            if (tbcvalue.equalsIgnoreCase(stid)) {
                dupInv = "dublicate product in invoice";
//                System.out.println(dupInv + "," + i);
                j = i;
            }
        }

        if (code.isEmpty() || stock_Id == 0) {
            JOptionPane.showMessageDialog(this, "Please set barcode", "warning", JOptionPane.WARNING_MESSAGE);
        } else if (qty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity or weight", "warning", JOptionPane.WARNING_MESSAGE);
        } else if (dupInv == "dublicate product in invoice") {
            String tbqty = jTable1.getModel().getValueAt(j, 4).toString();
            String tbtot = jTable1.getModel().getValueAt(j, 5).toString();
            jTable1.getModel().setValueAt(Integer.parseInt(tbqty) + Integer.parseInt(qty), j, 4);
            jTable1.getModel().setValueAt(Double.parseDouble(tbtot) + Double.parseDouble(total), j, 5);
            dupInv = null;
            j = 0;
            clearData();
            loadTotal();
            jTextField1.requestFocus();
        } else {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();

            Vector v = new Vector();
            v.add(stock_Id);
            v.add(code);
            v.add(product);
            v.add(value);
            v.add(qty);
            v.add(total);
            dtm.addRow(v);

            clearData();
            loadTotal();
            jTextField1.requestFocus();

//            JOptionPane.showMessageDialog(this, "Added to Invoice", "success", JOptionPane.INFORMATION_MESSAGE);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed

    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        String qty = jTextField2.getText();
        String text = qty + evt.getKeyChar();

        if (!Pattern.compile("[1-9][0-9]*").matcher(text).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased

        loadDataWithQty(evt);

    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField1InputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField1InputMethodTextChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        String btotal = jLabel13.getText();

        Vector v = null;
        HashMap parameter = null;
        JasperPrint jp = null;
        JRBeanCollectionDataSource datasource = null;
        InputStream pathStream = null;
        JasperReport jr = null;

        try {

//            pathStream = StudentPayment.class.getResourceAsStream("/report/Invoice.jasper");
            jr = JasperCompileManager.compileReport("src/com/orien/dms/reports/Invoice_20.jrxml");

            v = new Vector();

            if (jTable1.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Pleasse Add Product", "warning", JOptionPane.WARNING_MESSAGE);
            } else {

                long mTime = System.currentTimeMillis();

                String uniqueId = mTime + "-" + Login.userNic;
                String uniqueId2 = mTime + Login.userId;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dNow = sdf.format(new Date());

                SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");
                String tNow = stf.format(new Date());

                MySQL.iud("INSERT INTO `invoice` (`id`,`date`,`time`,`total`,`bill_status_id`,`user_nic`,`unique_id`) VALUES ('" + uniqueId2 + "','" + dNow + "','" + tNow + "','" + btotal + "','1','" + Login.userNic + "','" + uniqueId + "')");

                ResultSet rs1 = MySQL.search("SELECT * FROM `invoice` WHERE `unique_id`='" + uniqueId + "'");
                rs1.next();
                String id = rs1.getString("id");

//            MySQL.iud("INSERT INTO `invoice_payment` (`invoice_id`,`payment`,`balance`) VALUES ('" + id + "','" + payment + "','" + balance + "')");
                ResultSet rs3 = MySQL.search("SELECT * FROM `user` WHERE `nic`='" + Login.userNic + "'");
                rs3.next();
//                String uName = rs3.getString("first_name" + " " + "last_name");

                for (int i = 0; i < jTable1.getRowCount(); i++) {

                    String sid = jTable1.getValueAt(i, 0).toString();
//                    String bNo = jTable1.getValueAt(i, 1).toString();
                    String product = jTable1.getValueAt(i, 2).toString();
                    String value = jTable1.getValueAt(i, 3).toString();
                    String qty = jTable1.getValueAt(i, 4).toString();
                    String total = jTable1.getValueAt(i, 5).toString();

                    ResultSet rs4 = MySQL.search("SELECT * FROM `stock` WHERE `stock`.`id`='" + sid + "'");
                    rs4.next();

                    String availableQty = rs4.getString("qty");

                    int upadatedQty = (Integer.parseInt(availableQty) - Integer.parseInt(qty));

                    MySQL.iud("UPDATE `stock` SET `qty`='" + upadatedQty + "' WHERE `id`='" + sid + "'");

                    MySQL.iud("INSERT INTO `invoice_item` (`qty` ,`total`, `invoice_id`,`stock_id`) VALUES ('" + qty + "','" + total + "','" + id + "','" + sid + "')");

                    String subTotal = jLabel13.getText();

                    parameter = new HashMap();
                    v.add(new InvoiceJasp(uniqueId2, Login.userName, product, qty, value, total, subTotal, String.valueOf(i + 1)));

                }

                datasource = new JRBeanCollectionDataSource(v);
                jp = JasperFillManager.fillReport(jr, parameter, datasource);

                JasperViewer jv = new JasperViewer(jp, false);
                jv.setVisible(true);
                JasperPrintManager.printReport(jp, true);

                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);
                jLabel13.setText("0.00");

//                JOptionPane.showMessageDialog(this, "New Invoice Created", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        int i = jTable1.getSelectedRow();

        if (evt.getClickCount() == 2) {

            if (i == -1) {
                JOptionPane.showMessageDialog(this, "Pleasse select a Invoice Item", "warning", JOptionPane.WARNING_MESSAGE);
            } else {

                int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {

                    DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                    dtm.removeRow(i);

                    jTextField1.requestFocus();

                    JOptionPane.showMessageDialog(this, "Invoice Item Removed", "Succsess", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

    }//GEN-LAST:event_jTable1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
