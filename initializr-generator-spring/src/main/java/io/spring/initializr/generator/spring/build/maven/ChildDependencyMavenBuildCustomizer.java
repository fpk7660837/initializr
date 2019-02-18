package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.project.module.DefaultModuleTopology;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.MetadataBuildItemMapper;
import io.spring.initializr.metadata.Dependency;

import java.util.List;

/**
 * Date : 2019-02-18
 * Author : pengkai.fu
 */
public class ChildDependencyMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {


    static final String DEFAULT_STARTER = "root_starter";

    private final ResolvedProjectDescription projectDescription;


    public ChildDependencyMavenBuildCustomizer(ResolvedProjectDescription projectDescription) {
        this.projectDescription = projectDescription;
    }

    // extension here
    @Override
    public void customize(MavenBuild build) {
        List<MavenBuild> childBuilds = build.getChildBuilds();

        childBuilds.forEach(childBuild -> {
            String name = childBuild.getArtifact();

            // todo fix me
            if (DefaultModuleTopology.Modules.COMMON.getName().equals(name)) {
                addCommonDependency(childBuild);
            } else if (DefaultModuleTopology.Modules.SERVICE.getName().equals(name)) {
                addServiceDependency(childBuild);
            }


        });
    }


    private void addCommonDependency(MavenBuild mavenBuild) {
        DependencyContainer dependencies = mavenBuild.dependencies();

        // root starter
        io.spring.initializr.metadata.Dependency root = new Dependency();
        root.setId(DEFAULT_STARTER);
        root.asSpringBootStarter("");
        dependencies.add(DEFAULT_STARTER,
                MetadataBuildItemMapper.toDependency(root));
    }


    private void addServiceDependency(MavenBuild mavenBuild) {
        DependencyContainer dependencies = mavenBuild.dependencies();
        dependencies.add("test", "org.springframework.boot",
                "spring-boot-starter-test", DependencyScope.TEST_COMPILE);
    }

}
