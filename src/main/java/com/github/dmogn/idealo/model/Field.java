package com.github.dmogn.idealo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Rectangular field.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "field")
public class Field {
    private int width;
    private int heigth;
    
    /** 
     * Check if the position is inside the field.
     */
    public boolean isInside(Position position) {
        if (position.getX() < 0 || position.getX() >= width)
            return false;
        
        if (position.getY() < 0 || position.getY() >= heigth)
            return false;
        
        return true;
    }
}
