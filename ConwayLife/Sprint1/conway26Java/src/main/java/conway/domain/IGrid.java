package main.java.conway.domain;

// La griglia è un'insieme di celle di dimensione decisa dall'utente.
// Possiede accessori per impostare (set) ed estrarre (get) lo stato di ogni cella.
public interface IGrid {
	  public int getRowsNum(); // primitiva
	  public int getColsNum(); // primitiva
	  public void setCellValue(int x, int y, boolean state); // composta
	  public ICell getCell(int x, int y); // primitiva
	  public boolean getCellValue(int x, int y); // composta
	  public void reset(); // composta
}
