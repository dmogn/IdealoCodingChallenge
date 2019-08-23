package com.github.dmogn.idealo;

import com.github.dmogn.idealo.model.Command;
import com.github.dmogn.idealo.model.Direction;
import com.github.dmogn.idealo.model.Position;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Robot controller REST Endpoint.
 */
@Api("Robot API")
@RestController
@RequestMapping("/api/v1/robot")
public class RobotREST {
    
    private static final String COMMAND_PATTERN = "\\w+";
    
    private static final String DELIMITER_PATTERN = "\\W+";
    
    protected static final String ROBOT_MISSING_RESPONSE = "ROBOT MISSING";
            
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    Robot robot;
    
    @ApiOperation(value = "Single robot command")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully"),
        @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping(value = "/command", produces = "text/plain")
    public ResponseEntity<String> command(
            @ApiParam(required = true) 
            @RequestBody String request) {
        try {
            return ResponseEntity.ok(
                executeCommand(request)
                        .orElse("")
            );
        } catch (IllegalArgumentException|IllegalStateException e) {
            logger.error("command processingg exception", e);
            return ResponseEntity.badRequest().body("Bad format");
        }
    }
    
    @ApiOperation(value = "Execute batch scrypt")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully"),
        @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping(value = "/batchScrypt", produces = "text/plain")
    public ResponseEntity<String> batchScrypt(
            @ApiParam(required = true) 
            @RequestBody String request) {        
        
        final Scanner s = new Scanner(request);        
        final StringBuilder result = new StringBuilder();
        
        try {
            while (s.hasNextLine()) {
                final Optional<String> commandResult = executeCommand(s.nextLine());
                if (commandResult.isPresent())
                    result.append(commandResult.get()).append("\n");
            }
        } catch (IllegalArgumentException|IllegalStateException e) {
            logger.error("batchScrypt processingg exception", e);
            return ResponseEntity.badRequest().body("Bad format");
        }
        
        return ResponseEntity.ok(
            result.toString()
        );
    }
    
    protected Optional<String> executeCommand(String command) {
        final Scanner s = new Scanner(command.trim()).useDelimiter(DELIMITER_PATTERN);
        
        if (!s.hasNext(COMMAND_PATTERN))
            return Optional.empty(); // avoid empty string
        
        // parse command
        Command commandKey = Command.valueOf(s.next(COMMAND_PATTERN));
        
        logger.info("command: " + commandKey);
        
        // process command
        switch (commandKey) {
            case PLACE:
                final int x = Integer.valueOf( s.next() );
                final int y = Integer.valueOf( s.next() );
                final Direction direction = Direction.valueOf( s.next() );
                
                if ( !robot.place(new Position(x, y), direction) ) {
                    return Optional.of(ROBOT_MISSING_RESPONSE);
                }
                break;
            case MOVE:
                if ( !robot.move() ) {
                    return Optional.of(ROBOT_MISSING_RESPONSE);
                }
                break;
            case LEFT:
                robot.left();
                break;
            case RIGHT:
                robot.right();
                break;
            case REPORT:
                return Optional.of(robot.report());
            default:
                logger.error("Unknown command: " + command);
                throw new IllegalStateException("Unknown command");
        }
        
        return Optional.empty();
    }
}
