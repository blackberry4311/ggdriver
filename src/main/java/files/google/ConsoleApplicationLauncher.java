package files.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import files.google.rest.RestConfiguration;
import files.google.services.ServiceConfiguration;

@SpringBootApplication(scanBasePackageClasses = { RestConfiguration.class, ServiceConfiguration.class })
public class ConsoleApplicationLauncher {
	private static final Logger logger = LoggerFactory.getLogger(ConsoleApplicationLauncher.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ConsoleApplicationLauncher.class, args);
		logger.info("Press Ctrl+C to exit ...");
	}

}