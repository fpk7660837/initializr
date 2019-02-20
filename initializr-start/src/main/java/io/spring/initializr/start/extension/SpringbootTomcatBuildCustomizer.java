package io.spring.initializr.start.extension;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenPlugin;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2019-02-20
 * Author : pengkai.fu
 */
public class SpringbootTomcatBuildCustomizer implements BuildCustomizer<MavenBuild> {

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void customize(MavenBuild build) {
        List<MavenBuild> webModuleBuild = getWebModuleBuild(build);

        webModuleBuild.forEach(webBuild -> {
            addTomcatPlugin(webBuild);
            addSpringbootPlugin(webBuild);
            addMavenSourcePlugin(webBuild);
        });


    }

    private void addSpringbootPlugin(MavenBuild webBuild) {
        webBuild.plugin("org.springframework.boot", "spring-boot-maven-plugin");
    }

    private void addTomcatPlugin(MavenBuild webBuild) {
        MavenPlugin springbootTomcat = webBuild.plugin("org.apache.tomcat.maven",
                "tomcat7-maven-plugin", "2.2");

        springbootTomcat.configuration(configurationCustomization -> {
            configurationCustomization.add("port", "8080");
            configurationCustomization.add("path", "/");
            configurationCustomization.add("uriEncoding", "utf-8");
        });
    }


    private void addMavenSourcePlugin(MavenBuild webBuild) {
        MavenPlugin springbootTomcat = webBuild.plugin("org.apache.maven.plugins",
                "maven-resources-plugin", "2.6");

        springbootTomcat.configuration(configurationCustomization -> {
            configurationCustomization.add("useDefaultDelimiters", "true");
        });
    }


    private List<MavenBuild> getWebModuleBuild(MavenBuild build) {
        List<MavenBuild> childBuilds = build.getChildBuilds();

        return childBuilds.stream()
                .filter(build1 -> "war".equals(build1.getPackaging()))
                .collect(Collectors.toList());
    }
}
