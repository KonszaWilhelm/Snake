package com.wilhelm.konsza;

import javax.swing.*;

public class GameFrame extends JFrame {


    public GameFrame() {

        this.add(new GamePanel());
        this.setTitle("Snake \uD83D\uDC0D");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //if we add components to the JFrame the pack function will take the frame and fit it around the components
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
