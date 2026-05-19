package ru.mephi.vikingdemo.controller;

import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;

import javax.swing.SwingUtilities;

@Component
public class VikingListener {

    private VikingDesktopFrame frame;

    public void attach(VikingDesktopFrame frame) {
        this.frame = frame;
    }

    public void upsert(Viking viking) {
        VikingDesktopFrame current = frame;
        if (current == null || viking == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> current.showViking(viking));
    }

    public void remove(int id) {
        VikingDesktopFrame current = frame;
        if (current == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> current.hideViking(id));
    }

    public void refresh() {
        VikingDesktopFrame current = frame;
        if (current == null) {
            return;
        }
        SwingUtilities.invokeLater(current::reloadTable);
    }
}
