package com.backend.business;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.backend.dto.ChefeSubordinadoDTO;
import com.backend.entity.ColaboradorEntity;
import com.backend.repositories.ColaboradorRepository;

@Service
public class ColaboradorBusiness {

	@Autowired
	ColaboradorRepository colaboradorRepository;
	
	@GetMapping
	public ColaboradorEntity findById(Integer id) {
		return colaboradorRepository.findById(id).get();
	}
	@GetMapping
	public List<ColaboradorEntity> findAll() {
		return colaboradorRepository.findAll();
	}
	public ColaboradorEntity save(ColaboradorEntity colaboradorEntity) throws NoSuchAlgorithmException {
		
		colaboradorEntity.setScore(calcularSenha(colaboradorEntity.getSenha()));
		colaboradorEntity.setSenha(criptografaSenha(colaboradorEntity.getSenha()));
		
		return colaboradorRepository.save(colaboradorEntity);
		
	}
	
	 
	public int calcularSenha(String password){
	        
	        //total score of password
	        int iPasswordScore = 0;
	        
	        if( password.length() < 8 )
	            return 0;
	        else if( password.length() >= 10 )
	            iPasswordScore += 2;
	        else 
	            iPasswordScore += 1;
	        
	        //if it contains one digit, add 2 to total score
	        if( password.matches("(?=.*[0-9]).*") )
	            iPasswordScore += 2;
	        
	        //if it contains one lower case letter, add 2 to total score
	        if( password.matches("(?=.*[a-z]).*") )
	            iPasswordScore += 2;
	        
	        //if it contains one upper case letter, add 2 to total score
	        if( password.matches("(?=.*[A-Z]).*") )
	            iPasswordScore += 2;    
	        
	        //if it contains one special character, add 2 to total score
	        if( password.matches("(?=.*[~!@#$%^&*()_-]).*") )
	            iPasswordScore += 2;
	        	
	        return iPasswordScore;
	        
	   	}
	public String criptografaSenha(String password) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
	    byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
	
	    StringBuilder hexString = new StringBuilder();
	    for (byte b : encodedHash) {
	        String hex = Integer.toHexString(0xff & b);
	        if (hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	
	    return hexString.toString();
	}
	public ColaboradorEntity associaSubordinado(ChefeSubordinadoDTO chefe) {
		ColaboradorEntity chef = colaboradorRepository.findById(chefe.getIdChefe()).get();
		ColaboradorEntity sub = colaboradorRepository.findById(chefe.getIdSubordinado()).get();
		sub.setId_chefe(chef);
		colaboradorRepository.save(sub);
		return sub;
	}
		
	
	
	



}
