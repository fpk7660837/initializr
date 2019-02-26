package io.spring.initializr.start.code.mvc;

import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.*;

import java.lang.reflect.Modifier;

/**
 * Date : 2019-02-22
 * Author : pengkai.fu
 */
public class WebMvcCustomizer implements WebMvcCodeCustomizer<JavaTypeDeclaration> {


    @Override
    public void customize(JavaTypeDeclaration typeDeclaration) {

        JavaMethodDeclaration addInterceptors = JavaMethodDeclaration.method("addInterceptors")
                .modifiers(Modifier.PUBLIC)
                .returning("void")
                .parameters(new Parameter("org.springframework.web.servlet.config.annotation.InterceptorRegistry",
                        "interceptorRegistry"))
                .body();
        addInterceptors.annotate(Annotation.name("java.lang.Override"));
        typeDeclaration.addMethodDeclaration(addInterceptors);


        JavaMethodDeclaration responseBodyConverter = JavaMethodDeclaration.method("responseBodyConverter")
                .modifiers(Modifier.PUBLIC)
                .returning("org.springframework.http.converter.HttpMessageConverter<String>")
                .body(new JavaExpressionStatement(
                        new JavaMethodInvocation("java.nio.charset.Charset", "forName", "java.nio.charset.Charset", "defaultCharset")
                                .argument("\"UTF-8\"")
                ), new JavaExpressionStatement(
                        new JavaMethodInvocation("org.springframework.http.converter.StringHttpMessageConverter", "responseBodyConverter",
                                "StringHttpMessageConverter")
                                .argument("defaultCharset")
                ));
        responseBodyConverter.annotate(Annotation.name("org.springframework.context.annotation.Bean"));
        typeDeclaration.addMethodDeclaration(responseBodyConverter);


        JavaMethodDeclaration configureMessageConverters = JavaMethodDeclaration.method("configureMessageConverters")
                .modifiers(Modifier.PUBLIC)
                .returning("void")
                .parameters(new Parameter("java.util.List<HttpMessageConverter>", "converters"))
                .body(new JavaReturnStatement(
                        new JavaMethodInvocation("converters", "add")
                                .argument("responseBodyConverter())")
                ));
        configureMessageConverters.annotate(Annotation.name("java.lang.Override"));
        typeDeclaration.addMethodDeclaration(configureMessageConverters);

    }
}
