package io.spring.initializr.start.code;

import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.start.code.mvc.WebMvcCodeContributor;
import io.spring.initializr.start.code.mvc.WebMvcCodeCustomizer;
import io.spring.initializr.start.code.mvc.WebMvcCustomizer;
import io.spring.initializr.start.code.mybatis.MybatisDaoCodeProjectContributor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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


    // @Configuration
    // static class MybatisConfig {
    //
    //     private final ResolvedProjectDescription projectDescription;
    //
    //     public MybatisConfig(ResolvedProjectDescription projectDescription) {
    //         this.projectDescription = projectDescription;
    //     }
    //
    //
    //     @Bean
    //     public MybatisPlusCodeContributor mybatisPlusCodeContributor(
    //             ObjectProvider<MybatisPlusCodeCustomizer<?>> mybatisPlusCodeCustomizers) {
    //         return new MybatisPlusCodeContributor( mybatisPlusCodeCustomizers, projectDescription);
    //     }
    //
    //
    //     @Bean
    //     public MybatisPlusCustomizer mybatisPlusCustomizer() {
    //         return new MybatisPlusCustomizer();
    //     }
    // }

    @Bean
    public MybatisDaoCodeProjectContributor mybatisDaoCodeProjectContributor(ResolvedProjectDescription projectDescription) {
        return new MybatisDaoCodeProjectContributor(projectDescription);
    }


    @Configuration
    static class WebMvcConfig {

        private final ResolvedProjectDescription projectDescription;

        public WebMvcConfig(ResolvedProjectDescription projectDescription) {
            this.projectDescription = projectDescription;
        }


        @Bean
        public WebMvcCodeContributor webMvcCodeContributor(ObjectProvider<WebMvcCodeCustomizer<?>> webMvcCodeCustomizers) {
            return new WebMvcCodeContributor(webMvcCodeCustomizers, projectDescription);
        }

        @Bean
        public WebMvcCustomizer webMvcCustomizer() {
            return new WebMvcCustomizer();
        }

    }
}
