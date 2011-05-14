package ch.jester.common.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.model.Player;
import ch.jester.model.Tournament;

public class DaoEqualityTest extends ActivatorProviderForTestCase {
	Player p1_unsaved = new Player();
	Player p2_unsaved = new Player();
	
	Player p1_saved = new Player();
	
	Tournament t1_unsaved = new Tournament();
	
	Tournament t1_saved = new Tournament();
	
	@Before
	public void setup(){
		p1_saved.setId(5);
		t1_saved.setId(5);
	}
	
	@Test
	public void testEqual_UnsavedDao(){
		Assert.assertEquals(true, p1_unsaved.equals(p1_unsaved));
	}
	@Test
	public void testNotEqual_UnsavedDao(){
		Assert.assertEquals(false, p2_unsaved.equals(p1_unsaved));
		Assert.assertEquals(false, p1_unsaved.equals(p2_unsaved));
		
		Assert.assertEquals(false, t1_unsaved.equals(p1_unsaved));
	
	}
	@Test
	public void testNotEqual_safedUnsavedDao(){
		Assert.assertEquals(false, t1_saved.equals(p1_unsaved));
		Assert.assertEquals(false, t1_unsaved.equals(p1_saved));
		Assert.assertEquals(false, t1_saved.equals(p1_saved));
	}
	@Test
	public void testEqual_savedDao(){
		Assert.assertEquals(true, p1_saved.equals(p1_saved));
		Assert.assertEquals(true, t1_saved.equals(t1_saved));
	}
	@Test
	public void testNotEqual_savedDao(){
		Assert.assertEquals(false, p1_saved.equals(t1_saved));
		Assert.assertEquals(false, t1_saved.equals(p1_saved));
	}
}
