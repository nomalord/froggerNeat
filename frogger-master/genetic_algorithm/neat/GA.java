package genetic_algorithm.neat;

import genetic_algorithm.neat.genome.GenomeFrame;
import genetic_algorithm.neat.neat.Client;
import genetic_algorithm.neat.neat.Neat;

public class GA {

    public static void rateClient(Display game, Client client){
        game.reset();

        int iteration = 0;
        while(iteration < 20 && game.isGameOver() == false){
            iteration ++;
            game.move(client,false);
            }
        client.setScore(game.getNeatScore());
        game.setDeaths(0);
        game.setScore(0);
    }

    public static void evolve(Display game, Neat neat){
        for(int i = 0; i < neat.getMax_clients(); i++){
            rateClient(game, neat.getClient(i));
        }
        neat.evolve();
    }

    public static void main(String[] args) {

        Display game = new Display();
        game.SetNeat(true);

        Neat neat = new Neat(4,3,1000);
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


        for(int i = 0; i < 1000; i++){
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
        game = new Display();
        game.SetNeat(true);
        new Frame(game, neat.getBestClient());

    }

}
