package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.BomContainer;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.module.DefaultModuleTopology;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2019-02-18
 * Author : pengkai.fu
 */
public class BaseChildDependencyMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {


    private static final String ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";


    // extension here
    @Override
    public void customize(MavenBuild build) {
        DependencyContainer dependencies = build.dependencies();

        for (MavenBuild childBuild : build.getChildBuilds()) {
            dependencies.items().forEach(dependency -> {
                if (null != dependency.getModules() && dependency.getModules()
                        .contains(childBuild.getSuffix())) {
                    childBuild.dependencies()
                            .add(dependency.getId(), dependency);
                } else if (null == dependency.getModules() && DefaultModuleTopology.Modules.COMMON.getSuffix()
                        .equals(childBuild.getSuffix())) {
                    // default behavior, set the dependency to common module
                    childBuild.dependencies()
                            .add(dependency.getId(), dependency);
                }
            });
        }

        BomContainer boms = build.boms();
        removeSpringbootDependenciesFromRoot(dependencies, boms);
    }

    private void removeSpringbootDependenciesFromRoot(DependencyContainer dependencies, BomContainer boms) {
        // remove all dependencies from root pom
        dependencies.clear();

        // remove org.springframework.boot dependencies management from root pom
        List<String> bomIds = boms.items()
                .filter(bom -> ORG_SPRINGFRAMEWORK_BOOT.equals(bom.getGroupId()))
                .map(io.spring.initializr.generator.buildsystem.BillOfMaterials::getId)
                .collect(Collectors.toList());
        boms.removeAll(bomIds);
    }

    private List<io.spring.initializr.generator.buildsystem.Dependency> filterModuleDependency(DependencyContainer dependencies) {
        return dependencies.items()
                .filter(dependency -> !CollectionUtils.isEmpty(dependency.getModules()))
                .collect(Collectors.toList());
    }


}
