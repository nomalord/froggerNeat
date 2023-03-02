//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package genetic_algorithm.neat.genome;

import genetic_algorithm.neat.visual.Panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GenomeFrame extends JFrame {
    private Panel panel;
    private Genome genome;

    public GenomeFrame(Genome genome) {
        this();
        this.setGenome(genome);
        this.repaint();
    }

    public void setGenome(Genome genome) {
        this.panel.setGenome(genome);
        this.genome = genome;
    }

    public GenomeFrame() throws HeadlessException {
        this.setDefaultCloseOperation(3);
        this.setTitle("NEAT");
        this.setMinimumSize(new Dimension(1000, 700));
        this.setPreferredSize(new Dimension(1000, 700));
        this.setLayout(new BorderLayout());
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();

        try {
            UIManager.setLookAndFeel(looks[3].getClassName());
        } catch (ClassNotFoundException var10) {
            var10.printStackTrace();
        } catch (InstantiationException var11) {
            var11.printStackTrace();
        } catch (IllegalAccessException var12) {
            var12.printStackTrace();
        } catch (UnsupportedLookAndFeelException var13) {
            var13.printStackTrace();
        }

        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(1000, 100));
        menu.setLayout(new GridLayout(1, 6));
        JButton buttonB = new JButton("random weight");
        buttonB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenomeFrame.this.genome.mutate_weight_random();
                GenomeFrame.this.repaint();
            }
        });
        menu.add(buttonB);
        JButton buttonZ = new JButton("weight shift");
        buttonZ.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenomeFrame.this.genome.mutate_weight_shift();
                GenomeFrame.this.repaint();
            }
        });
        menu.add(buttonZ);
        JButton buttonC = new JButton("Link mutate");
        buttonC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenomeFrame.this.genome.mutate_link();
                GenomeFrame.this.repaint();
            }
        });
        menu.add(buttonC);
        JButton buttonD = new JButton("Node mutate");
        buttonD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenomeFrame.this.genome.mutate_node();
                GenomeFrame.this.repaint();
            }
        });
        menu.add(buttonD);
        JButton buttonE = new JButton("on/off");
        buttonE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenomeFrame.this.genome.mutate_link_toggle();
                GenomeFrame.this.repaint();
            }
        });
        menu.add(buttonE);
        JButton buttonF = new JButton("Mutate");
        buttonF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenomeFrame.this.genome.mutate();
                GenomeFrame.this.repaint();
            }
        });
        menu.add(buttonF);
        JButton buttonG = new JButton("Calculate");
        buttonG.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.err.println("Not yet implemented");
            }
        });
        menu.add(buttonG);
        this.add(menu, "North");
        this.panel = new Panel();
        this.add(this.panel, "Center");
        this.setVisible(true);
    }
}
