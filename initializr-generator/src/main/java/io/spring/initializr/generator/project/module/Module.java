package io.spring.initializr.generator.project.module;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class Module {

    private String name;

    private List<Module> childModules;

    private List<Module> referModules;


    public Module(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Module> getChildModules() {
        return childModules;
    }


    public List<Module> getChildModulesRecursive() {
        if (CollectionUtils.isEmpty(this.childModules)) {
            return new ArrayList<>();
        }

        Set<Module> dependencies = new HashSet<>();
        for (Module childModule : childModules) {
            dependencies.add(childModule);
            List<Module> childModules = childModule.getChildModulesRecursive();

            if (CollectionUtils.isEmpty(childModules)) {
                continue;
            }
            dependencies.addAll(childModules);
        }

        return new ArrayList<>(dependencies);
    }

    public List<Module> getAllModules() {
        List<Module> childModules = getChildModulesRecursive();
        List<Module> referModules = getReferModules();

        List<Module> allModules = new ArrayList<>();

        if (!CollectionUtils.isEmpty(childModules)) {
            allModules.addAll(childModules);
        }

        if (!CollectionUtils.isEmpty(referModules)) {
            allModules.addAll(referModules);
        }

        return allModules;
    }

    public void addChildModule(Module child) {
        if (null == childModules) {
            childModules = new ArrayList<>();
        }
        childModules.add(child);
    }

    public void addReferModule(Module module) {
        if (null == referModules) {
            referModules = new ArrayList<>();
        }
        referModules.add(module);
    }

    public List<Module> getReferModules() {
        return referModules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module that = (Module) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
