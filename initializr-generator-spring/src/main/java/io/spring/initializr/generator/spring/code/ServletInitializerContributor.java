/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator.spring.code;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.util.LambdaSafe;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link MainSourceCodeCustomizer} that contributes the servlet initializer to
 * applications using war packaging.
 *
 * @author Andy Wilkinson
 */
class ServletInitializerContributor implements
        MainSourceCodeCustomizer<TypeDeclaration, CompilationUnit<TypeDeclaration>, SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>>> {


    private final String initializerClassName;

    private final ObjectProvider<ServletInitializerCustomizer<?>> servletInitializerCustomizers;

    private final ResolvedProjectDescription projectDescription;

    ServletInitializerContributor(String initializerClassName,
                                  ObjectProvider<ServletInitializerCustomizer<?>> servletInitializerCustomizers,
                                  ResolvedProjectDescription projectDescription) {
        this.initializerClassName = initializerClassName;
        this.servletInitializerCustomizers = servletInitializerCustomizers;
        this.projectDescription = projectDescription;
    }

    @Override
    public void customize(
            SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode) {
        createServletInitializer(sourceCode, projectDescription.getModuleName("web"), "web");
        createServletInitializer(sourceCode, projectDescription.getModuleName("api"), "api");
    }

    private void createServletInitializer(SourceCode<TypeDeclaration, CompilationUnit<TypeDeclaration>> sourceCode, String module, String suffix) {
        CompilationUnit<TypeDeclaration> compilationUnit = sourceCode
                .createCompilationUnit(projectDescription.getPackageName(suffix), "ServletInitializer", module);
        TypeDeclaration servletInitializer = compilationUnit
                .createTypeDeclaration("ServletInitializer");
        servletInitializer.extend(this.initializerClassName);
        customizeServletInitializer(servletInitializer);
    }

    @SuppressWarnings("unchecked")
    private void customizeServletInitializer(TypeDeclaration servletInitializer) {
        List<ServletInitializerCustomizer<?>> customizers = this.servletInitializerCustomizers
                .orderedStream().collect(Collectors.toList());
        LambdaSafe
                .callbacks(ServletInitializerCustomizer.class, customizers,
                        servletInitializer)
                .invoke((customizer) -> customizer.customize(servletInitializer));
    }

}
