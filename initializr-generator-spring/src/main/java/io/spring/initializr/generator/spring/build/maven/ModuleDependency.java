package io.spring.initializr.generator.spring.build.maven;


import io.spring.initializr.generator.buildsystem.Dependency;

/**
 * Date : 2019-02-20
 * Author : pengkai.fu
 */
public class ModuleDependency {

    private String module;

    private Dependency dependency;


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }
}
