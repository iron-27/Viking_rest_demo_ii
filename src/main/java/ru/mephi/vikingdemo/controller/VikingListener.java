package ru.mephi.vikingdemo.controller;

import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;

import javax.swing.SwingUtilities;

@Component
public class VikingListener {

    private VikingDesktopFrame gui;

    public void setGui(VikingDesktopFrame gui) {
        this.gui = gui;
    }

    public void onVikingAdded(Viking viking) {
        VikingDesktopFrame current = gui;
        if (current == null || viking == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> current.addNewViking(viking));
    }

    public void onVikingDeleted(int id) {
        VikingDesktopFrame current = gui;
        if (current == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> current.removeViking(id));
    }
}