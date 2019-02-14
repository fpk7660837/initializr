package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildWriter;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.spring.build.BuildWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

/**
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class MultiModuleMavenBuildProjectContributor implements BuildWriter, ProjectContributor {


    private final MavenBuild build;

    private final IndentingWriterFactory indentingWriterFactory;

    private final MavenBuildWriter buildWriter;


    public MultiModuleMavenBuildProjectContributor(MavenBuild build, IndentingWriterFactory indentingWriterFactory) {
        this.build = build;
        this.indentingWriterFactory = indentingWriterFactory;
        this.buildWriter = new MavenBuildWriter();
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {

    }

    @Override
    public void writeBuild(Writer out) throws IOException {

    }
}
