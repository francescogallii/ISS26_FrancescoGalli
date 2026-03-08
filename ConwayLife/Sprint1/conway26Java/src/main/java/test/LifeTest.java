package main.java.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.conway.domain.Life;
import main.java.conway.domain.LifeInterface;
 
/*
 * TEST PLAN
 */
public class LifeTest {
private LifeInterface lifeModel;

	@Before
	public void setup() {
		System.out.println("GridTest | setup");	
		lifeModel = new Life(5, 5);	
	}
	
	@After
	public void down() {
		System.out.println("GridTest | down");
	}
	
	@Test
	public void testSetCellAlive() {
		lifeModel.setCell(1, 1, true);
		assertTrue(lifeModel.isAlive(1, 1));
	}
	
	@Test
	public void testSetCellDead() {
		lifeModel.setCell(2, 2, true); 
		lifeModel.setCell(2, 2, false);
		assertFalse(lifeModel.isAlive(2, 2));
	}
	
	@Test
	public void testReset() {
		lifeModel.setCell(3, 3, true);
		lifeModel.resetGrids();
		assertFalse(lifeModel.isAlive(3, 3));
	}

}
