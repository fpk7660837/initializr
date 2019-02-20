package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.BomContainer;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.module.DefaultModuleTopology;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.MetadataBuildItemMapper;
import io.spring.initializr.metadata.Dependency;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2019-02-18
 * Author : pengkai.fu
 */
public class BaseChildDependencyMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {


    static final String DEFAULT_STARTER = "root_starter";
    public static final String ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";


    // extension here
    @Override
    public void customize(MavenBuild build) {
        build.getChildBuilds().stream()
                .filter(mavenBuild -> mavenBuild.getArtifact().equals(DefaultModuleTopology.Modules.COMMON.getName()))
                .findFirst()
                .ifPresent(this::addDefaultStarterDependency);

        DependencyContainer dependencies = build.dependencies();
        List<io.spring.initializr.generator.buildsystem.Dependency> filteredDependencies = filterModuleDependency(dependencies);

        for (MavenBuild childBuild : build.getChildBuilds()) {
            for (io.spring.initializr.generator.buildsystem.Dependency dependency : filteredDependencies) {
                if (dependency.getModule().equals(childBuild.getArtifact())) {
                    childBuild.dependencies()
                            .add(dependency.getId(), dependency);
                }
            }
        }

        BomContainer boms = build.boms();
        removeSpringbootDependenciesFromRoot(dependencies, boms);
    }

    private void removeSpringbootDependenciesFromRoot(DependencyContainer dependencies, BomContainer boms) {
        List<String> dependencyIds = dependencies.items()
                .filter(dependency -> ORG_SPRINGFRAMEWORK_BOOT.equals(dependency.getGroupId()))
                .map(io.spring.initializr.generator.buildsystem.Dependency::getId)
                .collect(Collectors.toList());
        dependencies.removeAll(dependencyIds);

        List<String> bomIds = boms.items()
                .filter(bom -> ORG_SPRINGFRAMEWORK_BOOT.equals(bom.getGroupId()))
                .map(io.spring.initializr.generator.buildsystem.BillOfMaterials::getId)
                .collect(Collectors.toList());
        boms.removeAll(bomIds);
    }

    private List<io.spring.initializr.generator.buildsystem.Dependency> filterModuleDependency(DependencyContainer dependencies) {
        return dependencies.items()
                .filter(dependency -> !StringUtils.isEmpty(dependency.getModule()))
                .collect(Collectors.toList());
    }


    private void addDefaultStarterDependency(MavenBuild mavenBuild) {
        DependencyContainer dependencies = mavenBuild.dependencies();

        // spring-boot-starter
        io.spring.initializr.metadata.Dependency root = new Dependency();
        root.setId(DEFAULT_STARTER);
        root.asSpringBootStarter("");
        dependencies.add(DEFAULT_STARTER,
                MetadataBuildItemMapper.toDependency(root));
    }


}
