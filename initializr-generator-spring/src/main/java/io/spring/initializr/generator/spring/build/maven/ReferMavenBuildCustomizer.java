package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.buildsystem.MavenRepository;
import io.spring.initializr.generator.buildsystem.MavenRepositoryContainer;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.module.Module;
import io.spring.initializr.generator.project.module.ModuleTopology;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import java.util.List;

/**
 * Date : 2019-02-15
 * Author : pengkai.fu
 */
public class ReferMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

    private BuildItemResolver buildItemResolver;

    private ModuleTopology moduleTopology;

    public ReferMavenBuildCustomizer(BuildItemResolver buildItemResolver, ModuleTopology moduleTopology) {
        this.buildItemResolver = buildItemResolver;
        this.moduleTopology = moduleTopology;
    }

    @Override
    public void customize(MavenBuild build) {
        List<String> allModuleNames = moduleTopology.getAllReferModuleNames();

        for (String moduleName : allModuleNames) {
            addReferMavenBuild(build, moduleTopology.getModule(moduleName));
        }
    }

    private void addReferMavenBuild(MavenBuild build, Module module) {
        MavenBuild childBuild = build.childBuild(buildItemResolver);
        childBuild.setArtifact(module.getName());
        childBuild.setGroup(build.getGroup());
        childBuild.setPackaging(module.getPackaging());
        childBuild.setSuffix(module.getSuffix());
        childBuild.setVersion(build.getVersion());

        addRepositories(childBuild);
    }


    private void addRepositories(MavenBuild build) {
        MavenRepositoryContainer repositories = build.repositories();
        repositories.add(new MavenRepository("nexus-releases", "Nexus Release Repository", "http://nexus.dmall.com:8081/nexus/content/repositories/releases/"));
        repositories.add(new MavenRepository("nexus-snapshots", "Nexus Snapshot Repository", "http://nexus.dmall.com:8081/nexus/content/repositories/snapshots/", true));
    }


}
