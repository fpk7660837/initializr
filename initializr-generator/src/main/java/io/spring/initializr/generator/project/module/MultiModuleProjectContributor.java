package io.spring.initializr.generator.project.module;

import io.spring.initializr.generator.project.contributor.ProjectContributor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * generate multi module directory
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class MultiModuleProjectContributor implements ProjectContributor {


    private ModuleTopology moduleTopology;


    public MultiModuleProjectContributor(ModuleTopology moduleTopology) {
        this.moduleTopology = moduleTopology;
    }

    // should run first
    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        generateMultiModules(projectRoot);
    }

    private void generateMultiModules(Path projectRoot) throws IOException {
        for (String modulePath : moduleTopology.getAllChildModuleNames()) {
            Path module = projectRoot.resolve(modulePath);
            Files.createDirectories(module);
        }

        for (String referModulePath : moduleTopology.getAllReferModuleNames()) {
            Path module = projectRoot.resolve(referModulePath);
            Files.createDirectories(module);
        }
    }
}
