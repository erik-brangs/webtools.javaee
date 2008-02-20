/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *	   David Schneider, david.schneider@unisys.com - [142500] WTP properties pages fonts don't follow Eclipse preferences
 *******************************************************************************/
package org.eclipse.jst.j2ee.internal;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.BuildPathDialogAccess;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jst.j2ee.application.internal.operations.ClasspathElement;
import org.eclipse.jst.j2ee.internal.common.ClasspathModel;
import org.eclipse.jst.j2ee.internal.common.ClasspathModelListener;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.impl.ModuleURIUtil;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.frameworks.internal.ui.WorkspaceModifyComposedOperation;

public class WebLibDependencyPropertiesPage extends JARDependencyPropertiesPage implements IClasspathTableOwner, Listener, ClasspathModelListener {

	public WebLibDependencyPropertiesPage(final IProject project, final J2EEDependenciesPage page) {
		super(project, page);
	}

	protected ClasspathModel createClasspathModel() {
		return new ClasspathModel(null, true);
	}
	
	public Composite createContents(Composite parent) {
		initialize();
		Composite composite = createBasicComposite(parent);
		if (model.getComponent() != null) {
			if (!isValidWebModule())
				return composite;
			J2EEDependenciesPage.createDescriptionComposite(composite, ManifestUIResourceHandler.Web_Libraries_Desc);
			// createProjectLabelsGroup(composite);
			createListGroup(composite);
			handleWLPSupport();
			setEnablement();
		}
	    Dialog.applyDialogFont(parent);
	    postCreateContents();
		return composite;
	}

