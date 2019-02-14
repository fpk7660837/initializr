package io.spring.initializr.generator.buildsystem.maven;

import io.spring.initializr.generator.buildsystem.DependencyContainer;

import java.util.Map;
import java.util.TreeMap;

/**
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class MavenProfile {

    private String id;

    private Activation activation;

    private Map<String, String> properties = new TreeMap<>();

    private DependencyContainer dependencies = null;

    public MavenProfile(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void setDependencies(DependencyContainer dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public DependencyContainer getDependencies() {
        return dependencies;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public static class Activation {

        private boolean activeByDefault;

        public Activation(boolean activeByDefault) {
            this.activeByDefault = activeByDefault;
        }

        public boolean isActiveByDefault() {
            return activeByDefault;
        }

        public void setActiveByDefault(boolean activeByDefault) {
            this.activeByDefault = activeByDefault;
        }
    }

}
