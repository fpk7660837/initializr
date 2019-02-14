package io.spring.initializr.start.code;

import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.start.code.dubbo.DubboConfigCodeContributor;
import io.spring.initializr.start.code.dubbo.DubboConfigCustomizer;
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

        @Bean
        public DubboConfigCodeContributor dubboSourceCodeCustomizer(ObjectProvider<DubboConfigCustomizer<TypeDeclaration>> dubboConfigCustomizer) {
            return new DubboConfigCodeContributor(projectDescription.getPackageName(), dubboConfigCustomizer);
        }

    }


}
