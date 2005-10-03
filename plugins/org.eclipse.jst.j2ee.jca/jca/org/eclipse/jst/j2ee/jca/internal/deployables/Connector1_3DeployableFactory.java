/***************************************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.jst.j2ee.jca.internal.deployables;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.internal.deployables.J2EEDeployableFactory;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;

public class Connector1_3DeployableFactory extends J2EEDeployableFactory {
	private static final String ID = "org.eclipse.jst.j2ee.server.connector13"; //$NON-NLS-1$

	/**
	 * Constructor for Connector1_3DeployableFactory
	 */
	public Connector1_3DeployableFactory() {
		super();
	}

	/*
	 * @see DeployableProjectFactoryDelegate#getFactoryID()
	 */
	public String getFactoryId() {
		return ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.server.core.model.ModuleFactoryDelegate#getModules()
	 */
	public IModule[] getModules() {
        cacheModules(false);
        ArrayList moduleList = new ArrayList();
        for (Iterator iter = projects.values().iterator(); iter.hasNext();) {
            IModule[] element = (IModule[]) iter.next();
            for (int j = 0; j < element.length; j++) {
                moduleList.add(element[j]);
            }
        }
        IModule[] modules = new IModule[moduleList.size()];
        moduleList.toArray(modules);
        return modules;
	}
	
	protected boolean isValidModule(IProject project) {
		if (isFlexibleProject(project)) {
	       IVirtualComponent comp = ComponentCore.createComponent(project);
            if(IModuleConstants.JST_CONNECTOR_MODULE.equals(comp.getComponentTypeId()))
                return true;
        }
        return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jst.j2ee.internal.deployables.J2EEDeployableFactory#createModuleDelegates(org.eclipse.emf.common.util.EList, org.eclipse.core.resources.IProject)
	 */
	protected List createModuleDelegates(IVirtualComponent component) throws CoreException {
        ConnectorFlexibleDeployable moduleDelegate = null;
        IModule module = null;
        List moduleList = new ArrayList();
        try {
            if(IModuleConstants.JST_CONNECTOR_MODULE.equals(component.getComponentTypeId())) {
                moduleDelegate = new ConnectorFlexibleDeployable(component.getProject(), ID, component);
                module = createModule(component.getName(), component.getName(), moduleDelegate.getType(), moduleDelegate.getVersion(), moduleDelegate.getProject());
                moduleList.add(module);
                moduleDelegate.initialize(module);
            }
        } catch (Exception e) {
            Logger.getLogger().write(e);
        } finally {
            if (module != null) {
                if (getModuleDelegate(module) == null)
                    moduleDelegates.add(moduleDelegate);
            }
        }
        return moduleList;
	}

}