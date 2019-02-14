package io.spring.initializr.start.code.dubbo;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;
import io.spring.initializr.generator.spring.util.LambdaSafe;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2019-02-13
 * Author : pengkai.fu
 */
public class DubboConfigCodeContributor implements MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {


    private final String packageName;


    private ObjectProvider<DubboConfigCustomizer<TypeDeclaration>> dubboConfigCustomizer;


    public DubboConfigCodeContributor(String packageName, ObjectProvider<DubboConfigCustomizer<TypeDeclaration>> dubboConfigCustomizer) {
        this.packageName = packageName;
        this.dubboConfigCustomizer = dubboConfigCustomizer;
    }

    @Override
    public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
        CompilationUnit<TypeDeclaration> compilationUnit = sourceCode.createCompilationUnit(this.packageName, "DubboConfig");
        TypeDeclaration dubboConfig = compilationUnit.createTypeDeclaration("DubboConfig");
        dubboConfig.annotate(Annotation.name("org.springframework.context.annotation.Configuration"));
        customizeDubboConfig(dubboConfig);
    }


    @SuppressWarnings("unchecked")
    private void customizeDubboConfig(TypeDeclaration dubboConfig) {
        List<DubboConfigCustomizer<TypeDeclaration>> customizers = this.dubboConfigCustomizer.orderedStream()
                .collect(Collectors.toList());

        LambdaSafe.callbacks(DubboConfigCustomizer.class, customizers, dubboConfig)
                .invoke((customizer) -> customizer.customize(dubboConfig));
    }
}
