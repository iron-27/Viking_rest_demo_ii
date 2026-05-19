package ru.mephi.vikingboard.gui;

import ru.mephi.vikingboard.model.EquipmentItem;
import ru.mephi.vikingboard.model.Viking;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VikingTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {
            "Id", "Name", "Age", "Height (cm)", "Hair color", "Beard style", "Equipment"
    };

    private final List<Viking> rows = new ArrayList<>();

    public void put(Viking viking) {
        int rowIndex = findRow(viking.id());
        if (rowIndex >= 0) {
            rows.set(rowIndex, viking);
            fireTableRowsUpdated(rowIndex, rowIndex);
            return;
        }

        int insertedRow = rows.size();
        rows.add(viking);
        fireTableRowsInserted(insertedRow, insertedRow);
    }

    public void remove(int id) {
        int rowIndex = findRow(id);
        if (rowIndex < 0) {
            return;
        }
        rows.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void setRows(List<Viking> vikings) {
        rows.clear();
        rows.addAll(vikings);
        fireTableDataChanged();
    }

    private int findRow(Integer id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < rows.size(); i++) {
            if (id.equals(rows.get(i).id())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Viking viking = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> viking.id();
            case 1 -> viking.name();
            case 2 -> viking.age();
            case 3 -> viking.heightCm();
            case 4 -> viking.hairColor();
            case 5 -> viking.beardStyle();
            case 6 -> formatEquipment(viking.equipment());
            default -> "";
        };
    }

    private String formatEquipment(List<EquipmentItem> equipment) {
        if (equipment == null || equipment.isEmpty()) {
            return "";
        }
        return equipment.stream()
                .map(item -> item.name() + " (" + item.quality() + ")")
                .collect(Collectors.joining("; "));
    }
}
