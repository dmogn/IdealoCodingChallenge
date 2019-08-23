package com.github.dmogn.idealo;

import com.github.dmogn.idealo.model.Direction;
import com.github.dmogn.idealo.model.Position;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(Application.class)
@SpringBootTest
public class RobotTest {
    
    @Autowired
    Robot robot;
    
    @Test
    public void testPlace() {
        robot.reset();
        
        final boolean result = robot.place(new Position(3, 3), Direction.NORTH);
        
        Assert.assertTrue(result);
        Assert.assertTrue( robot.isInitialized() );
        
        Assert.assertEquals(3, robot.getPosition().get().getX());        
        Assert.assertEquals(3, robot.getPosition().get().getY());      
        Assert.assertEquals(Direction.NORTH, robot.getDirection());
    }
    
    @Test
    public void testPlaceOutOfField() {
        robot.reset();
        
        final boolean result = robot.place(new Position(5, 5), Direction.NORTH);
        
        Assert.assertFalse(result);
        Assert.assertFalse( robot.isInitialized() );
    }
    
    @Test
    public void testMoveNORTH() {
        robot.reset();
        robot.place(new Position(3, 3), Direction.NORTH);
        
        final boolean result = robot.move();
        
        Assert.assertTrue(result);
        Assert.assertEquals(3, robot.getPosition().get().getX());        
        Assert.assertEquals(4, robot.getPosition().get().getY());
    }
    
    @Test
    public void testMoveSOUTH() {
        robot.reset();
        robot.place(new Position(3, 3), Direction.SOUTH);
        
        final boolean result = robot.move();
        
        Assert.assertTrue(result);
        Assert.assertEquals(3, robot.getPosition().get().getX());        
        Assert.assertEquals(2, robot.getPosition().get().getY());
    }
    
    @Test
    public void testMoveEAST() {
        robot.reset();
        robot.place(new Position(3, 3), Direction.EAST);
        
        final boolean result = robot.move();
        
        Assert.assertTrue(result);
        Assert.assertEquals(4, robot.getPosition().get().getX());        
        Assert.assertEquals(3, robot.getPosition().get().getY());
    }
    
    @Test
    public void testMoveWEST() {
        robot.reset();
        robot.place(new Position(3, 3), Direction.WEST);
        
        final boolean result = robot.move();
        
        Assert.assertTrue(result);
        Assert.assertEquals(2, robot.getPosition().get().getX());        
        Assert.assertEquals(3, robot.getPosition().get().getY());
    }
    
    @Test
    public void testMoveNORTHOutOfField() {
        robot.reset();
        robot.place(new Position(3, 4), Direction.NORTH);
        
        final boolean result = robot.move();
        
        Assert.assertFalse(result);
        Assert.assertEquals(3, robot.getPosition().get().getX());        
        Assert.assertEquals(4, robot.getPosition().get().getY());
    }
    
    @Test
    public void testMoveSOUTHOutOfField() {
        robot.reset();
        robot.place(new Position(3, 0), Direction.SOUTH);
        
        final boolean result = robot.move();
        
        Assert.assertFalse(result);
        Assert.assertEquals(3, robot.getPosition().get().getX());        
        Assert.assertEquals(0, robot.getPosition().get().getY());
    }
    
    @Test
    public void testMoveEASTOutOfField() {
        robot.reset();
        robot.place(new Position(4, 3), Direction.EAST);
        
        final boolean result = robot.move();
        
        Assert.assertFalse(result);
        Assert.assertEquals(4, robot.getPosition().get().getX());        
        Assert.assertEquals(3, robot.getPosition().get().getY());
    }
    
    @Test
    public void testMoveWESTOutOfField() {
        robot.reset();
        robot.place(new Position(0, 3), Direction.WEST);
        
        final boolean result = robot.move();
        
        Assert.assertFalse(result);
        Assert.assertEquals(0, robot.getPosition().get().getX());        
        Assert.assertEquals(3, robot.getPosition().get().getY());
    }
    
    
    /**
     * Do full (360 degree) turn left.
     */
    @Test
    public void testLeft() {
        robot.reset();
        robot.place(new Position(3, 3), Direction.NORTH);
        
        robot.left();
        
        Assert.assertEquals(Direction.WEST, robot.getDirection());
        
        robot.left();
        
        Assert.assertEquals(Direction.SOUTH, robot.getDirection());
        
        robot.left();
        
        Assert.assertEquals(Direction.EAST, robot.getDirection());
        
        robot.left();
        
        Assert.assertEquals(Direction.NORTH, robot.getDirection());
    }
    
    /**
     * Do full (360 degree) turn right.
     */
    @Test
    public void testRight() {
        robot.reset();
        robot.place(new Position(3, 3), Direction.NORTH);
        
        robot.right();
        
        Assert.assertEquals(Direction.EAST, robot.getDirection());
        
        robot.right();
        
        Assert.assertEquals(Direction.SOUTH, robot.getDirection());
        
        robot.right();
        
        Assert.assertEquals(Direction.WEST, robot.getDirection());
        
        robot.right();
        
        Assert.assertEquals(Direction.NORTH, robot.getDirection());
    }
    
    @Test
    public void testRepport() {
        robot.reset();
        robot.place(new Position(0, 3), Direction.WEST);
        
        final String result = robot.report();
        
        Assert.assertEquals("0,3,WEST", result); 
    }
}
