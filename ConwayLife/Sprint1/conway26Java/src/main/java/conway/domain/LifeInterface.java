package main.java.conway.domain;

// Life è un sistema che si occupa dell'evoluzione della griglia seguendo le regole del gioco.
public interface LifeInterface {
	/** Calcola l'evoluzione dello stato alla generazione successiva */
    void nextGeneration(); // primitiva

    /** Restituisce lo stato di una cella specifica */
    boolean isAlive(int row, int col); // composta

    /** Imposta lo stato di una cella */
    void setCell(int row, int col, boolean alive); // composta

    /** Restituisce il numero di righe e colonne */
//    int getRows();
//    int getCols();
    
    /** Restituisce la Cella */
    ICell getCell(int x, int y); // composta
    
    /** Restituisce la grid */
    IGrid getGrid(); // primitiva
    
    /** imposta tutte le celle della griglia allo stato morto*/
    void resetGrids(); // composta
    
    /** Restituisce una rappresentazione grafica testuale della grglia*/
    //public String gridRep( );
}
