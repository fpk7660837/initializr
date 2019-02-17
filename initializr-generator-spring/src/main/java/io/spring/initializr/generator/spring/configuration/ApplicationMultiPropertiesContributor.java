package io.spring.initializr.generator.spring.configuration;

import io.spring.initializr.generator.project.contributor.MultipleResourcesProjectContributor;

import java.nio.file.Path;

/**
 * Date : 2019-02-15
 * Author : pengkai.fu
 */
public class ApplicationMultiPropertiesContributor extends MultipleResourcesProjectContributor {


    private String relateFilePath;

    public ApplicationMultiPropertiesContributor() {
        super("classpath:configuration");
        this.relateFilePath = "src/main/resources/";
    }


    @Override
    protected Path getFilePath(Path projectRoot, String filename) {
        String moduleName = filename.substring(0, filename.lastIndexOf("/"));
        String moduleResourceFileName = filename.substring(filename.lastIndexOf("/") + 1);
        return projectRoot.resolve(moduleName + "/" + this.relateFilePath + moduleResourceFileName);
    }
}
