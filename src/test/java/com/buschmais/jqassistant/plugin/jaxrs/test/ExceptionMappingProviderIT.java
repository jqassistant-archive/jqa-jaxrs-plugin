package com.buschmais.jqassistant.plugin.jaxrs.test;

import javax.ws.rs.ext.ExceptionMapper;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.jaxrs.test.set.beans.BookResolver;
import com.buschmais.jqassistant.plugin.jaxrs.test.set.beans.NotFoundExceptionMapper;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test to verify JAX-RS Exception Mapping Provider concepts.
 *
 * @author Aparna Chaudhary
 */
class ExceptionMappingProviderIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept {@code jaxrs:ExceptionMappingProvider} for
     * {@link ExceptionMapper}.
     *
     * @throws java.io.IOException
     *             If the test fails.
     * @throws NoSuchMethodException
     *             If the test fails.
     */
    @Test
    void exceptionMappingProviderConcept() throws Exception {
        scanClasses(NotFoundExceptionMapper.class, BookResolver.class);
        assertThat(applyConcept("jaxrs:ExceptionMappingProvider").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat("Expected exceptionMappingProvider", query("MATCH (provider:JaxRS:ExceptionMappingProvider) RETURN provider").getColumn("provider"),
                hasItem(typeDescriptor(NotFoundExceptionMapper.class)));

        assertThat("UnExpected exceptionMappingProvider", query("MATCH (provider:JaxRS:ExceptionMappingProvider) RETURN provider").getColumn("provider"),
                not(hasItem(typeDescriptor(BookResolver.class))));
        store.commitTransaction();
    }

}
