/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.internal.web.validation;

import java.util.Hashtable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.wst.common.componentcore.StructureEdit;


/**
 * Insert the type's description here. Creation date: (10/2/2001 7:06:43 PM)
 * 
 * @author: Administrator
 */
public class UIWarHelper extends WarHelper {

	Hashtable aWarFileMap = new Hashtable();

	/**
	 * UIWarHelper constructor comment.
	 */
	public UIWarHelper() {
		super();
	}

	/**
	 * 
	 * This is a war specific helper, get file here always mean get web.xml .
	 * 
	 * @param object
	 *            org.omg.CORBA.Object
	 */
	public IFile getFile(Object object) {

		IFile warFile;
		IProject project = getProject();

		warFile = (IFile) aWarFileMap.get(project.toString());
		if (warFile != null)
			return warFile;
		
		IPath path = getDeploymentDescriptorPath(project);
		IPath projectPath = project.getFullPath();
		path = path.removeFirstSegments(path.matchingFirstSegments(projectPath)); // make it relative
		warFile = project.getFile(path);
		aWarFileMap.put(project.toString(), warFile);
		
		return warFile;
	}
	
	protected IPath getDeploymentDescriptorPath(IProject project) {
		WebArtifactEdit webArtifactEdit = null; 
		try {
			webArtifactEdit = (WebArtifactEdit)StructureEdit.getFirstArtifactEditForRead(project);
			if (webArtifactEdit != null)
				return webArtifactEdit.getDeploymentDescriptorPath();
		} finally {
			if (webArtifactEdit!=null)
				webArtifactEdit.dispose();
		}
		return null;
	}
}