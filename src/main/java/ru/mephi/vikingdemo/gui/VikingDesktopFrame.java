package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final JLabel status = new JLabel("Ready");

    public VikingDesktopFrame(VikingService vikingService) {
        this.vikingService = vikingService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1040, 460));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel("Viking Demo");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 14, 4, 14));
        add(title, BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(0, 1, 8, 8));
        actions.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 12));

        JButton createRandomButton = new JButton("Create random viking");
        createRandomButton.addActionListener(event -> onCreateViking());

        JButton reloadButton = new JButton("Reload table");
        reloadButton.addActionListener(event -> onReloadTable());

        actions.add(createRandomButton);
        actions.add(reloadButton);
        actions.add(status);
        add(actions, BorderLayout.EAST);

        onReloadTable();
    }

    private void onCreateViking() {
        Viking created = vikingService.createRandomViking();
        addNewViking(created);
        status.setText("Added id " + created.id());
    }

    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
        status.setText("Shown id " + viking.id());
    }

    public void removeViking(int id) {
        tableModel.remove(id);
        status.setText("Removed id " + id);
    }

    public void onReloadTable() {
        tableModel.setRows(vikingService.findAll());
        status.setText("Rows: " + tableModel.getRowCount());
    }
}