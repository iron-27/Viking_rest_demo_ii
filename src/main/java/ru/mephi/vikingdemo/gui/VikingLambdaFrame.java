package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.List;
import java.util.stream.Collectors;

public class VikingLambdaFrame extends JFrame {

    private final JLabel titleLabel = new JLabel(" ");
    private final JTextArea textArea = new JTextArea();

    public VikingLambdaFrame() {
        setTitle("Lambda — результаты");
        setSize(620, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 12, 4, 12));
        add(titleLabel, BorderLayout.NORTH);

        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        textArea.setMargin(new Insets(8, 10, 8, 10));
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Закрыть");
        closeBtn.addActionListener(e -> dispose());
        JPanel bottom = new JPanel();
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    public void showViking(Viking viking) {
        if (viking == null) {
            titleLabel.setText("Викинг не найден");
            textArea.setText("Нет викингов, удовлетворяющих условию.");
            return;
        }
        titleLabel.setText("Случайный викинг ростом > 180 см");
        textArea.setText(formatViking(viking));
    }

    public void showVikings(List<Viking> vikings) {
        if (vikings == null || vikings.isEmpty()) {
            titleLabel.setText("Список пуст");
            textArea.setText("Нет викингов, удовлетворяющих условию.");
            return;
        }
        titleLabel.setText("Найдено викингов: " + vikings.size());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vikings.size(); i++) {
            if (i > 0) {
                sb.append("\n").append("─".repeat(52)).append("\n");
            }
            sb.append(formatViking(vikings.get(i)));
        }
        textArea.setText(sb.toString());
        textArea.setCaretPosition(0);
    }

    public void showText(String header, String body) {
        titleLabel.setText(header);
        textArea.setText(body);
        textArea.setCaretPosition(0);
    }

    private String formatViking(Viking v) {
        String equipment = (v.equipment() == null || v.equipment().isEmpty())
                ? "—"
                : v.equipment().stream()
                .map(e -> e.name() + " [" + e.quality() + "]")
                .collect(Collectors.joining(", "));
        return String.format(
                "ID:         %d%n" +
                        "Имя:        %s%n" +
                        "Возраст:    %d лет%n" +
                        "Рост:       %d см%n" +
                        "Волосы:     %s%n" +
                        "Борода:     %s%n" +
                        "Снаряжение: %s",
                v.id(), v.name(), v.age(), v.heightCm(),
                v.hairColor(), v.beardStyle(), equipment
        );
    }
}
