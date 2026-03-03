package main.java.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.conway.domain.Cell;
import main.java.conway.domain.Life;
import main.java.conway.domain.LifeInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.conway.domain.Cell;
import main.java.conway.domain.Life;
import main.java.conway.domain.LifeInterface;

public class LifeTest {

	private LifeInterface life;
	private final int ROWS = 5;
    private final int COLS = 5;

	@Before
	public void setup() {
		System.out.println("ConwayLifeTest | setup");	
		life = Life.CreateLife(ROWS, COLS);
	
	}
	@After
	public void down() {
		System.out.println("ConwayLifeTest | down");
	}
	
	@Test
    public void testInitialStateIsDead() {
        System.out.println("LifeTest | testInitialStateIsDead");
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                assertFalse("La cella dovrebbe essere morta all'inizio", life.isAlive(r, c));
            }
        }
    }
	
}
