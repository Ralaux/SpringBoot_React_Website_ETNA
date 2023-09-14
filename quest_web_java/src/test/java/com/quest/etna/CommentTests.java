package com.quest.etna;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.Comment;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.repositories.CharacterRepository;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.UserRepository;

import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@Sql(statements = "DELETE FROM user WHERE username like \"TestUser\" OR username = \"TestUser2\"")
@Sql(statements = "DELETE FROM comment")
public class CommentTests {

	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	CharacterRepository characRepo;
	
	@Autowired
	CommentRepository commentRepo;
	
	@Autowired
	JwtTokenUtil tokenUtil;
	
	/**
     * Tests post, put, get, delete on comments
     */	
    @Test
    @Order(1)
    public void testComments() throws Exception {
    	
    	// User that will create all
    	User user = new User();
    	user.setUsername("TestUser");
    	user.setPassword("TestUser");
    	userRepo.save(user);
    	JwtUserDetails userDet = new JwtUserDetails(user);
    	String token = tokenUtil.generateToken(userDet);
    	
    	// Other other to make sure he can't act on my comments
    	User user2 = new User();
    	user2.setUsername("TestUser2");
    	user2.setPassword("TestUser2");
    	userRepo.save(user2);
    	JwtUserDetails userDet2 = new JwtUserDetails(user2);
    	String token2 = tokenUtil.generateToken(userDet2);
    	
    	Comment newCom = new Comment();
        newCom.setBody("Comment for TL");
        newCom.setCharacter(characRepo.findById(2).get());
        newCom.setUser(user);
        commentRepo.save(newCom);
    	
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.post("/comment")
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"body\":\"Comment for new one\",\"character_id\":3}"))
    	.andDo(print())
    	.andExpect(status().isCreated());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/comment/" + newCom.getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token2)
    			.content("{\"body\":\"I AM AN EVIL COMMENT\"}"))
    	.andDo(print())
    	.andExpect(status().isForbidden());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/comment/" + newCom.getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"body\":\"I REGRET, GET THIS COMMENT INSTEAD\"}"))
    	.andDo(print())
    	.andExpect(status().isOk());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/comment/mine").header("Authorization", "Bearer " + token))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
    	.andExpect(status().isOk());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/comment/mine").header("Authorization", "Bearer " + token2))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)))
    	.andExpect(status().isOk());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/comment/2"))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
    	.andExpect(status().isOk());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.delete("/comment/"+newCom.getId()).header("Authorization", "Bearer " + token2))
    	.andDo(print())
    	.andExpect(status().isForbidden());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.delete("/comment/"+newCom.getId()).header("Authorization", "Bearer " + token))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
}
