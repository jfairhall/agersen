
package com.jf.agersenstest;

import java.io.IOException;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;



@RestController
@RequestMapping("/agersenstest")
public class AnimalsController {

	private static Logger logger = LogManager.getLogger(AnimalsController.class);

	private static final String FIREBASE_ENDPOINT = "https://agersens-test.firebaseio.com/animals.json";
	private static final String LOGFILE = "./animals.log";
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	{	
		// configure RestTemplate to allow PATCH requests to HTTPS
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60 * 1000); // 60 seconds
        requestFactory.setReadTimeout(60 * 1000);    // 60 seconds
        
        restTemplate.setRequestFactory(requestFactory);
        
        // configure logger to output to specific file
        // *NOTE*: This is here for convenience. Normally this would be in a log4j.properties file
        // Using log4j and not log4j 2.0 because 2.0 doesn't easily allow this in code.
        
		try {  
	        FileAppender appender = new FileAppender(new PatternLayout("%d{yyyy-MM-dd}-%t-%x-%-5p-%-10c:%m%n"), LOGFILE);
	        logger.addAppender(appender);
	        logger.setLevel(Level.INFO);
	        
		} catch (IOException e) {
			// shouldn't happen
			e.printStackTrace();
		}    

	}
	

	@PostMapping("/animals")
	public ResponseEntity<String> animals(@RequestBody Map<String, Animal> animals) {

		logger.info("REST animals method called");
		
		try {

			logger.info("validating request");

			// validate request
			
			 // could also check for empty list? would indicate either very bad data or empty input. Unknown if empty input is illegal.
            for (Map.Entry<String, Animal> entry : animals.entrySet())
			{
				entry.getValue().validate(entry.getKey());
			}
			
			// PATCH to firebase db
                                 
			logger.info("patching to firebase db");
            
            String result = restTemplate.patchForObject(FIREBASE_ENDPOINT, animals, String.class);
            
			logger.info("patched to firebase db");
			logger.debug("firebase returned : " + result);
			logger.info("REST animals method returning OK");
			
			return new ResponseEntity<>("", HttpStatus.OK);

		} catch (ValidationException ve) 
		{
			logger.error("validation failed", ve);
			return new ResponseEntity<>(ve.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (RestClientException rce)
		{
			logger.error("patching to firebase failed", rce);
			return new ResponseEntity<>("failed to forward data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
