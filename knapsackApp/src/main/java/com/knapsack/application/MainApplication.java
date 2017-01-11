package com.knapsack.application;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.knapsack.serviceImpl.KnapsackServiceImpl;

/**
 * To BootStrap the Application.
 * 
 * @author Satish
 *
 */

// Here If More Classes and packages will involve then we have to add
// @ComponetScan with package
@ComponentScan("com.knapsack.*")
@SpringBootApplication
public class MainApplication {
	private static Logger logger = Logger.getLogger(MainApplication.class);

	final static String inpuntFileName = "inputData.txt";

	// Reading Through properties file
	@Value("${item.file}")

	private static String dataFile;

	public static void main(String[] args) {
		logger.info("main starting.");
		ApplicationContext context = SpringApplication.run(MainApplication.class);
		KnapsackServiceImpl service = context.getBean(KnapsackServiceImpl.class);

		// Checking that file is providing from command-line or not
		// if yes then give priority geven file
		if (args != null && args.length >= 1) {
			service.findMaximumSatisfaction(args[0]);
		} else {// If not given take default file
			service.findMaximumSatisfaction(inpuntFileName);
		}

		logger.info("main finished.");
	}
}
