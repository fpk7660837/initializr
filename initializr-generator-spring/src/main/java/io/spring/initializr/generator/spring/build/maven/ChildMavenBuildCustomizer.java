package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.module.Module;
import io.spring.initializr.generator.project.module.ModuleTopology;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionReference;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Date : 2019-02-15
 * Author : pengkai.fu
 */
public class ChildMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

    private BuildItemResolver buildItemResolver;

    private ModuleTopology moduleTopology;

    public ChildMavenBuildCustomizer(BuildItemResolver buildItemResolver, ModuleTopology moduleTopology) {
        this.buildItemResolver = buildItemResolver;
        this.moduleTopology = moduleTopology;
    }

    @Override
    public void customize(MavenBuild build) {
        List<String> allModuleNames = moduleTopology.getAllModuleNames();

        for (String moduleName : allModuleNames) {
            addChildMavenBuild(build, moduleTopology.getModule(moduleName));
        }
    }

    public void addChildMavenBuild(MavenBuild build, Module module) {
        addChildMavenBuild(build, module.getName(), module.getPackaging(), module.getChildModules());
    }

    public void addChildMavenBuild(MavenBuild build, String name, String packaging, List<Module> childModules) {
        MavenBuild childBuild = build.childBuild(buildItemResolver);
        childBuild.parent(build.getGroup(), build.getArtifact(), build.getVersion());
        childBuild.setArtifact(name);
        childBuild.setPackaging(packaging);

        if (CollectionUtils.isEmpty(childModules)) {
            return;
        }

        addReferDependency(build, childBuild, childModules);
    }


    public void addReferDependency(MavenBuild build, MavenBuild childBuild, List<Module> childModules) {
        DependencyContainer dependencies = childBuild.dependencies();

        childModules
                .forEach(module -> {
                    String name = module.getName();
                    dependencies.add(name, build.getGroup(), name, VersionReference.ofValue("${project.parent.version}"),
                            DependencyScope.COMPILE);
                });
    }


}
