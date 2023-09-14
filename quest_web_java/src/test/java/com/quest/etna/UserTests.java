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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.quest.etna.model.User;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.config.JwtTokenUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@Sql(statements = "DELETE FROM user WHERE username like \"TestUser\" OR username = \"TestUser2\" OR username = \"TestUser3\"")
public class UserTests {

	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	JwtTokenUtil tokenUtil;
	
	@Autowired
	 private AuthenticationManager authManager;
	
	/**
     * Tests /register, /authenticate, /me
     */	
    @Test
    @Order(1)
    public void testAuthenticate() throws Exception {
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.post("/register")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content("{\"username\":\"TestUser\",\"password\":\"TestUser\"}"))
    	.andDo(print())
    	.andExpect(status().isCreated());
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.post("/register")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content("{\"username\":\"TestUser\",\"password\":\"TestUser\"}"))
    	.andDo(print())
    	.andExpect(status().isConflict());
    	
    	 MvcResult result = this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.post("/authenticate")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content("{\"username\":\"TestUser\",\"password\":\"TestUser\"}"))
    	.andDo(print())
    	.andExpect(status().isOk())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.token").exists()) // Necessary dot before token: WHY?
    	.andReturn(); 
    	
    	String response = result.getResponse().getContentAsString(); 
    	String[] parts = response.split("\"");
    	String token = "Bearer " + parts[9];
    	
    	 
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/me")
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", token)
    			.content("{\"username\":\"TestUser\",\"password\":\"TestUser\"}"))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
    
    
    /**
     * tests get user, update user, delete user
     */
    @Test
    @Order(2)
    public void testUser() throws Exception {
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/user"))
    	.andDo(print())
    	.andExpect(status().isUnauthorized());
    	
    	
    	User user = new User();
    	user.setUsername("TestUser");
    	user.setPassword("TestUser");
    	userRepo.save(user);
    	JwtUserDetails userDet = new JwtUserDetails(user);
    	String token = tokenUtil.generateToken(userDet);
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/user").header("Authorization", "Bearer " + token))
    	.andDo(print())
    	.andExpect(status().isOk());
    	
    	User user2 = new User();
    	user2.setUsername("TestUser2");
    	user2.setPassword("TestUser2");
    	userRepo.save(user2);
    	UsernamePasswordAuthenticationToken token2 = new UsernamePasswordAuthenticationToken("TestUser2", "TestUser2");
			
		Authentication authResult = authManager.authenticate(token2);
			
		SecurityContextHolder.getContext().setAuthentication(authResult);
		String jwt = tokenUtil.generateToken((org.springframework.security.core.userdetails.UserDetails) authResult.getPrincipal());
			
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/user/" + user2.getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + jwt)
    			.content("{\"role\":\"ROLE_ADMIN\"}"))
    	.andDo(print())
    	.andExpect(status().isForbidden());
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/user/" + user2.getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + jwt)
    			.content("{\"password\":\"TestUser3\"}"))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("TestUser2"))
    	.andExpect(status().isOk());
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.delete("/user/" + user2.getId()).header("Authorization", "Bearer " + token))
    	.andDo(print())
    	.andExpect(status().isForbidden());
    	
    	user.setRole(User.UserRole.ROLE_ADMIN.toString());
    	userRepo.save(user);
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.delete("/user/" + user2.getId()).header("Authorization", "Bearer " + token))
    	.andDo(print())
    	.andExpect(status().isOk());
    }
    
    /*
    @Test
    @Order(3)
    public void testAddress() throws Exception {
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/address"))
    	.andDo(print())
    	.andExpect(status().isUnauthorized());
    	
    	
    	User user = new User();
    	user.setUsername("TestUser");
    	user.setPassword("TestUser");
    	userRepo.save(user);
    	JwtUserDetails userDet = new JwtUserDetails(user);
    	String token = tokenUtil.generateToken(userDet);
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/address").header("Authorization", "Bearer " + token))
    	.andDo(print())
    	.andExpect(status().isOk());
    	
    	MvcResult result = this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.post("/address")
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"city\":\"Toulouse\","
    					+ "\"country\":\"France\","
    					+ "\"postalCode\":\"31000\","
    					+ "\"street\":\"Parga\"}"))
    	.andDo(print())
    	.andExpect(status().isCreated())
    	.andReturn(); 
    	
    	String response = result.getResponse().getContentAsString(); 
    	String[] parts = response.split("\"");
    	String addressID = parts[2].substring(1, parts[2].length()-1);;
    	
    	
    	User user2 = new User();
    	user2.setUsername("TestUser2");
    	user2.setPassword("TestUser2");
    	userRepo.save(user2);
    	JwtUserDetails userDet2 = new JwtUserDetails(user2);
    	String token2 = tokenUtil.generateToken(userDet2);
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.delete("/address/"+addressID)
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token2))
    	.andDo(print())
    	.andExpect(status().isForbidden()); 
    	
    	user2.setRole(User.UserRole.ROLE_ADMIN.toString());
    	userRepo.save(user2);
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.delete("/address/"+addressID)
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token2))
    	.andDo(print())
    	.andExpect(status().isOk()); 
    }*/
    
    
}