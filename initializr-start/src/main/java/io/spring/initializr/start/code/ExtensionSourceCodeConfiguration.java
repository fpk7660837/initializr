package io.spring.initializr.start.code;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.java.*;
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
                // // todo write plain java code for mybatis config
                JavaMethodDeclaration configure = JavaMethodDeclaration
                        .method("dataSource").modifiers(Modifier.PROTECTED)
                        .returning("com.alibaba.druid.pool.DruidDataSource")
                        .parameters()
                        .body(new JavaExpressionStatement(
                                        new JavaMethodInvocation(" org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder", "create",
                                                "org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder",
                                                "dataSourceBuilder")),
                                new JavaExpressionStatement(
                                        new JavaMethodInvocation("org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder", "type",
                                                "org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder",
                                                "type").argument("DruidDataSource.class")),
                                new JavaExpressionStatement(
                                        new JavaMethodInvocation("type", "build",
                                                "com.alibaba.druid.pool.DruidDataSource",
                                                "dataSource")),
                                new JavaReturnStatement(
                                        new JavaMethodInvocation("", "","","dataSource")));

                configure.annotate(Annotation.name("org.springframework.context.annotation.Bean", builder -> {
                    builder.attribute("name", String.class, "dataSource");
                    builder.attribute("initMethod", String.class, "init");
                }));
                configure.annotate(Annotation.name("org.springframework.context.annotation.Primary"));
                configure.annotate(Annotation.name("org.springframework.boot.context.properties.ConfigurationProperties", builder -> {
                    builder.attribute("prefix", String.class, "spring.datasource");
                }));
                typeDeclaration.addMethodDeclaration(configure);
            };
        }
    }
}
