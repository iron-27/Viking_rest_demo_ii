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

    private final VikingService rosterService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final JLabel status = new JLabel("Ready");

    public VikingDesktopFrame(VikingService rosterService) {
        this.rosterService = rosterService;

        setTitle("Viking Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1040, 460));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel("Viking Board");
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
        createRandomButton.addActionListener(event -> addRandomViking());

        JButton reloadButton = new JButton("Reload table");
        reloadButton.addActionListener(event -> reloadTable());

        actions.add(createRandomButton);
        actions.add(reloadButton);
        actions.add(status);
        add(actions, BorderLayout.EAST);

        reloadTable();
    }

    private void addRandomViking() {
        Viking created = rosterService.createRandom();
        showViking(created);
        status.setText("Added id " + created.id());
    }

    public void showViking(Viking viking) {
        tableModel.put(viking);
        status.setText("Shown id " + viking.id());
    }

    public void hideViking(int id) {
        tableModel.remove(id);
        status.setText("Removed id " + id);
    }

    public void reloadTable() {
        tableModel.setRows(rosterService.loadAll());
        status.setText("Rows: " + tableModel.getRowCount());
    }
}
