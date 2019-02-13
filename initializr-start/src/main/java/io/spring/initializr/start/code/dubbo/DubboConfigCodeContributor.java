package io.spring.initializr.start.code.dubbo;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.spring.code.MainSourceCodeCustomizer;

/**
 * Date : 2019-02-13
 * Author : pengkai.fu
 */
public class DubboConfigCodeContributor implements MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {


    private DubboConfigCustomizer<TypeDeclaration> dubboConfigCustomizer;


    public DubboConfigCodeContributor(DubboConfigCustomizer<TypeDeclaration> dubboConfigCustomizer) {
        this.dubboConfigCustomizer = dubboConfigCustomizer;
    }

    @Override
    public void customize(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {

    }
}
