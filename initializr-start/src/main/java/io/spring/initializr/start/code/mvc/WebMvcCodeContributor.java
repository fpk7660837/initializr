package io.spring.initializr.start.code.mvc;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.project.module.DefaultModuleTopology;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2019-02-21
 * Author : pengkai.fu
 */
public class WebMvcCodeContributor implements MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {


    private ObjectProvider<WebMvcCodeCustomizer<?>> webMvcCodeCustomizers;

    private ResolvedProjectDescription projectDescription;


    public WebMvcCodeContributor(ObjectProvider<WebMvcCodeCustomizer<?>> webMvcCodeCustomizers, ResolvedProjectDescription projectDescription) {
        this.webMvcCodeCustomizers = webMvcCodeCustomizers;
        this.projectDescription = projectDescription;
    }

    @Override
    public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
        String suffix = DefaultModuleTopology.Modules.WEB
                .getSuffix();
        CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
                .createCompilationUnit(projectDescription.getPackageName(suffix), "WebMvcConfig", projectDescription.getModuleName(suffix));
        TypeDeclaration webMvcConfig = compilationUnit
                .createTypeDeclaration("WebMvcConfig");
        webMvcConfig.annotate(Annotation.name("org.springframework.context.annotation.Configuration"));
        webMvcConfig.impl("org.springframework.web.servlet.config.annotation.WebMvcConfigurer");
        customizeWebMvcConfig(webMvcConfig);
    }

    @SuppressWarnings("unchecked")
    private void customizeWebMvcConfig(TypeDeclaration webMvcConfig) {
        List<WebMvcCodeCustomizer<?>> customizers = this.webMvcCodeCustomizers
                .orderedStream().collect(Collectors.toList());
        LambdaSafe
                .callbacks(WebMvcCodeCustomizer.class, customizers,
                        webMvcConfig)
                .invoke((customizer) -> customizer.customize(webMvcConfig));
    }
}