	protected void createProjectLabelsGroup(Composite parent) {

		Composite labelsGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		labelsGroup.setLayout(layout);
		labelsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(labelsGroup, SWT.NONE);
		label.setText(ManifestUIResourceHandler.Project_name__UI_);

		componentNameText = new Text(labelsGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		componentNameText.setEditable(false);
		componentNameText.setLayoutData(data);
		componentNameText.setText(project.getName());
	}

	protected void createListGroup(Composite parent) {
		Composite listGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		listGroup.setLayout(layout);
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.horizontalIndent = 5;
		listGroup.setLayoutData(gData);

		availableDependentJars = new Label(listGroup, SWT.NONE);
		gData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
		availableDependentJars.setText(ManifestUIResourceHandler.Available_dependent_JARs__UI_);
		availableDependentJars.setLayoutData(gData);
		createTableComposite(listGroup);
	}

	protected void createTableComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gData);
		tableManager = new ClasspathTableManager(this, model, validateEditListener);
		tableManager.setReadOnly(isReadOnly());
		tableManager.fillWLPComposite(composite);
	}

	protected boolean isValidWebModule() {
		if (!J2EEProjectUtilities.isDynamicWebProject(project)) {
			propPage.setErrorMessage(ManifestUIResourceHandler.Web_Lib_Error);
			return false;
		}
		return true;
	}

	protected void setEnablement() {
		if (tableManager.availableJARsViewer.getTable().getItems().length == 0) {
			tableManager.selectAllButton.setEnabled(false);
			tableManager.deselectAllButton.setEnabled(false);
		} else {
			tableManager.selectAllButton.setEnabled(true);
			tableManager.deselectAllButton.setEnabled(true);
		}
	}

	private void handleWLPSupport() {
		availableDependentJars.setText(ManifestUIResourceHandler.WEB_LIB_LIST_DESCRIPTION);
		tableManager.refresh();
	}
	
	/**
	 * Called to refresh the UI when the classpath changes
	 */
	protected void handleClasspathChange() {
		model.resetClassPathSelectionForWLPs();
		super.handleClasspathChange();
	}


	public boolean performOk() {
		if (model.getComponent() == null || !isValidWebModule()) {
			return true;
		}
		if (!isDirty) {
			return true;
		}
		try {
			boolean createdFlexProjects = runWLPOp(createFlexProjectOperations());
			boolean createdComponentDependency = false;
			if (createdFlexProjects) {
				createdComponentDependency = runWLPOp(createComponentDependencyOperations());
				isDirty = false;
			}
			// treat as a classpath change for refresh purposes
			// XXX this refresh is not working - suspect it is because the virtual component dependencies are
			// not consistently being recomputed
			//handleClasspathChange();
			return createdComponentDependency;
		} finally {
			model.dispose();
		}
	}

	private boolean runWLPOp(WorkspaceModifyComposedOperation composed) {
		try {
			if (composed != null)
				new ProgressMonitorDialog(propPage.getShell()).run(true, true, composed);
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
			if (ex.getCause() != null) {
				ex.getCause().printStackTrace();
			}
			String title = ManifestUIResourceHandler.An_internal_error_occurred_ERROR_;
			String msg = title;
			if (ex.getTargetException() != null && ex.getTargetException().getMessage() != null)
				msg = ex.getTargetException().getMessage();
			MessageDialog.openError(propPage.getShell(), title, msg);
			org.eclipse.jem.util.logger.proxy.Logger.getLogger().logError(ex);
			return false;
		} catch (InterruptedException e) {
			// cancelled
			return false;
		}
		return true;
	}

	private void createRef(String aComponentName){
		IVirtualComponent archive = ComponentCore.createArchiveComponent(model.getComponent().getProject(), aComponentName);

		// To do: check if archive component already exists
		IVirtualReference ref = ComponentCore.createReference(model.getComponent(), archive, new Path("/WEB-INF/lib")); //$NON-NLS-1$
		model.getComponent().addReferences(new IVirtualReference [] { ref });

		ClasspathElement element = createClassPathElement(archive, archive.getName());
//		ClassPathSelection selection = createClassPathSelectionForExternalJar(element);
		model.getClassPathSelectionForWLPs().getClasspathElements().add(element);
	}
	
	public void handleSelectExternalJarButton() {
		if (J2EEProjectUtilities.isDynamicWebProject(project)) {
			IPath[] selected = BuildPathDialogAccess.chooseExternalJAREntries(propPage.getShell());
			if (selected != null) {
				String type = VirtualArchiveComponent.LIBARCHIVETYPE + IPath.SEPARATOR;
				for (int i = 0; i < selected.length; i++) {
					createRef(type + selected[i].toString());
				}
				refresh();
			}
		}
	}

	public void handleSelectVariableButton() {
		if (J2EEProjectUtilities.isDynamicWebProject(project)) {
			IPath existingPath[] = new Path[0];
			IPath[] selected = BuildPathDialogAccess.chooseVariableEntries(propPage.getShell(), existingPath);

			if (selected != null) {
				String type = VirtualArchiveComponent.VARARCHIVETYPE + IPath.SEPARATOR;
				for (int i = 0; i < selected.length; i++) {
					IPath resolvedPath = JavaCore.getResolvedVariablePath(selected[i]);
					java.io.File file = new java.io.File(resolvedPath.toOSString());
					if (file.isFile() && file.exists()) {
						createRef(type + selected[i].toString());
					} else {
						// display error
					}
				}
				refresh();
			}
		}
	}

	private ClasspathElement createClassPathElement(IVirtualComponent archiveComp, String unresolvedName) {

		URI uri = URI.createURI(ModuleURIUtil.getHandleString(archiveComp));
		ClasspathElement element = new ClasspathElement(uri);
		element.setValid(false);
		element.setSelected(true);
		element.setRelativeText(unresolvedName);
		element.setText(unresolvedName);
		element.setEarProject(null);
		return element;
	}

//	private ClassPathSelection createClassPathSelectionForExternalJar(ClasspathElement element) {
//		ClassPathSelection selection = new ClassPathSelection();
//		selection.getClasspathElements().add(element);
//		return selection;
//	}

//	private ClassPathSelection createClassPathSelectionForProjectJar(ClasspathElement element) {
//		ClassPathSelection selection = new ClassPathSelection();
//		selection.getClasspathElements().add(element);
//		return selection;
//	}

	public void handleSelectProjectJarButton() {
		if (J2EEProjectUtilities.isDynamicWebProject(project)) {
			IPath[] selected = BuildPathDialogAccess.chooseJAREntries(propPage.getShell(), project.getLocation(), new IPath[0]);
			if (selected != null) {
				String type = VirtualArchiveComponent.LIBARCHIVETYPE + IPath.SEPARATOR;
				for (int i = 0; i < selected.length; i++) {
					createRef(type + selected[i].makeRelative().toString());
				}
				refresh();
			}
		}

	}
}
