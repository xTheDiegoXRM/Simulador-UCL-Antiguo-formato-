package models;
import java.util.Random;
import models.Clubes;

public class Partidos {
    private Clubes clubLocal;
    private Clubes clubVisita;
    private int golesLocal;
    private int golesVisita;
    private int golesIdaLocal; // Goles de ida del local
    private int golesIdaVisita; // Goles de ida del visita
    private Clubes ganador;

    public Partidos(Clubes clubLocal, Clubes clubVisita) {
        this.clubLocal = clubLocal;
        this.clubVisita = clubVisita;
        this.golesLocal = 0;
        this.golesVisita = 0;
        this.golesIdaLocal = 0;
        this.golesIdaVisita = 0;
        simularPartido();
    }

    public Clubes getClubLocal() {
        return clubLocal;
    }

    public Clubes getClubVisita() {
        return clubVisita;
    }

    public void setClubLocal(Clubes clubLocal) {
        this.clubLocal = clubLocal;
    }

    public void setClubVisita(Clubes clubVisita) {
        this.clubVisita = clubVisita;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public void setGolesVisita(int golesVisita) {
        this.golesVisita = golesVisita;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public int getGolesVisita() {
        return golesVisita;
    }

    public int getGolesIdaLocal() {
        return golesIdaLocal;
    }

    public void setGolesIdaLocal(int golesIdaLocal) {
        this.golesIdaLocal = golesIdaLocal;
    }

    public int getGolesIdaVisita() {
        return golesIdaVisita;
    }

    public void setGolesIdaVisita(int golesIdaVisita) {
        this.golesIdaVisita = golesIdaVisita;
    }

    public Clubes getGanador() {
        return ganador;
    }
    
    public void setGolesIda(int golesLocal, int golesVisita) {
        this.golesIdaLocal = golesLocal;
        this.golesIdaVisita = golesVisita;
    }

    public void setGanador(Clubes ganador) {
        this.ganador = ganador;
    }
    
    public void simularPartido() {
        Random random = new Random();
        this.golesLocal = random.nextInt(4);       
        this.golesVisita = random.nextInt(4);   
    }   
    
    public void setGoles(int golesLocal, int golesVisita) {
        this.golesLocal = golesLocal;
        this.golesLocal = golesVisita;
    }
    
    public String getResultado() {
        return this.clubLocal.getNombre() + this.golesLocal + " vs " + this.golesVisita + this.clubVisita.getNombre();
    }
    
    @Override
    public String toString() {
        return this.clubLocal.getNombre() +  " vs " + this.clubVisita.getNombre();
    }
}