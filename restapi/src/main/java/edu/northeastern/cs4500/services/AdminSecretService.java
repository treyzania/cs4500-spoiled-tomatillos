package edu.northeastern.cs4500.services;

import java.io.File;
import java.io.FileWriter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import edu.northeastern.cs4500.util.Tokens;

@Service
public class AdminSecretService implements InitializingBean {

	private static final String SECRET_PATH = "/mnt/rtpersist/adminsecret.txt";
	
	private String superuserSecret = Tokens.createNewToken();
	
	public String getSuperuserSecret() {
		return this.superuserSecret;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		File sf = new File(SECRET_PATH);
		try (FileWriter fw = new FileWriter(sf)) {
			fw.write(this.getSuperuserSecret());
			fw.close();
		}
		
	}
	
}
