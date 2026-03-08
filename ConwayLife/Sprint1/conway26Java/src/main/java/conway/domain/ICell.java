package main.java.conway.domain;

// La cella è dotata di uno stato (viva o morta) e può essere impostata con opportuni accessori
public interface ICell {
	void setStatus(boolean v); //primitiva
	boolean isAlive(); //primitiva
	void switchCellState(); //composta
}
