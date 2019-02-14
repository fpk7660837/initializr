package io.spring.initializr.start.code;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import org.springframework.context.annotation.Configuration;

/**
 * Date : 2019-02-13
 * Author : pengkai.fu
 */
@Configuration
public class ExtensionConditionalConfiguration {


    @ConditionalOnRequestedDependency("dubbo")
    static class DubboCondition {


        // public DubboConfigCustomizer<JavaTypeDeclaration> dubboConfigCustomizer(ResolvedProjectDescription projectDescription) {
        //     return typeDeclaration -> {
        //
        //         JavaMethodDeclaration
        //                 .method("registryConfig")
        //                 .modifiers(Modifier.PUBLIC)
        //                 .parameters(new p)
        //
        //
        //
        //     }
        //
        // }


    }
}
