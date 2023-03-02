package genetic_algorithm.neat;

import genetic_algorithm.neat.neat.Client;

import javax.swing.JFrame;

public class main extends JFrame{
	private Display game;
	private Client client;
	
	public static void main(String[] args) {		
		JFrame frame= new JFrame("Pepe the Frogger");
		Display game= new Display();	
		frame.add(game);
		frame.setSize(600,480);
      		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public main(Display game, Client client) {
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
			while(game.isGameOver() == false){
				game.move(client,false);
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.print("\rscore: " + game.getScore());

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
