package com.taquin;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.MIN_PRIORITY;
import static java.lang.Thread.sleep;

public class Tableau {
    private final int TAILLE = 6;
    private final String[][] tableau;
    private final List<Agent> agents;
    public static int deplacement = 0;

    public int getTAILLE() {
        return TAILLE;
    }

    public Tableau(Agent[] a){
        tableau = new String[TAILLE][TAILLE];
        agents = new ArrayList<>();
        for(int i=0; i<TAILLE; i++){
            for(int j=0; j<TAILLE; j++){
                tableau[i][j] = "_ ";
            }
        }
        for (Agent agent : a) {
            agents.add(agent);
            tableau[agent.getPosActuelle().getX()][agent.getPosActuelle().getY()] = agent.getSymbol();
        }
    }

    // Verification de la présence d'un agent sur la position p
    public Agent agentALaPosition(Position p){
        for(Agent a:agents){
            if(a.getPosActuelle().equals(p)){
                return a;
            }
        }
        return null;
    }

    // Verification si oui ou non il y a un agent voisin
    public Agent agentVoisin(Agent a){
        Agent voisin = null;
        if((a.getPosActuelle().getX()<TAILLE-1)){
            voisin = agentALaPosition(new Position(a.getPosActuelle().getX()+1,a.getPosActuelle().getY()));
        }
        if((a.getPosActuelle().getX()>0)){
            voisin = agentALaPosition(new Position(a.getPosActuelle().getX()-1,a.getPosActuelle().getY()));
        }
        if((a.getPosActuelle().getY()>0)){
            voisin = agentALaPosition(new Position(a.getPosActuelle().getX(),a.getPosActuelle().getY()-1));
        }
        if((a.getPosActuelle().getY()<TAILLE-1)){
            voisin = agentALaPosition(new Position(a.getPosActuelle().getX(),a.getPosActuelle().getY()+1));
        }
        return voisin;
    }

    // Verfification si oui ou non l'agent est à la position finale
    public boolean pasFini(){
        for(Agent a : agents){
            if(!(a.getPosActuelle().equals(a.getPosCible()))){
                return true;
            }
        }
        return false;
    }

    // Résolution du tableau
    public void resoudre(){
        for(Agent a:agents){
            a.start();
            if(a.getPosActuelle() == a.getPosCible()){
                a.setPriority(MIN_PRIORITY);
            }
        }
    }

    // Deplacement d'un agent
    public void deplacerAgent(Agent a, Position p) throws InterruptedException {
        Main.b[a.getPosActuelle().getX() * TAILLE + a.getPosActuelle().getY()].setIcon(new ImageIcon("src/com/company/data/blank.png"));
        Main.b[a.getPosActuelle().getX() * TAILLE + a.getPosActuelle().getY()].setFocusable(false);
        Main.b[p.getX() * TAILLE + p.getY()].setIcon(new ImageIcon(a.getLink()));
        Main.b[p.getX() * TAILLE + p.getY()].setFocusable(false);
        tableau[p.getX()][p.getY()] = a.getSymbol();
        tableau[a.getPosActuelle().getX()][a.getPosActuelle().getY()] = "_ ";
        sleep(100);
        Main.window.setVisible(true);
        deplacement++;
    }

    // Verification case libre
    public Position getcaseLibre(Agent a){

        Position position = a.getPosActuelle();

        if((position.getX()+1 <TAILLE) && (tableau[position.getX() + 1][position.getY()].equals("_ "))) {
            position = new Position(position.getX() + 1, position.getY());
        }else if((position.getX()-1 >0) && (tableau[position.getX() - 1][position.getY()].equals("_ "))){
            position = new Position(position.getX()-1,position.getY());
        }else if((position.getY()+1 <TAILLE) && (tableau[position.getX()][position.getY() + 1].equals("_ "))){
            position = new Position(position.getX(),position.getY()+1);
        }else if((position.getY()-1 > 0) && (tableau[position.getX()][position.getY() - 1].equals("_ "))){
            position = new Position(position.getX(),position.getY()-1);
        }
        return position;
    }
}
