package com.jumbo.cucumber;

import com.jumbo.WebservicesApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = WebservicesApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {
}
