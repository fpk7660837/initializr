package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenProfile;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class ProfileMavenBuilderCustomizer implements BuildCustomizer<MavenBuild> {


    @Override
    public void customize(MavenBuild build) {
        addProfile(build, "dev", true);
        addProfile(build, "test", false);
        addProfile(build, "online", false);
    }


    private void addProfile(MavenBuild build, String id, boolean activation) {
        MavenProfile profile = build.profile(id, activation);
        profile.properties("env", id);
    }


}
