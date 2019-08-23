package com.github.dmogn.idealo;

import com.github.dmogn.idealo.model.Direction;
import com.github.dmogn.idealo.model.Field;
import com.github.dmogn.idealo.model.Position;
import java.util.Optional;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Robot (state & operations). 
 * 
 * Singleton.
 */
@Data
@Component
@Scope(value = "singleton")
public class Robot {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private Field field;
    
    private Direction direction;
    
    private Optional<Position> position = Optional.empty();
    
    public Robot() {
        clear();
    }
    
    public synchronized void reset() {
        clear();
    }
    
    private final void clear() {
        direction = Direction.SOUTH;
        position = Optional.empty();
    }
    
    public boolean isInitialized() {
        return position.isPresent();
    }
    
    public synchronized boolean place(Position position, Direction direction) {
        logger.trace("place robot", position, direction);
        
        // check for position in field
        if (!field.isInside(position)) {
            logger.warn("The robot placing out of field, ignoring", position, direction);
            return false;
        }
        
        this.position = Optional.of(position);
        this.direction = direction;
        
        return true;
    }
    
    public synchronized void left() {
        logger.trace("turn robot left");
        
        if (!isInitialized()) {
            logger.warn("Left turn ignored. The robot isn't initialized.");
            return;// ignore
        }
        
        if (Direction.NORTH.equals(direction))
            direction = Direction.WEST;
        else if (Direction.WEST.equals(direction))
            direction = Direction.SOUTH;
        else if (Direction.SOUTH.equals(direction))
            direction = Direction.EAST;
        else if (Direction.EAST.equals(direction))
            direction = Direction.NORTH;
    }
    
    public synchronized void right() {  
        logger.trace("turn robot right");  
        
        if (!isInitialized()) {
            logger.warn("Right turn ignored. The robot isn't initialized.");
            return;// ignore
        }
        
        if (Direction.NORTH.equals(direction))
            direction = Direction.EAST;
        else if (Direction.EAST.equals(direction))
            direction = Direction.SOUTH;
        else if (Direction.SOUTH.equals(direction))
            direction = Direction.WEST;
        else if (Direction.WEST.equals(direction))
            direction = Direction.NORTH;
    }
    
    /**
     * Move robot on 1 step in predefined direction.
     * 
     * @return robot stare. false if unitialized or out of range.
     */
    public synchronized boolean move() {
        logger.trace("move robot 1 step"); 
        
        if (!isInitialized()) {
            return false;// ignore
        }
        
        Position newPosition;
        
        if (Direction.NORTH.equals(direction))
            newPosition = new Position(position.get().getX(), position.get().getY()+1);
        else if (Direction.EAST.equals(direction))
            newPosition = new Position(position.get().getX()+1, position.get().getY());
        else if (Direction.SOUTH.equals(direction))
            newPosition = new Position(position.get().getX(), position.get().getY()-1);
        else if (Direction.WEST.equals(direction))
            newPosition = new Position(position.get().getX()-1, position.get().getY());
        else
            throw new IllegalStateException("undefined direction");
            
        if (field.isInside(newPosition)) {
            this.position = Optional.of(newPosition);
            return true;
        } else {
            logger.warn("out of field robot moving, ignoring");
            return false;
        }
    }
    
    public synchronized String report() {        
        if (!isInitialized()) {    
            // ignore
            logger.warn("Report ignored. The robot isn't initialized.");
            return "";
        }
        
        return String.format("%d,%d,%s", position.get().getX(), position.get().getY(), direction.toString());
    }
}
