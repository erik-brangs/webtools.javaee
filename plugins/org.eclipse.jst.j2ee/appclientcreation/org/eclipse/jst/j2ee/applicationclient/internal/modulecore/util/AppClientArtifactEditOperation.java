/*******************************************************************************
 * Copyright (c) 2003, 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.applicationclient.internal.modulecore.util;

import org.eclipse.jst.j2ee.applicationclient.componentcore.util.AppClientArtifactEdit;
import org.eclipse.wst.common.componentcore.ArtifactEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.internal.operation.ArtifactEditOperation;

public class AppClientArtifactEditOperation extends ArtifactEditOperation {

    public AppClientArtifactEditOperation(AppClientArtifactEditOperationDataModel operationDataModel) {
        super(operationDataModel);
    }
    protected ArtifactEdit getArtifactEditForModule(WorkbenchComponent module) {
        return AppClientArtifactEdit.getArtifactEditForWrite(module);
    }
    
    protected AppClientArtifactEdit getAppClientArtifactEdit() {
        return (AppClientArtifactEdit)getArtifactEdit();
    }
}
