package ru.mephi.vikingboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mephi.vikingboard.desktop.DesktopSync;
import ru.mephi.vikingboard.desktop.VikingBoardFrame;
import ru.mephi.vikingboard.service.VikingRosterService;

import javax.swing.SwingUtilities;

@SpringBootApplication
public class VikingBoardApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(VikingBoardApplication.class);
        application.setHeadless(false);

        ConfigurableApplicationContext context = application.run(args);
        VikingRosterService service = context.getBean(VikingRosterService.class);
        DesktopSync desktopSync = context.getBean(DesktopSync.class);

        SwingUtilities.invokeLater(() -> {
            VikingBoardFrame frame = new VikingBoardFrame(service);
            desktopSync.attach(frame);
            frame.setVisible(true);
        });
    }
}
