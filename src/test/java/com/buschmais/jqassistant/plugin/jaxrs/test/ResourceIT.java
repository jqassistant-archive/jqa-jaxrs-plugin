package com.buschmais.jqassistant.plugin.jaxrs.test;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.jaxrs.test.set.beans.MyRestResource;

import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.WARNING;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Test to verify JAX-RS Resource concepts.
 *
 * @author Aparna Chaudhary
 */
class ResourceIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept {@code jaxrs:Resource}.
     *
     * @throws java.io.IOException
     *             If the test fails.
     * @throws NoSuchMethodException
     *             If the test fails.
     */
    @Test
    void resourceConcept() throws Exception {
        scanClasses(MyRestResource.class);
        assertThat(applyConcept("jaxrs:Resource").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat("Expected RestResource", query("MATCH (getResource:JaxRS:Resource) RETURN getResource").getColumn("getResource"),
                hasItem(typeDescriptor(MyRestResource.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept {@code jaxrs:Resource} is not applied to invalid
     * Resource classes.
     *
     * @throws java.io.IOException
     *             If the test fails.
     * @throws NoSuchMethodException
     *             If the test fails.
     */
    @Test
    void testInvalid_Resource_Concept() throws Exception {
        scanClasses(ResourceIT.class);
        assertThat(applyConcept("jaxrs:Resource").getStatus(), equalTo(WARNING));
        store.beginTransaction();
        assertThat("Unexpected RestResource", query("MATCH (getResource:JaxRS:Resource) RETURN getResource").getRows(), empty());
        store.commitTransaction();

    }

}
