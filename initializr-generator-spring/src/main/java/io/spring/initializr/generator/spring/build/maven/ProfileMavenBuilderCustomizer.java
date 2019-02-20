package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.MavenProfile;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.MetadataBuildItemMapper;
import io.spring.initializr.metadata.Dependency;

/**
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class ProfileMavenBuilderCustomizer implements BuildCustomizer<MavenBuild> {


    private BuildItemResolver buildItemResolver;

    public ProfileMavenBuilderCustomizer(BuildItemResolver buildItemResolver) {
        this.buildItemResolver = buildItemResolver;
    }

    @Override
    public void customize(MavenBuild build) {
        Dependency root = new Dependency();
        root.setId("tomcat");
        root.asSpringBootStarter("tomcat");
        root.setScope(Dependency.SCOPE_PROVIDED);

        io.spring.initializr.generator.buildsystem.Dependency dependency = MetadataBuildItemMapper.toDependency(root);

        addProfile(build, "dev", true, null);
        addProfile(build, "test", false, dependency);
        addProfile(build, "online", false, dependency);
    }


    private void addProfile(MavenBuild build, String id, boolean activation, io.spring.initializr.generator.buildsystem.Dependency dependency) {
        MavenProfile profile = build.profile(id, activation,buildItemResolver);
        profile.properties("env", id);
        profile.addDependency(dependency);

    }


}
