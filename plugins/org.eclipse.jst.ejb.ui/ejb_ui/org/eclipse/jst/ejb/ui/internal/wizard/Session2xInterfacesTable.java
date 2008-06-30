/*******************************************************************************
 * Copyright (c) 2007, 2008 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Kaloyan Raev, kaloyan.raev@sap.com - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ejb.ui.internal.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.dialogs.OpenTypeSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ejb.ui.internal.util.EJBUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class Session2xInterfacesTable extends Composite {

	private static final String _TABLEITEM = "_TABLEITEM";

	private Table table;
	private TableViewer viewer;
	private IDataModel model;
	private final static String ABBREVIATION_COLUMN = "abbreviation";
	private final static String CLASS_NAME_COLUMN = "className";
	
	private ArrayList tableValues = new ArrayList();
	
	// Set column names
	private String[] columnNames = { ABBREVIATION_COLUMN, CLASS_NAME_COLUMN };
	
	protected class IntfTableContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return new Object[0];
		}

		public void dispose() {
			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}
		
	}
	
	protected class IntfTableLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Session2xInterfacesTableRow) {
				Session2xInterfacesTableRow row = (Session2xInterfacesTableRow) element;
				if (columnIndex == 0) {
					return row.getAbbreviation();
				} else if(columnIndex == 1){
					return row.getClassName();
				}
			}
			return null;
		}
		
	}	
	

	public Session2xInterfacesTable(Composite parent, String[] columnTitles, IDataModel model, Session2xInterfacesTableRow[] tableRows) {
		super(parent, SWT.NONE);
		this.model = model;
		for (int k = 0; k < tableRows.length; k++) {
			tableValues.add(tableRows[k]);
		}
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 4;
		layout.marginWidth = 0;
		this.setLayout(layout);
		
		createTable();
		createTableViewer();
		
		final OpenTypeSelectionDialog dialog = new OpenTypeSelectionDialog(parent.getShell(), false, null, SearchEngine.createWorkspaceScope(), IJavaSearchConstants.INTERFACE);
		
		setInput(tableValues);
		TableItem[] items = getTable().getItems();
		for(int i = 0; i < items.length; i++) {
            final TableItem item = items[i];
            TableEditor editor = new TableEditor(getTable());
            Button button = new Button(getTable(), SWT.FLAT);
            button.setText("...");
            button.pack();
            
            button.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}

				public void widgetSelected(SelectionEvent e) {					
					if (dialog.open() != IDialogConstants.OK_ID)
						return;

					Object[] types= dialog.getResult();
					if (types != null && types.length == 1) {
						IType type= (IType) types[0];
						Session2xInterfacesTableRow row = (Session2xInterfacesTableRow) item.getData();
						row.setClassName(type.getFullyQualifiedName());
						getTableViewer().update(row, null);
					}
				}
            	
            });
            editor.minimumWidth = 24;
            editor.grabHorizontal = true;
            editor.horizontalAlignment = SWT.LEFT;
            editor.setEditor(button, item, 2);
            GridData data = new GridData(GridData.FILL_BOTH);
    		data.heightHint = getTable().getItemHeight() * 3;
    		this.setLayoutData(data);
    		
    		GridData gridData1 = new GridData();
    		gridData1.verticalAlignment = GridData.FILL;
    		gridData1.grabExcessVerticalSpace = true;
    		gridData1.horizontalAlignment = GridData.FILL;
    		gridData1.grabExcessHorizontalSpace = true;
    		getTable().setLayoutData(gridData1);
		}
		
		setTableTooltips(table);
	}

	public void setInput(List input) {		
		viewer.setInput(input);
	}

	public TableViewer getTableViewer() {
		return viewer;
	}
	
	/**
	 * Create the Table
	 */
	private void createTable() {
		int style = SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION;

		setTable(new Table(this, style));
		GridData gridData = new GridData();
		//gridData.grabExcessVerticalSpace = true;
		//gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
//		gridData.horizontalAlignment = GridData.FILL;
		getTable().setLayoutData(gridData);		
					
		getTable().setLinesVisible(true);
		getTable().setHeaderVisible(false);
		
		// 1st column abbreviation
		TableColumn column = new TableColumn(getTable(), SWT.CENTER, 0);		
		column.setWidth(25);
		
		// 2nd column className
		column = new TableColumn(getTable(), SWT.LEFT, 1);
		column.setWidth(100);

		column = new TableColumn(getTable(), SWT.LEFT, 2);
		column.setWidth(25);
		
		this.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				Rectangle area = table.getParent().getClientArea();
				Point preferredSize = viewer.getTable().computeSize(
						SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * table.getBorderWidth() * 2;
				if (preferredSize.y > area.height + table.getHeaderHeight()) {
					// Subtract the scrollbar width from the total column width
					// if a vertical scrollbar will be required
					Point vBarSize = table.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				int delta = area.width - preferredSize.x;
				TableColumn column2 = table.getColumn(1);
				int tmp = column2.getWidth() + delta;
				column2.setWidth(tmp);
				Point size = table.getSize();
			}

		});


	}

	/**
	 * Create the TableViewer 
	 */
	private void createTableViewer() {

		viewer = new TableViewer(getTable()) {

			@Override
			public void update(Object element, String[] properties) {				
				super.update(element, properties);
				Session2xInterfacesTableRow row = (Session2xInterfacesTableRow) element;
				model.setStringProperty(row.getPropertyName(), row.getClassName());
			}
			
		};
		viewer.setUseHashlookup(true);
		viewer.setColumnProperties(columnNames);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.length];

		TextCellEditor textEditor = new TextCellEditor(getTable());
		((Text) textEditor.getControl()).setTextLimit(60);
		// Column 1 : Completed (Checkbox)
		editors[0] = null;

		// Column 2 : Description (Free text)
		
		editors[1] = textEditor;

		// Column 3 : Owner (Combo Box) 
		
		
		// Assign the cell editors to the viewer 
		viewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		viewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				if(property.equals(CLASS_NAME_COLUMN)) {
					return true;
				}
				return false;
			}

			public Object getValue(Object element, String property) {
				Session2xInterfacesTableRow row = (Session2xInterfacesTableRow) element;
				if(property.equals(CLASS_NAME_COLUMN)) {					
					return row.getClassName();
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem) element;
				Session2xInterfacesTableRow row = (Session2xInterfacesTableRow) item.getData();
				if(property.equals(CLASS_NAME_COLUMN)) {
					row.setClassName((String) value);
				}
				viewer.update(row, null);
			}
			
		});
		viewer.setContentProvider(new IntfTableContentProvider());
		viewer.setLabelProvider(new IntfTableLabelProvider());
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Table getTable() {
		return table;
	}
	

	
	/**
	 * <p>This method has been derived from example 
	 * <a href="http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet125.java?view=co">Snippet 125</a></p>
	 * @param table
	 */
	protected void setTableTooltips(final Table table) {
		// Disable native tooltip
		table.setToolTipText("");
		
		// Implement a "fake" tooltip
		final Listener labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
					case SWT.MouseDown:
						Event e = new Event();
						e.item = (TableItem) label.getData(_TABLEITEM);
						// Assuming table is single select, set the selection as if
						// the mouse down event went through to the table
						table.setSelection(new TableItem[] { (TableItem) e.item });
						table.notifyListeners(SWT.Selection, e);
						shell.dispose();
						table.setFocus();
						break;
					case SWT.MouseExit:
						shell.dispose();
						break;
				}
			}
		};
		
		Listener tableListener = new Listener() {
			Shell tip = null;
			Label label = null;
			public void handleEvent(Event event) {
				switch (event.type) {
					case SWT.Dispose:
					case SWT.KeyDown:
					case SWT.MouseMove: {
						if (tip == null) break;
						tip.dispose();
						tip = null;
						label = null;
						break;
					}
					case SWT.MouseHover: {
						TableItem item = table.getItem(new Point(event.x, event.y));
						if (item != null) {
							if (tip != null  && !tip.isDisposed()) tip.dispose();
							Display display = event.widget.getDisplay();
							Shell shell = ((Table) event.widget).getShell();
							tip = new Shell(shell, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
							tip.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
							FillLayout layout = new FillLayout();
							layout.marginWidth = 2;
							tip.setLayout(layout);
							label = new Label(tip, SWT.NONE);
							label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
							label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
							label.setData(_TABLEITEM, item);
							label.setText(getTooltipForTableItem(item.getText()));
							label.addListener(SWT.MouseExit, labelListener);
							label.addListener(SWT.MouseDown, labelListener);
							Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
							Rectangle rect = item.getBounds(0);
							Point pt = table.toDisplay(rect.x, rect.y);
							tip.setBounds(pt.x, pt.y, size.x, size.y);
							tip.setVisible(true);
						}
					}
				}
			}
		};
		
		table.addListener(SWT.Dispose, tableListener);
		table.addListener(SWT.KeyDown, tableListener);
		table.addListener(SWT.MouseMove, tableListener);
		table.addListener(SWT.MouseHover, tableListener);
	}
	
	protected String getTooltipForTableItem(String tableItemText) {
		if (EJBUIMessages.LOCAL_COMPONENT_INTERFACE_CODE.equals(tableItemText)) {
			return EJBUIMessages.LOCAL_COMPONENT_INTERFACE_TOOLTIP;
		} else if (EJBUIMessages.LOCAL_HOME_INTERFACE_CODE.equals(tableItemText)) {
			return EJBUIMessages.LOCAL_HOME_INTERFACE_TOOLTIP;
		} else if (EJBUIMessages.REMOTE_COMPONENT_INTERFACE_CODE.equals(tableItemText)) {
			return EJBUIMessages.REMOTE_COMPONENT_INTERFACE_TOOLTIP;
		} else if (EJBUIMessages.REMOTE_HOME_INTERFACE_CODE.equals(tableItemText)) {
			return EJBUIMessages.REMOTE_HOME_INTERFACE_TOOLTIP;
		} 
		
		return "";
	}
	
}
