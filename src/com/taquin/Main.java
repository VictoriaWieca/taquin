package com.taquin;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

import static java.lang.Thread.sleep;

public class Main {
    public static Tableau tableau;
    public static HashMap<Agent, List<Message>> messages;
    public static ImageIcon icon;
    public static JFrame window = new JFrame("Taquin");
    public static JButton[] b;


    public static void main(String[] args) throws InterruptedException {
        
        Scanner scanInput = new Scanner(System.in);
        System.out.println("Nombre d'agents : ");
        Agent.nbAgents = scanInput.nextInt();
        Agent.ag = new Agent[Agent.nbAgents];
        if (Agent.nbAgents >= 0) System.arraycopy(Agent.agents, 0, Agent.ag, 0, Agent.nbAgents);

        tableau = new Tableau(Agent.ag);

        window.setSize(500,500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new GridLayout(tableau.getTAILLE(),tableau.getTAILLE()));
        b = new JButton[tableau.getTAILLE()*tableau.getTAILLE()];

        for (int i = 0; i < tableau.getTAILLE()*tableau.getTAILLE(); i++) {
            b[i] = new JButton();
            window.add(b[i]);
        }

        messages = new HashMap<>();
        System.out.println("Taquin " + tableau.getTAILLE() + "*" + tableau.getTAILLE() + "\n");
        for(int i = 0; i < Agent.nbAgents; i++) {
            messages.put(Agent.agents[i], new ArrayList<>());
        }
        afficherGUIInitiale();
        sleep(1000);
        tableau.resoudre();

    }

    // Affichage du tableau de départ
    public static void afficherGUIInitiale(){

        for (int i = 0; i < tableau.getTAILLE()*tableau.getTAILLE(); i++) {
            b[i].setIcon( new ImageIcon("src/com/taquin/data/blank.png"));
            for (int j =0; j< Agent.nbAgents; j++ ) {
                if (i == Agent.agents[j].getPosActuelle().getX() * tableau.getTAILLE() + Agent.agents[j].getPosActuelle().getY()) {
                    if (b[i] != null) {
                        icon = new ImageIcon(Agent.agents[j].getLink()); //imports the image
                        b[i].setIcon(icon);
                        b[i].setFocusable(false);
                    }
                }
            }
        }
        window.setVisible(true);
        System.out.print(System.currentTimeMillis() + " ");

    }

}
