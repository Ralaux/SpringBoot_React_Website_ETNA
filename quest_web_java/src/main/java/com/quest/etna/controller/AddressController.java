//package com.quest.etna.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.List;
//import java.util.Optional;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.quest.etna.model.Address;
//import com.quest.etna.model.User;
//import com.quest.etna.model.User.UserRole;
//import com.quest.etna.repositories.AddressRepository;
//import com.quest.etna.repositories.UserRepository;
//
//
//
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//public class AddressController {
//	@Autowired
//	private AddressRepository addressRepository;
//	
//	@Autowired
//	private UserRepository userRepository;
//	
//	@PostMapping(value = "/address", produces = "application/json")
//	public ResponseEntity<Object> createAddress(Authentication authentication, @Valid @RequestBody Address addressJson) {
//		
//		try {
//			String username = authentication.getName();
//	        Optional<User> user = userRepository.findByUsername(username);
//	        addressJson.setUser(user.get());
//			return new ResponseEntity<>(addressRepository.save(addressJson), HttpStatus.CREATED);
//		}
//		catch (Exception e) {
//			Map<String, Object> error = new HashMap<>();
//	        error.put("error", e.getCause().getCause().getMessage());
//	        error.put("status", HttpStatus.BAD_REQUEST.value());
//	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//		}
//	}
//	
//	@PutMapping(value = "/address/{addressID}", produces = "application/json")
//	public ResponseEntity<Object> updateAddress(Authentication authentication, @RequestBody Map<String, String> addressJson, @PathVariable("addressID") Integer addressID) throws Exception {
//		
//		try {
//			String username = authentication.getName();
//	        Optional<User> user = userRepository.findByUsername(username);
//			Optional<Address> addressToUpdate = addressRepository.findById(addressID);
//			if (addressToUpdate.isEmpty()) {
//				Map<String, Object> error = new HashMap<>();
//				error.put("error", "This address does not exist");
//		        error.put("status", HttpStatus.NOT_FOUND.value());
//				return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//			}
//			if (user.get().getRole().equals(UserRole.ROLE_USER.toString()) && user.get().getId() != addressToUpdate.get().getUser().getId() ) {
//				Map<String, Object> error = new HashMap<>();
//				error.put("error", "This is not your address and you are not admin: REJECTED PEASANT");
//		        error.put("status", HttpStatus.FORBIDDEN.value());
//		        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
//	        }
//
//			for (Map.Entry<String, String> entry : addressJson.entrySet()) {
//		        switch(entry.getKey()){
//		        
//			        case "street": 
//			        	addressToUpdate.get().setStreet(entry.getValue());
//			            break;
//			    
//			        case "city":
//			        	addressToUpdate.get().setCity(entry.getValue());
//			            break;
//			    
//			        case "country":
//			        	addressToUpdate.get().setCountry(entry.getValue());
//			            break;
//			            
//			        case "postalCode":
//			        	addressToUpdate.get().setPostalCode(entry.getValue());
//			            break;
//			            
//			        default:
//			            break;
//			    }
//		    }
//			addressToUpdate.get().setStreet(addressJson.get("street"));
//			addressToUpdate.get().toString();
//			return new ResponseEntity<>(addressRepository.save(addressToUpdate.get()), HttpStatus.OK);
//		}
//		catch (Exception e) {
//			Map<String, Object> error = new HashMap<>();
//	        error.put("error", e.getCause().getCause().getMessage());
//	        error.put("status", HttpStatus.BAD_REQUEST.value());
//	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//		}
//		
//	}
//	
//	@DeleteMapping(value = "/address/{addressID}", produces = "application/json")
//	public ResponseEntity<Object> deleteAddress(final HttpServletResponse response, Authentication authentication, @PathVariable("addressID") Integer addressID) {		
//		response.setContentType("application/json");
//		try {
//			Optional<Address> addressToDelete = addressRepository.findById(addressID);
//			if (addressToDelete.isEmpty()) {
//				Map<String, Object> retVal = new HashMap<>();
//				retVal.put("success", false);
//				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
//			}
//			
//			String username = authentication.getName();
//	        Optional<User> user = userRepository.findByUsername(username);
//			if (user.get().getRole().equals(UserRole.ROLE_USER.toString()) && user.get().getId() != addressToDelete.get().getUser().getId() ) {
//				Map<String, Object> error = new HashMap<>();
//				error.put("success", false);
//		        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
//	        }
//			
//			addressRepository.deleteById(addressID);
//			Map<String, Object> retVal = new HashMap<>();
//			retVal.put("success", true);
//			return new ResponseEntity<>(retVal, HttpStatus.OK);
//		}
//		catch (Exception e) {
//			Map<String, Object> error = new HashMap<>();
//	        error.put("success", false);
//	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//		}
//		
//	}
//	
//	@GetMapping(value = "/address", produces = "application/json")
//	public ResponseEntity<Object> getAddress(Authentication authentication) {		
//		try {
//			String username = authentication.getName();
//	        Optional<User> user = userRepository.findByUsername(username);
//	        if (user.get().getRole().equals(UserRole.ROLE_USER.toString())) {
//		        return new ResponseEntity<>(addressRepository.findAllMine(user.get().getId()), HttpStatus.OK);
//	        }
//	        return new ResponseEntity<>(addressRepository.findAll(), HttpStatus.OK);
//		}
//		catch (Exception e) {
//			Map<String, Object> error = new HashMap<>();
//	        error.put("error", e.getCause().getCause().getMessage());
//	        error.put("status", HttpStatus.BAD_REQUEST.value());
//	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//		}
//	}
//	
//	@GetMapping(value = "/address/{addressID}", produces = "application/json")
//	public ResponseEntity<Object> getOneAddress(Authentication authentication, @PathVariable("addressID") Integer addressID) {		
//		try {
//			String username = authentication.getName();
//	        Optional<User> user = userRepository.findByUsername(username);
//	        Optional<Address> addressToGet = addressRepository.findById(addressID);
//	        if (addressToGet.isEmpty()) {
//				Map<String, Object> retVal = new HashMap<>();
//				retVal.put("error", "Nothing to see here");
//				return new ResponseEntity<>(retVal, HttpStatus.NOT_FOUND);
//			}
//	        if (user.get().getRole().equals(UserRole.ROLE_USER.toString()) && user.get().getId() != addressToGet.get().getUser().getId()) {
//	        	Map<String, Object> error = new HashMap<>();
//				error.put("error", "Only a true king could look into this");
//		        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
//	        }
//	        return new ResponseEntity<>(addressToGet, HttpStatus.OK);
//		}
//		catch (Exception e) {
//			Map<String, Object> error = new HashMap<>();
//	        error.put("error", e.getCause().getCause().getMessage());
//	        error.put("status", HttpStatus.BAD_REQUEST.value());
//	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//		}
//	}
//}
