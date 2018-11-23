package com.github.eks5115.docker.depend.spring.boot.autoconfigure;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * @author eks5115
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(DockerDependProperties.class)
@ConditionalOnProperty(name = "eks5115.docker.depend.enable", havingValue = "true")
public class DockerDependAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(DockerDependAutoConfiguration.class);

    private ConfigurableEnvironment env;

    @Autowired(required = false)
    private DockerDependProperties dockerDependProperties;

    public DockerDependAutoConfiguration(ConfigurableEnvironment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() throws InterruptedException {

        if (dockerDependProperties==null
                || !dockerDependProperties.isEnable()
                || dockerDependProperties.getServices()==null) {
            return;
        }

        List<DockerDependProperties.Service> services = dockerDependProperties.getServices();
        boolean stubborn = dockerDependProperties.isStubborn();
        int delayTimeAfterSuccess = dockerDependProperties.getDelayTimeAfterSuccess();
        int retry = dockerDependProperties.getRetry();
        int interval = dockerDependProperties.getInterval();

        HttpClient client = HttpClients.createDefault();
        for (DockerDependProperties.Service service : services) {
            String host = service.getHost();
            int port = service.getPort();
            String healthUrl = service.getHealthUrl();

            int tmpRetry = 0;
            while (true) {
                if (tmpRetry >= retry && !stubborn) {
                    break;
                }

                try {
                    HttpResponse httpResponse = client.execute(new HttpGet(new URI("http://" + host + ":" + port + healthUrl)));
                    int statusCode = httpResponse.getStatusLine().getStatusCode();

                    if (statusCode != HttpStatus.SC_OK) {
                        continue;
                    }
                    logger.info("Connect to {}:{} success", host, port);
                    break;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    logger.warn("Connect to {}:{} fail", host, port);

                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    tmpRetry++;
                }
            }
        }

        Thread.sleep(delayTimeAfterSuccess);
    }
}
