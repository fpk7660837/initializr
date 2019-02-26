package io.spring.initializr.start.code.mybatis;

import io.spring.initializr.generator.language.TypeDeclaration;
import org.springframework.core.Ordered;

/**
 * Date : 2019-02-21
 * Author : pengkai.fu
 */
@FunctionalInterface
public interface MybatisPlusCodeCustomizer<T extends TypeDeclaration> extends Ordered {

    void customize(T typeDeclaration);

    @Override
    default int getOrder() {
        return 0;
    }
}