/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.presentation.cmnbirest.resources.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("")
public class CmNbiRestResourceMapper extends Application {
    private final Set<Object> resourceObjects = new HashSet<>();
    private final Set<Class<?>> resourceClasses = new HashSet<>();

    public CmNbiRestResourceMapper() {
        resourceClasses.add(CmNbiCrudResource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resourceClassesCopy = new HashSet<>();
        resourceClassesCopy.addAll(resourceClasses);
        return resourceClassesCopy;

    }

    @Override
    public Set<Object> getSingletons() {
        final Set<Object> resourceObjectsCopy = new HashSet<>();
        resourceObjectsCopy.addAll(resourceObjects) ;
        return resourceObjectsCopy;
    }
}

