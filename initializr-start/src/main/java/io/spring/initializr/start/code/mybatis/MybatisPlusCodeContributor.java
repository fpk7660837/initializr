package io.spring.initializr.start.code.mybatis;

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
public class MybatisPlusCodeContributor implements MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {


    private ObjectProvider<MybatisPlusCodeCustomizer<?>> mybatisPlusCodeCustomizers;

    private ResolvedProjectDescription projectDescription;


    public MybatisPlusCodeContributor(ObjectProvider<MybatisPlusCodeCustomizer<?>> mybatisPlusCodeCustomizers, ResolvedProjectDescription projectDescription) {
        this.mybatisPlusCodeCustomizers = mybatisPlusCodeCustomizers;
        this.projectDescription = projectDescription;
    }

    @Override
    public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
        String suffix = DefaultModuleTopology.Modules.DAO
                .getSuffix();
        CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
                .createCompilationUnit(projectDescription.getPackageName(suffix), "MybatisConfig", projectDescription.getModuleName(suffix));
        TypeDeclaration mybatisConfig = compilationUnit
                .createTypeDeclaration("MybatisConfig");
        mybatisConfig.annotate(Annotation.name("org.springframework.context.annotation.Configuration"));
        mybatisConfig.annotate(Annotation.name("org.mybatis.spring.annotation.MapperScan",
                builder -> builder.attribute("basePackages", String.class, "this need to be define yourself")));
        customizeMybatisConfig(mybatisConfig);

    }

    @SuppressWarnings("unchecked")
    private void customizeMybatisConfig(TypeDeclaration mybatisConfig) {
        List<MybatisPlusCodeCustomizer<?>> customizers = this.mybatisPlusCodeCustomizers
                .orderedStream().collect(Collectors.toList());
        LambdaSafe
                .callbacks(MybatisPlusCodeCustomizer.class, customizers,
                        mybatisConfig)
                .invoke((customizer) -> customizer.customize(mybatisConfig));
    }




}
