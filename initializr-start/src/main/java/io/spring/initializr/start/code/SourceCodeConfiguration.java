package io.spring.initializr.start.code;

import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.start.code.dubbo.DubboConfigCodeContributor;
import io.spring.initializr.start.code.dubbo.DubboConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * all external open source code configuration which used for project
 * eg. dubbo,redis,mybatis
 * Date : 2019-02-13
 * Author : pengkai.fu
 */
@ProjectGenerationConfiguration
public class SourceCodeConfiguration {


    // dubbo config bean definition
    @Configuration
    static class DubboConfig {

        @Bean
        public DubboConfigCodeContributor dubboSourceCodeCustomizer(DubboConfigCustomizer<TypeDeclaration> dubboConfigCustomizer) {
            return new DubboConfigCodeContributor(dubboConfigCustomizer);
        }

    }


}
