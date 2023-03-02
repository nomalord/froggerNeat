package genetic_algorithm.neat;

import genetic_algorithm.neat.neat.Client;

import javax.swing.*;

public class Frame extends JFrame{

    Client client;
    public Frame(Display game, Client client) {
        JFrame frame= new JFrame("Pepe the Frogger");
        this.client = client;

        new Thread(() -> {
            game.reset();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < 20; i++){
                System.out.println("");
            }
            while(game.getNeatScore() < 100){
                game.move(client,false, true);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\rscore: " + game.getNeatScore());

                repaint();
            }
        }).start();
        frame.add(game);
        frame.setSize(600,480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
