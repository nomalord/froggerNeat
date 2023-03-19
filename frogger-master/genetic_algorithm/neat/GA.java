package genetic_algorithm.neat;

import genetic_algorithm.neat.genome.GenomeFrame;
import genetic_algorithm.neat.neat.Client;
import genetic_algorithm.neat.neat.Neat;

import java.io.*;

public class GA {

    public static void rateClient(Display game, Client client) throws IOException, ClassNotFoundException {
        game.reset();

        int iteration = 0;
        int iterationYDidntChange = 0;
        int iterationXDidntChange = 0;
        while(iteration < 5000 && game.isGameOver() == false && iterationYDidntChange < 20 && iterationXDidntChange < 20) {

            iteration++;
            game.move(client, false);

            if (game.getCurrentY() == game.getPreviousY()) {
                iterationYDidntChange++;
            } else
                iterationYDidntChange = 0;

            if (game.getCurrentX() == game.getPreviousX()) {
                iterationXDidntChange++;
            } else
                iterationXDidntChange = 0;
        }
        if(iterationYDidntChange == 0 && iterationXDidntChange == 0) {
            client.setScore(0);
            game.setDeaths(0);
            game.setScore(0);
            return;
        }
        int timeBasedReward = game.getTimeBasedReward(iteration);
        int score = 50 + game.getScore() * 100 - game.GetDeaths() * 100
                + game.getCheckpointReward() + timeBasedReward + game.getDistanceScore();
        client.setScore(score);
        game.setDeaths(0);
        game.setScore(0);
    }

    public static void evolve(Display game, Neat neat) throws IOException, ClassNotFoundException {
        for(int i = 0; i < neat.getMax_clients(); i++){
            rateClient(game, neat.getClient(i));
        }
        neat.evolve();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Display game = new Display();
        game.SetNeat(true);

        Neat neat = new Neat(30,3,1000);
        neat.setCP(3);

        neat.setPROBABILITY_MUTATE_WEIGHT_RANDOM(0.01);
        neat.setPROBABILITY_MUTATE_WEIGHT_SHIFT(0.01);
        neat.setPROBABILITY_MUTATE_LINK(0.3);
        neat.setPROBABILITY_MUTATE_NODE(0.1);
        neat.setSURVIVORS(0.3);

        for(int i = 0; i < neat.getMax_clients(); i++){
//            new GenomeFrame(neat.getBestClient().getGenome());

            rateClient(game, neat.getClient(i));
//            new Frame(game, neat.getClient(i));
        }


        for(int i = 0; i < 100; i++){
            System.out.println("#################### " + i + " ######################");
            evolve(game, neat);
            neat.printSpecies();
            System.out.println(neat.getBestClient().getScore());
            neat.printScoreInformation();
        }

        for(int i = 0; i < neat.getMax_clients(); i++){
            rateClient(game, neat.getClient(i));
        }


        new GenomeFrame(neat.getBestClient().getGenome());
        File f = new File("C:\\Users\\nkous\\Desktop\\Neat 2023\\frogger-simple\\client.txt");
        if(!f.exists() && f.isDirectory()) {
            serializeDataOut(neat.getBestClient());//save the best client
        }
        game = new Display();
        game.SetNeat(true);
        new Frame(game, neat.getBestClient());

    }


    public static void serializeDataOut(Client client) throws IOException {
        String fileName= "client.txt";
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(client);
        oos.close();
    }

    public static Client serializeDataIn() throws IOException, ClassNotFoundException {
        String fileName= "client.txt";
        FileInputStream fin = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fin);
        Client client= (Client) ois.readObject();
        ois.close();
        return client;
    }
}
