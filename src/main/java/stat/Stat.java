package stat;

/*
 ***************************
 * Copyright (c) 2015      *
 *                         *
 * This code belongs to:   *
 *                         *
 * @author Ahmet Emre Ünal *
 * S001974                 *
 *                         *
 * emre@aemreunal.com      *
 * emre.unal@ozu.edu.tr    *
 *                         *
 * aemreunal.com           *
 ***************************
 */

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import stat.graphics.ApplicationWindow;
import stat.graphics.MenuPage;

@Component
public class Stat implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("<------TEST------>");
        ApplicationWindow appWindow = new ApplicationWindow(new MenuPage());
        appWindow.setVisible(true);
    }
}
