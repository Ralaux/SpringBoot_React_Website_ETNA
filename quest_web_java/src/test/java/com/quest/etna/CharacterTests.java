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
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.repositories.CharacterRepository;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@Sql(statements = "DELETE FROM user WHERE username like \"TestUser\"")
public class CharacterTests {

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
     * Tests /register, /authenticate, /me
     */	
    @Test
    @Order(1)
    public void testCharacter() throws Exception {
    	User user = new User();
    	user.setUsername("TestUser");
    	user.setPassword("TestUser");
    	userRepo.save(user);
    	JwtUserDetails userDet = new JwtUserDetails(user);
    	String token = tokenUtil.generateToken(userDet);
    	
    	
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/character/" + characRepo.findByName("Toon Link").get().getId()))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Toon Link"))
    	.andExpect(status().isOk());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.get("/character"))
    	.andDo(print())
    	.andExpect(status().isOk());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/character/" + characRepo.findByName("Toon Link").get().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"speed\":1.42}"))
    	.andDo(print())
    	.andExpect(status().isForbidden());
        
        user.setRole("ROLE_ADMIN");
    	userRepo.save(user);
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/character/" + characRepo.findByName("Toon Link").get().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"speed\":1.42}"))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.speed").value(1.42))
    	.andExpect(status().isOk());
    }
    
    @Test
    @Order(2)
    public void testAttacks() throws Exception {
    	User user = new User();
    	user.setUsername("TestUser");
    	user.setPassword("TestUser");
    	userRepo.save(user);
    	JwtUserDetails userDet = new JwtUserDetails(user);
    	String token = tokenUtil.generateToken(userDet);
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/downTilt/" + characRepo.findByName("Toon Link").get().getDown_tilt().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"endlag\":5}"))
    	.andDo(print())
    	.andExpect(status().isForbidden());
        
        this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/upTilt/" + characRepo.findByName("Toon Link").get().getUp_tilt().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"damage\":8}"))
    	.andDo(print())
    	.andExpect(status().isForbidden());
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/forwardTilt/" + characRepo.findByName("Toon Link").get().getForward_tilt().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"frame\":9}"))
    	.andDo(print())
    	.andExpect(status().isForbidden());
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/dashAttack/" + characRepo.findByName("Toon Link").get().getDash_attack().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"name\":\"strike\"}"))
    	.andDo(print())
    	.andExpect(status().isForbidden());
        
        user.setRole("ROLE_ADMIN");
    	userRepo.save(user);
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/downTilt/" + characRepo.findByName("Toon Link").get().getDown_tilt().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"endlag\":5}"))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.endlag").value(5))
    	.andExpect(status().isOk());
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/upTilt/" + characRepo.findByName("Toon Link").get().getUp_tilt().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"damage\":8}"))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.damage").value(8))
    	.andExpect(status().isOk());
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/forwardTilt/" + characRepo.findByName("Toon Link").get().getForward_tilt().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"frame\":9}"))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.frame").value(9))
    	.andExpect(status().isOk());
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/dashAttack/" + characRepo.findByName("Toon Link").get().getDash_attack().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"name\":\"strike\"}"))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("strike"))
    	.andExpect(status().isOk());
    }
    
    public void testVideo() throws Exception {
    	User user = new User();
    	user.setUsername("TestUser");
    	user.setPassword("TestUser");
    	userRepo.save(user);
    	JwtUserDetails userDet = new JwtUserDetails(user);
    	String token = tokenUtil.generateToken(userDet);
    	
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/video/" + characRepo.findByName("Toon Link").get().getVideo().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"url\":\"https://www.youtube.com/watch?v=EVrMpZmfRyw\"}"))
    	.andDo(print())
    	.andExpect(status().isForbidden());
    	
    	user.setRole("ROLE_ADMIN");
    	userRepo.save(user);
    	
    	this.mockMvc
    	.perform(
    			MockMvcRequestBuilders.put("/video/" + characRepo.findByName("Toon Link").get().getVideo().getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.header("Authorization", "Bearer " + token)
    			.content("{\"url\":\"https://www.youtube.com/watch?v=EVrMpZmfRyw\"}"))
    	.andDo(print())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.url").value("https://www.youtube.com/watch?v=EVrMpZmfRyw"))
    	.andExpect(status().isOk());
    }
}
