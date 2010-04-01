/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.project;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.componentcore.J2EEModuleVirtualComponent;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.common.XMLResource;
import org.eclipse.jst.j2ee.internal.common.classpath.J2EEComponentClasspathUpdater;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebAppVersionType;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.eclipse.wst.common.componentcore.internal.util.ComponentUtilities;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;

public class WebUtilities extends JavaEEProjectUtilities {
	public static IPath WEBLIB = new Path("/WEB-INF/lib"); //$NON-NLS-1$

	public WebUtilities() {
	}

	/**
	 * <p>
	 * Retrieves Servlet version information derived from the {@see WebAppResource}.
	 * </p>
	 * 
	 * @return an integer representation of a module version
	 * 
	 */
	public static int getServletVersion(IProject webProject) {
		int retVal = 0;
		Object webAppResource = ModelProviderManager.getModelProvider(webProject).getModelObject();
		if (webAppResource instanceof XMLResource)
		{
			retVal = ((XMLResource)webAppResource).getModuleVersionID();
		}
		else if (webAppResource instanceof WebApp)
		{
			switch (((WebApp)webAppResource).getVersion().getValue()) {
				//EE6TODO
				case WebAppVersionType._25: 
					retVal = J2EEVersionConstants.WEB_2_5_ID;
					break;
			}
		}

		return retVal;
	}

	/**
	 * This method returns the integer representation for the JSP specification level associated
	 * with the J2EE version for this workbench module. This method will not return null and returns
	 * 20 as default.
	 * 
	 * @return an integer representation of the JSP level
	 */
	public static int getJSPVersion(IProject webProject) {
		int servletVersion = getServletVersion(webProject);
		if (servletVersion == J2EEVersionConstants.WEB_2_2_ID)
			return J2EEVersionConstants.JSP_1_1_ID;
		else if (servletVersion == J2EEVersionConstants.WEB_2_3_ID)
			return J2EEVersionConstants.JSP_1_2_ID;
		else
			return J2EEVersionConstants.JSP_2_0_ID;
	}

	/**
	 * Returns a list of WebFragment components for the specified dynamic web component.
	 * @param webComponent
	 * @return
	 */
	public static List <IVirtualComponent> getWebFragments(IVirtualComponent webComponent){
		List <IVirtualComponent>result = new ArrayList<IVirtualComponent>();
		IVirtualReference[] refComponents = webComponent.getReferences();
		for(IVirtualReference virtualReference : refComponents){
			if(virtualReference.getRuntimePath().equals(WEBLIB)){
				IVirtualComponent virtualComponent = virtualReference.getReferencedComponent();
				if(JavaEEProjectUtilities.isWebFragmentProject(virtualComponent)){
					result.add(virtualComponent);
				}
			}
		}
		IVirtualFolder rootFolder = webComponent.getRootFolder();
		IVirtualFolder webLibFolder = rootFolder.getFolder(WEBLIB);
		if(webLibFolder.exists()){
			try {
				IVirtualResource [] webLibs = webLibFolder.members();
				for(IVirtualResource webLib : webLibs){
					if(webLib.getType() == IVirtualResource.FILE && J2EEComponentClasspathUpdater.endsWithIgnoreCase(webLib.getName(), IJ2EEModuleConstants.JAR_EXT)){
						IResource iResource = webLib.getUnderlyingResource();
						IVirtualComponent virtualComponent = ComponentCore.createArchiveComponent(webComponent.getProject(), VirtualArchiveComponent.LIBARCHIVETYPE + iResource.getFullPath().toString());
						if(JavaEEProjectUtilities.isWebFragmentProject(virtualComponent)){
							result.add(virtualComponent);
						}
					}
				}
			} catch (CoreException e) {
				org.eclipse.jst.j2ee.internal.plugin.J2EEPlugin.logError(e);
			}
		}
		return result;
	}
	
	
	/**
	 * This method will return the list of dependent modules which are utility jars in the web lib
	 * folder of the deployed path of the module. It will not return null.
	 * 
	 * @return array of the web library dependent modules
	 */
	public static IVirtualReference[] getLibModules(IVirtualComponent webComponent){
		List result = new ArrayList();
		IVirtualReference[] refComponents = null;
		if (!webComponent.isBinary())
			refComponents = ((J2EEModuleVirtualComponent)webComponent).getNonManifestReferences();
		else
			refComponents = webComponent.getReferences();
		// Check the deployed path to make sure it has a lib parent folder and matchs the web.xml
		// base path
		for (int i = 0; i < refComponents.length; i++) {
			if (refComponents[i].getRuntimePath().equals(WEBLIB))
				result.add(refComponents[i]);
		}

		return (IVirtualReference[]) result.toArray(new IVirtualReference[result.size()]);
	}
	
	/**
	 * This method will return the list of dependent modules which are utility jars in the web lib
	 * folder of the deployed path of the module. It will not return null.
	 * 
	 * @return array of the web library dependent modules
	 */
	public static IVirtualReference[] getLibModules(IProject webProject) {
		IVirtualComponent webComponent = ComponentCore.createComponent(webProject);
		return getLibModules(webComponent);
	}

	/**
	 * Determines whether the specified object is an accessible web resource.
	 * 
	 * <p>
	 * An accessible web object is a file or directory that can be accessed
	 * through an URI after deploying on an application server.
	 * </p>
	 * 
	 * <p>
	 * This includes all files and directories that are under the WebContent
	 * root directory of a Dynamic Web Project and are not under the WEB-INF and
	 * META-INF folders.
	 * </p>
	 * 
	 * @param object
	 *            the object to test
	 * @return <code>true</code> if accessible web object, <code>false</code> -
	 *         otherwise.
	 */
	public static boolean isWebResource(Object object) {
		if (object instanceof IResource) {
			IResource resource = (IResource) object;
			IVirtualComponent component = ComponentCore.createComponent(resource.getProject());
			if (component != null && JavaEEProjectUtilities.isDynamicWebComponent(component)) {
				IPath rootPath = component.getRootFolder().getWorkspaceRelativePath();
				IPath webInfPath = rootPath.append(J2EEConstants.WEB_INF);
				IPath metaInfPath = rootPath.append(J2EEConstants.META_INF);
				IPath resourcePath = resource.getFullPath();
				return rootPath.isPrefixOf(resourcePath) && 
						!rootPath.equals(resourcePath) &&
						!webInfPath.isPrefixOf(resourcePath) && 
						!metaInfPath.isPrefixOf(resourcePath);
			}
		}
		return false;
	}

	/**
	 * This method will set the context root on the associated workbench module with the given string
	 * value passed in.  This context root is used by the server at runtime.
	 * 
	 * @param contextRoot string
	 */
	public static void setServerContextRoot(IProject webProject, String contextRoot)
	{
		ComponentUtilities.setServerContextRoot(webProject, contextRoot);
	}
}
