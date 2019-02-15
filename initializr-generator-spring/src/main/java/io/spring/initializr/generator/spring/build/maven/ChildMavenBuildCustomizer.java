package io.spring.initializr.generator.spring.build.maven;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * Date : 2019-02-15
 * Author : pengkai.fu
 */
public class ChildMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {


    @Override
    public void customize(MavenBuild build) {
        addChildMavenBuild(build, "web", "war");
        addChildMavenBuild(build, "api", "war");
        addChildMavenBuild(build, "rpc");
        addChildMavenBuild(build, "service");
        addChildMavenBuild(build, "dao");
        addChildMavenBuild(build, "mq");
        addChildMavenBuild(build, "sdk");
        addChildMavenBuild(build, "common");
    }

    public void addChildMavenBuild(MavenBuild build, String name) {
        addChildMavenBuild(build, name, null);
    }

    public void addChildMavenBuild(MavenBuild build, String name, String packaging) {
        MavenBuild childBuild = build.childBuild(name);
        childBuild.parent(build.getGroup(), build.getArtifact(), build.getVersion());
        childBuild.setArtifact(name);
        childBuild.setPackaging(packaging);
    }

}
