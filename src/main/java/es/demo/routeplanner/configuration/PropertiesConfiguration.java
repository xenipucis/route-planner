package es.demo.routeplanner.configuration;

import es.demo.routeplanner.logic.ErrorManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PropertiesConfiguration {

    private static final String ERROR_FILE_NAME = "errorMessages.yaml";

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Qualifier(ErrorManager.QUALIFIER_NAME)
    public YamlPropertiesFactoryBean errorPropertiesFromYamlFile() {
        final YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(ERROR_FILE_NAME));
        return yaml;
    }
}

