package io.spring.initializr.start.code;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.start.code.mybatis.MybatisPlusCodeContributor;
import io.spring.initializr.start.code.mybatis.MybatisPlusCodeCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;

/**
 * all external open source code configuration which used for project
 * eg. dubbo,redis,mybatis
 * Date : 2019-02-13
 * Author : pengkai.fu
 */
@ProjectGenerationConfiguration
public class ExtensionSourceCodeConfiguration {


    // dubbo config bean definition
    @Configuration
    static class DubboConfig {

        private final ResolvedProjectDescription projectDescription;

        public DubboConfig(ResolvedProjectDescription projectDescription) {
            this.projectDescription = projectDescription;
        }


    }


    @Configuration
    static class MybatisConfig {

        private final ResolvedProjectDescription projectDescription;

        public MybatisConfig(ResolvedProjectDescription projectDescription) {
            this.projectDescription = projectDescription;
        }


        @Bean
        public MybatisPlusCodeContributor mybatisPlusCodeContributor(
                ObjectProvider<MybatisPlusCodeCustomizer<?>> mybatisPlusCodeCustomizers,
                ResolvedProjectDescription projectDescription) {
            return new MybatisPlusCodeContributor(
                    this.projectDescription.getPackageName(), mybatisPlusCodeCustomizers, projectDescription);
        }


        @Bean
        public MybatisPlusCodeCustomizer<JavaTypeDeclaration> mybatisPlusCodeCustomizer(
                ResolvedProjectDescription projectDescription) {
            return (typeDeclaration) -> {
                // todo write plain java code for mybatis config
                JavaMethodDeclaration configure = JavaMethodDeclaration
                        .method("configure").modifiers(Modifier.PROTECTED)
                        .returning(
                                "org.springframework.boot.builder.SpringApplicationBuilder")
                        .parameters(new Parameter(
                                "org.springframework.boot.builder.SpringApplicationBuilder",
                                "application"))
                        .body(new JavaReturnStatement(
                                new JavaMethodInvocation("application", "sources",
                                        projectDescription.getApplicationName())));
                configure.annotate(Annotation.name("java.lang.Override"));
                typeDeclaration.addMethodDeclaration(configure);
            };
        }
    }
}
