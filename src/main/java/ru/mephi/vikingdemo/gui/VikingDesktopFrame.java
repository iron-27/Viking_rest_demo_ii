package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;
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
import java.util.Arrays;
import java.util.stream.Collectors;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingLambdaService lambdaService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final JLabel status = new JLabel("Ready");

    public VikingDesktopFrame(
            VikingService vikingService,
            VikingLambdaService lambdaService
    ) {
        this.vikingService = vikingService;
        this.lambdaService = lambdaService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1100, 560));
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

        JPanel actions = new JPanel(new GridLayout(0, 1, 6, 6));
        actions.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 12));


        JButton createRandomButton = new JButton("Create random viking");
        createRandomButton.addActionListener(event -> onCreateViking());

        JButton reloadButton = new JButton("Reload table");
        reloadButton.addActionListener(event -> onReloadTable());


        JButton massGenButton = new JButton("⚡ Сгенерировать N викингов");
        massGenButton.addActionListener(event -> {
            String input = javax.swing.JOptionPane.showInputDialog(
                    this, "Сколько викингов сгенерировать?", "10");
            if (input == null) return;
            try {
                int count = Integer.parseInt(input.trim());
                if (count <= 0) throw new NumberFormatException();
                vikingService.createRandomVikings(count);
                onReloadTable();
                status.setText("Сгенерировано " + count + " викингов");
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this, "Введите целое положительное число.", "Ошибка ввода",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });


        JButton randomTallButton = new JButton("Случайный высокий (>180)");
        randomTallButton.addActionListener(event -> {
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showViking(lambdaService.getRandomTallViking());
            frame.setVisible(true);
        });

        JButton legendaryButton = new JButton("Легендарное снаряжение");
        legendaryButton.addActionListener(event -> {
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showVikings(lambdaService.getLegendaryVikings());
            frame.setVisible(true);
        });

        JButton redBeardButton = new JButton("Рыжие небритые (по возрасту)");
        redBeardButton.addActionListener(event -> {
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showVikings(lambdaService.getSortedRedBeardedVikings());
            frame.setVisible(true);
        });


        JButton statsButton = new JButton("Статистика выборок");
        statsButton.addActionListener(event -> showStatistics());


        JButton idMaxButton = new JButton("Макс. ID из массива int[]");
        idMaxButton.addActionListener(event -> {
            int maxId = lambdaService.findMaxId();
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showText("Максимальный ID (из int[])",
                    maxId == -1 ? "Таблица пуста." : "Максимальный ID: " + maxId);
            frame.setVisible(true);
        });

        JButton idEvenButton = new JButton("Чётные ID из массива int[]");
        idEvenButton.addActionListener(event -> {
            int[] even = lambdaService.findEvenIds();
            String body = even.length == 0
                    ? "Чётных ID нет."
                    : "Найдено: " + even.length + "\n\n"
                    + Arrays.stream(even)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(", "));
            VikingLambdaFrame frame = new VikingLambdaFrame();
            frame.showText("Чётные ID (из int[])", body);
            frame.setVisible(true);
        });

        actions.add(createRandomButton);
        actions.add(reloadButton);
        actions.add(massGenButton);
        actions.add(randomTallButton);
        actions.add(legendaryButton);
        actions.add(redBeardButton);
        actions.add(statsButton);
        actions.add(idMaxButton);
        actions.add(idEvenButton);
        actions.add(status);

        add(actions, BorderLayout.EAST);

        onReloadTable();
    }

    private void showStatistics() {
        long older40        = lambdaService.countOlderThan(40);
        long younger25      = lambdaService.countYoungerThan(25);
        long range30to50    = lambdaService.countInAgeRange(30, 50);
        long outside30to50  = lambdaService.countOutsideAgeRange(30, 50);
        long braidedBlond   = lambdaService.countByBeardAndHair(BeardStyle.BRAIDED, HairColor.Blond);
        long withAxes       = lambdaService.countWithAxes();

        String body = String.format(
                "── По возрасту ──────────────────────────%n" +
                        "  Старше 40 лет:          %4d%n" +
                        "  Моложе 25 лет:          %4d%n" +
                        "  Диапазон 30–50 лет:     %4d%n" +
                        "  Вне диапазона 30–50:    %4d%n%n" +
                        "── Борода + Волосы ──────────────────────%n" +
                        "  BRAIDED борода И Blond: %4d%n%n" +
                        "── Топоры ───────────────────────────────%n" +
                        "  С 1 или 2 топорами:     %4d",
                older40, younger25, range30to50, outside30to50,
                braidedBlond, withAxes
        );

        VikingLambdaFrame frame = new VikingLambdaFrame();
        frame.showText("Статистика выборок", body);
        frame.setVisible(true);
    }

    private void onCreateViking() {
        Viking created = vikingService.createRandom();
        addNewViking(created);
        status.setText("Added id " + created.id());
    }

    public void addNewViking(Viking viking) {
        onReloadTable();
        status.setText("Shown id " + viking.id());
    }

    public void removeViking(int id) {
        tableModel.remove(id);
        status.setText("Removed id " + id);
    }

    public void onReloadTable() {
        tableModel.setRows(vikingService.loadAll());
        status.setText("Rows: " + tableModel.getRowCount());
    }
}
