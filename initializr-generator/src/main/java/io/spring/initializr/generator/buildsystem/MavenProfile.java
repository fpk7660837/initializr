package io.spring.initializr.generator.buildsystem;

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

    private DependencyContainer dependencies;

    public MavenProfile(String id, boolean activation, BuildItemResolver itemResolver) {
        this.id = id;
        this.activation = new Activation(activation);
        this.dependencies =  new DependencyContainer(itemResolver::resolveDependency);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void properties(String key, String value) {
        this.properties.put(key, value);
    }

    public void addDependency(Dependency dependency) {
        if (null == dependency) {
            return;
        }
        this.dependencies.add(dependency.getId(), dependency);
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


        public String getValue() {
            return activeByDefault ? "true" : "false";
        }

    }

}
