package com.taquin;


import java.util.List;

import static com.taquin.Main.*;

public class Agent extends Thread{
    private Position positionActuelle;
    private final Position positionCible;
    private Position positionPrecedente;
    private final String symbol;
    private final String link;
    public static Long tempsDepart = System.currentTimeMillis();
    private static boolean isPrinted = false;
    public static int nbAgents = 5;
    public static Agent[] ag = new Agent[nbAgents];

    // Agents disponible à placer sur le plateau
    public static Agent[] agents = {
            //new Agent(new Position(1,1), new Position(0,0), "0 ", "src/com/company/data/0.png"),
            new Agent(new Position(0,3), new Position(0,1), "1 ", "src/com/taquin/data/1.png"),
            new Agent(new Position(3,3), new Position(0,2), "2 ","src/com/taquin/data/2.png"),
            new Agent(new Position(0,2), new Position(0,4), "4 ","src/com/taquin/data/4.png"),
            new Agent(new Position(4,1), new Position(1,0), "5 ","src/com/taquin/data/5.png"),
            new Agent(new Position(2,2), new Position(1,1), "6 ","src/com/taquin/data/6.png"),
            new Agent(new Position(1,2), new Position(1,3), "8 ","src/com/taquin/data/8.png"),
            new Agent(new Position(2,0), new Position(2,1), "11","src/com/taquin/data/11.png"),
            new Agent(new Position(3,0), new Position(2,4), "14","src/com/taquin/data/14.png"),
            new Agent(new Position(1,3), new Position(3,1), "16","src/com/taquin/data/16.png"),
            new Agent(new Position(4,4), new Position(3,3), "18","src/com/taquin/data/18.png"),
            new Agent(new Position(2,4), new Position(4,0), "20","src/com/taquin/data/20.png"),
            new Agent(new Position(3,4), new Position(4,3), "23","src/com/taquin/data/23.png"),
            new Agent(new Position(3,2), new Position(5,1), "24","src/com/taquin/data/24.png"),
    };

    // Constructeur d'agent
    public Agent(Position positionInitiale, Position positionCible, String sym, String link){
        this.positionActuelle = positionInitiale;
        this.positionCible = positionCible;
        this.positionPrecedente = positionInitiale;
        this.symbol = sym;
        this.link = link;
    }

    public Position getPosActuelle() {
        return positionActuelle;
    }

    public Position getPosCible() {
        return positionCible;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getLink() {
        return link;
    }

    // Mise à jour de la position des agents
    public void setPosition(Position position) throws InterruptedException {

        synchronized (tableau) {
            if (positionPrecedente.equals(position)) {
                position = tableau.getcaseLibre(this);
                if(getPriority()<10){
                    Thread.currentThread().setPriority(getPriority()+1);
                }
            }
            if (tableau.agentALaPosition(position) != null) {
                Message msg = new Message(this, tableau.agentALaPosition(position), position);
                messages.get(msg.getDestinataire()).add(msg);
            }
            else {
                tableau.deplacerAgent(this, position);
                positionPrecedente = positionActuelle;
                positionActuelle = position;

            }
        }
    }

    // Position finale verifié ou non
    public boolean atteintDestination(){
        return getPosActuelle().equals(getPosCible());
    }

    // Lancer le jeu avec l'algorithme A*
    public void run() {

        while(tableau.pasFini()) {

            Message message;
            List<Message> listMessages = messages.get(this);
            if(listMessages.isEmpty()){
                message = null;
            }else{
                message = listMessages.remove(0);
            }

            if(message != null
                    && !message.getTransmetteur().getPosCible().equals(message.getTransmetteur().getPosActuelle())
                    && getPosActuelle().equals(message.getPositionALiberer())) {

                if ( tableau.getcaseLibre(this) != getPosActuelle()) {
                    try {
                        setPosition( tableau.getcaseLibre(this));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Agent voisin = tableau.agentVoisin(this);
                    Message msg = new Message(this, voisin, voisin.getPosActuelle());
                    messages.get(msg.getDestinataire()).add(msg);
                }
            } else if(!atteintDestination()) {
                if(getPosActuelle().getX() > getPosCible().getX()) {
                    try {
                        setPosition(new Position(getPosActuelle().getX() - 1, getPosActuelle().getY()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(getPosActuelle().getX() < getPosCible().getX()) {
                    try {
                        setPosition(new Position(getPosActuelle().getX() + 1, getPosActuelle().getY()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(getPosActuelle().getY() > getPosCible().getY()) {
                    try {
                        setPosition(new Position(getPosActuelle().getX(), getPosActuelle().getY() - 1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(getPosActuelle().getY() < getPosCible().getY()) {
                    try {
                        setPosition(new Position(getPosActuelle().getX(), getPosActuelle().getY() + 1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            if(!isPrinted){
                isPrinted = true;
                System.out.print("Résolution: "+(System.currentTimeMillis() - tempsDepart)/1000 + " secondes \n");
                System.out.print("Le nombre de déplacement est de : " + Tableau.deplacement + "\n");
                sleep(5000);
                System.exit(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


