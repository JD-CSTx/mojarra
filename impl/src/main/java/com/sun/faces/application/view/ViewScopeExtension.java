/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.faces.application.view;

import static com.sun.faces.cdi.CdiUtils.addAnnotatedTypes;

import java.util.logging.Logger;

import com.sun.faces.util.FacesLogger;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBean;
import jakarta.faces.view.ViewScoped;

/**
 * The CDI extension that makes ViewScoped beans work in a CDI context.
 */
public class ViewScopeExtension implements Extension {

    private static final Logger LOGGER = FacesLogger.APPLICATION_VIEW.getLogger();

    public ViewScopeExtension() {
        LOGGER.finest("Constructor @ViewScoped CDI Extension called");
    }

    /**
     * Before bean discovery.
     *
     * @param beforeBeanDiscovery the before bean discovery.
     * @param beanManager the bean manager.
     */
    public void beforeBean(@Observes BeforeBeanDiscovery beforeBeanDiscovery, BeanManager beanManager) {
        addAnnotatedTypes(beforeBeanDiscovery, beanManager, ViewScopedCDIEventFireHelperImpl.class);
    }

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager) {
        event.addScope(ViewScoped.class, true, true);
    }

    /**
     * Processing bean.
     *
     * @param event the event.
     */
    public void processBean(@Observes ProcessBean<?> event) {
        ViewScoped viewScoped = event.getAnnotated().getAnnotation(ViewScoped.class);
        if (viewScoped != null) {
            LOGGER.finest("Processing occurrence of @ViewScoped");
        }
    }

    /**
     * After bean discovery.
     *
     * @param event the event.
     * @param beanManager the bean manager.
     */
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        LOGGER.finest("Adding @ViewScoped context to CDI runtime");
        event.addContext(new ViewScopeContext());
    }

}
