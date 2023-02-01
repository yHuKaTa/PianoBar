package frames;

import database.Help;

import javax.swing.*;

public class BarFrame extends JFrame {
    public Help help = new Help();
    public final BarRouter router;

    public BarFrame(){
        super("Пиян-оО Бар");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
        router = new BarRouter(this);
        router.showLoginPanel();
    }
}
