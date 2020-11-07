package com.ur.seminar;

import com.ur.seminar.app.property.FileStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;


/**
 * The API Expansion was created by
 * @author Daniel
 * @see com.ur.seminar.app.controller.FileController
 * @see FileStorageProperties
 */

@EnableConfigurationProperties({
        FileStorageProperties.class
})
@SpringBootApplication
public class Main  extends SpringBootServletInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Autowired
    private Environment env;
/**
 * Main method of the program
 * @param args normal part of the main method
 * */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("{}", env.getProperty("JAVA_HOME"));
        logger.info("{}", env.getProperty("app.name"));
    }
}
