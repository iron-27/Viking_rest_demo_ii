package ru.mephi.vikingdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mephi.vikingdemo.controller.VikingListener;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.SwingUtilities;

@SpringBootApplication
public class VikingDemoApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(VikingDemoApplication.class);
        application.setHeadless(false);

        ConfigurableApplicationContext context = application.run(args);
        VikingService service = context.getBean(VikingService.class);
        VikingListener vikingListener = context.getBean(VikingListener.class);

        SwingUtilities.invokeLater(() -> {
            VikingDesktopFrame frame = new VikingDesktopFrame(service);
            vikingListener.attach(frame);
            frame.setVisible(true);
        });
    }
}
