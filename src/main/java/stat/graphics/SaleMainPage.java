package stat.graphics;

import stat.controllers.SaleController;
import stat.domain.Sale;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Created by Eray Tuncer S000926 eray.tuncer@ozu.edu.tr
 */

@Component
// Required to not run this class in a test environment
@ConditionalOnProperty(value = "java.awt.headless", havingValue = "false")
public class SaleMainPage extends Page {

    @Autowired
    private SaleController saleController;

    @Autowired
    private SaleAddPage pageNewSale;

    private JTable saleTable;
    private ArrayList<Integer> saleIDList = new ArrayList<>();

    public SaleMainPage() {
        initPage();
        initSaleTable();
        initButtons();
    }

    private void initPage() {
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout(0, 0));
    }

    private void initSaleTable() {
        saleTable = new JTable(getSaleTableModel());
        saleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(saleTable);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private TableModel getSaleTableModel() {
        Object[][] data = { };
        String[] columnNames = { "Customer Name", "Date", "Total Price" };
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return tableModel;
    }

    private void initButtons() {
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new GridLayout(0, 3, 0, 0));
        buttonHolder.add(createButtonRemove());
        buttonHolder.add(createButtonView());
        buttonHolder.add(createButtonAdd());

        add(buttonHolder, BorderLayout.SOUTH);
    }

    private JButton createButtonRemove() {
        JButton buttonRemoveSale = new JButton("Delete Sale");
        buttonRemoveSale.addActionListener(e -> {
            if (saleTable.getSelectedRowCount() > 0) {
                int rowIndex = saleTable.getSelectedRow();
                removeRow(rowIndex);
                refreshTable();
            }
        });
        return buttonRemoveSale;
    }

    private JButton createButtonView() {
        JButton buttonViewSale = new JButton("View Sale");
        buttonViewSale.addActionListener(e -> {
            if (saleTable.getSelectedRowCount() > 0) {
                int rowIndex = saleTable.getSelectedRow();

                SaleViewPage viewPage = new SaleViewPage();
                viewPage.setSale(saleController.getSale(saleIDList.get(rowIndex)));
                showPopup(viewPage);
                saleController.fillSaleDetails(saleIDList.get(rowIndex));
            }
        });
        return buttonViewSale;
    }

    private JButton createButtonAdd() {
        JButton buttonAddSale = new JButton("Add Sale");
        buttonAddSale.addActionListener(e -> {
            showPopup(pageNewSale);
            saleController.populateWithProductNames();
        });
        return buttonAddSale;
    }

    public void emptySales() {
        DefaultTableModel tableModel = (DefaultTableModel) saleTable.getModel();
        tableModel.setRowCount(0);
        saleIDList = new ArrayList<>();
    }

    public void removeRow(int rowIndex) {
        int saleID = saleIDList.get(rowIndex);
        DefaultTableModel tableModel = (DefaultTableModel) saleTable.getModel();

        tableModel.removeRow(rowIndex);
        saleIDList.remove(rowIndex);
        saleController.removeSale(saleID);
    }

    public void addSales(Set<Sale> saleSet) {
        saleSet.forEach(this::addSale);
    }

    public void addSale(Sale sale) {
        DefaultTableModel tableModel = (DefaultTableModel) saleTable.getModel();
        Object[] saleRow = new Object[3];
        saleRow[0] = sale.getCustomerName();
        saleRow[1] = sale.getDate();
        saleRow[2] = 0.0; // TODO: find way to calculate total amount

        tableModel.addRow(saleRow);
        saleIDList.add(sale.getSaleId());
    }

    public void refreshTable() {
        emptySales();
        saleController.populateWithSales();
        saleTable.repaint();
    }

}
