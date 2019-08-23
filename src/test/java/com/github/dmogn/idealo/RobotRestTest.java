package com.github.dmogn.idealo;

import com.github.dmogn.idealo.model.Direction;
import com.github.dmogn.idealo.model.Position;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Endpoint integration tests.
 */
@SpringJUnitConfig(Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RobotRestTest {
    private static final String ROBOT_COMMAND_URI = "/api/v1/robot/command";
    private static final String ROBOT_BATCH_SCRYPT_URI = "/api/v1/robot/batchScrypt";
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    Robot robot;
    
    @Test
    public void testCommandPlace() throws Exception {
        // init
        robot.reset();
        
        // request
        final String requestBody = "PLACE 0,0,NORTH";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_COMMAND_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
    }
    
    @Test
    public void testCommandPlaceOutOfField() throws Exception {
        // init
        robot.reset();
        
        // request
        final String requestBody = "PLACE 5,5,NORTH";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_COMMAND_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        Assert.assertEquals(RobotREST.ROBOT_MISSING_RESPONSE, mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    public void testCommandMoveUnititalized() throws Exception {
        // init
        robot.reset();
        
        // request
        final String requestBody = "MOVE";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_COMMAND_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        Assert.assertEquals(RobotREST.ROBOT_MISSING_RESPONSE, mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    public void testCommandRight() throws Exception {
        // init
        robot.reset();
        robot.place(new Position(3, 3), Direction.NORTH);
        
        // request
        final String requestBody = "RIGHT";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_COMMAND_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());      
        Assert.assertEquals(Direction.EAST, robot.getDirection());
    }
    
    @Test
    public void testCommandLeft() throws Exception {
        // init
        robot.reset();
        robot.place(new Position(3, 3), Direction.NORTH);
        
        // request
        final String requestBody = "LEFT";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_COMMAND_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());      
        Assert.assertEquals(Direction.WEST, robot.getDirection());
    }
    
    @Test
    public void testCommandReport() throws Exception {
        // init
        robot.reset();
        robot.place(new Position(3, 3), Direction.NORTH);
        
        // request
        final String requestBody = "REPORT";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_COMMAND_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        Assert.assertEquals("3,3,NORTH", mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    public void testCommandBadRequest() throws Exception {
        // init
        robot.reset();
        robot.place(new Position(3, 3), Direction.NORTH);
        
        // request
        final String requestBody = "dsfgsd454325fsdf";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_COMMAND_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(400, mvcResult.getResponse().getStatus());
    }
    
    @Test
    public void testBatchScrypt() throws Exception {
        // init
        robot.reset();
        
        // request
        final String requestBody = "PLACE 1,2,EAST\n"
                + "MOVE\n"
                + "MOVE\n"
                + "LEFT\n"
                + "MOVE\n"
                + "REPORT";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_BATCH_SCRYPT_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
        Assert.assertEquals("3,3,NORTH", mvcResult.getResponse().getContentAsString().trim());
    }
    
    @Test
    public void testBatchScryptBadRequest() throws Exception {
        // init
        robot.reset();
        
        // request
        final String requestBody = "PLACE 1,2,EAST\n"
                + "ghfdghgdfh3254324523";
        
        final MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders
                        .post(ROBOT_BATCH_SCRYPT_URI)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(requestBody)
                        .accept(MediaType.TEXT_PLAIN))
                .andReturn();
        
        // validate
        Assert.assertEquals(400, mvcResult.getResponse().getStatus());
    }
}