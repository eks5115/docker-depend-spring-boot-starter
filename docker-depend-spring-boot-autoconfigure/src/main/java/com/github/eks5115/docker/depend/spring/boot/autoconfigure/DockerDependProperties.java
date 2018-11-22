package com.github.eks5115.docker.depend.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author eks5115
 */
@Component
@ConfigurationProperties(prefix = DockerDependProperties.DOCKER_DEPEND_PREFIX)
public class DockerDependProperties {

    public static final String DOCKER_DEPEND_PREFIX = "eks5115.docker.depend";

    /**
     * Services to rely on
     */
    private List<Service> services;

    /**
     * Delay time after successful connection creation. In milliseconds
     */
    private int delayTimeAfterSuccess = 5000;

    /**
     * Retry count
     */
    private int retry = 10;

    /**
     * Interval time per request. In milliseconds
     */
    private int interval = 5000;

    /**
     * Enable docker depend when it is true
     */
    private boolean enable = false;

    /**
     * When stubborn is true, it keeps trying to connect. And retry is invalid
     */
    private boolean stubborn = true;

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public int getDelayTimeAfterSuccess() {
        return delayTimeAfterSuccess;
    }

    public void setDelayTimeAfterSuccess(int delayTimeAfterSuccess) {
        this.delayTimeAfterSuccess = delayTimeAfterSuccess;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isStubborn() {
        return stubborn;
    }

    public void setStubborn(boolean stubborn) {
        this.stubborn = stubborn;
    }

    static class Service {

        private String host;

        private int port;

        /**
         * Health url
         */
        private String healthUrl = "/actuator/health";

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getHealthUrl() {
            return healthUrl;
        }

        public void setHealthUrl(String healthUrl) {
            this.healthUrl = healthUrl;
        }
    }
}
