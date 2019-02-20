package io.spring.initializr.generator.spring.build.maven;

import java.util.ArrayList;
import java.util.List;

/**
 * Date : 2019-02-20
 * Author : pengkai.fu
 */
public class ModuleDependencyContainer {


    private List<ModuleDependency> moduleDependencys;


    public List<ModuleDependency> getModuleDependencys() {
        return moduleDependencys;
    }

    public void setModuleDependencys(List<ModuleDependency> moduleDependencys) {
        this.moduleDependencys = moduleDependencys;
    }


    public void addModuleDependency(ModuleDependency moduleDependency) {
        if (null == moduleDependencys) {
            moduleDependencys = new ArrayList<>();
        }

        moduleDependencys.add(moduleDependency);
    }
}
