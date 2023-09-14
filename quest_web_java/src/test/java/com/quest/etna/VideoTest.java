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
public class VideoTest {

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
    
	@Test
    @Order(1)
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
