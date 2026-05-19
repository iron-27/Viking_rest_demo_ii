package ru.mephi.vikingboard.desktop;

import org.springframework.stereotype.Component;
import ru.mephi.vikingboard.model.Viking;

import javax.swing.SwingUtilities;

@Component
public class DesktopSync {

    private VikingBoardFrame frame;

    public void attach(VikingBoardFrame frame) {
        this.frame = frame;
    }

    public void upsert(Viking viking) {
        VikingBoardFrame current = frame;
        if (current == null || viking == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> current.showViking(viking));
    }

    public void remove(int id) {
        VikingBoardFrame current = frame;
        if (current == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> current.hideViking(id));
    }

    public void refresh() {
        VikingBoardFrame current = frame;
        if (current == null) {
            return;
        }
        SwingUtilities.invokeLater(current::reloadTable);
    }
}
