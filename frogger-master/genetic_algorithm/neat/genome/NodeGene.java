package genetic_algorithm.neat.genome;

import java.io.Serializable;

public class NodeGene extends Gene implements Serializable {


    private double x,y;

    public NodeGene(int innovation_number) {
        super(innovation_number);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean equals(Object o){
        if(!(o instanceof NodeGene)) return false;
        return innovation_number == ((NodeGene) o).getInnovation_number();
    }

    @Override
    public String toString() {
        return "NodeGene{" +
                "innovation_number=" + innovation_number +
                '}';
    }

    public int hashCode(){
        return innovation_number;
    }
}
