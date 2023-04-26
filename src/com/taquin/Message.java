package com.taquin;

public class Message {

    private final Agent transmetteur;
    private final Agent destinataire;
    private final Position positionALiberer;

    public Message(Agent a, Agent b, Position p){
        transmetteur = a;
        destinataire = b;
        positionALiberer = p;
    }

    public Agent getTransmetteur() {
        return transmetteur;
    }

    public Agent getDestinataire() {
        return destinataire;
    }

    public Position getPositionALiberer() {
        return positionALiberer;
    }
}
