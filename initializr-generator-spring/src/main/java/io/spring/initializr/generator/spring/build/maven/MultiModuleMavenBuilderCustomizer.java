package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class MultiModuleMavenBuilderCustomizer implements BuildCustomizer<MavenBuild> {

    @Override
    public void customize(MavenBuild build) {
    }


    private void makeModuleBuild(String name, MavenBuild parent) {


    }
}
