/*
 * Created on Jan 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.wtp.j2ee.headless.tests.j2ee.verifiers;

import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.tests.DataModelVerifier;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class J2EEExportDataModelVerifier extends DataModelVerifier {
	
	/* (non-Javadoc)
	 * @see org.eclipse.wtp.j2ee.headless.tests.j2ee.verifiers.DataModelVerifier#verify(org.eclipse.wtp.common.operation.WTPOperationDataModel)
	 */
	public void verify(IDataModel model) throws Exception {
		super.verify(model);
		
//		String archiveDestination = model.getStringProperty(J2EEModuleExportDataModel.ARCHIVE_DESTINATION);
//		File archive = new File(archiveDestination);
//		Assert.assertTrue("The exported archive must exist.", archive.exists());
	}

}
