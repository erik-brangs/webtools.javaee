package org.eclipse.jst.j2ee.ejb.internal.operations;

public class LocalComponentInterfaceTemplate
{
  protected static String nl;
  public static synchronized LocalComponentInterfaceTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    LocalComponentInterfaceTemplate result = new LocalComponentInterfaceTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl; //$NON-NLS-1$
  protected final String TEXT_1 = "package "; //$NON-NLS-1$
  protected final String TEXT_2 = ";"; //$NON-NLS-1$
  protected final String TEXT_3 = NL + NL + "import javax.ejb.EJBLocalObject;" + NL + "" + NL + "public interface "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  protected final String TEXT_4 = " extends EJBLocalObject {" + NL + "" + NL + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  protected final String TEXT_5 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     CreateSessionBeanTemplateModel model = (CreateSessionBeanTemplateModel) argument; 
    
	if (model.getJavaPackageName() != null && model.getJavaPackageName().length() > 0) {

    stringBuffer.append(TEXT_1);
    stringBuffer.append( model.getJavaPackageName() );
    stringBuffer.append(TEXT_2);
    
	}

    stringBuffer.append(TEXT_3);
    stringBuffer.append( model.getLocalComponentClassSimpleName() );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(TEXT_5);
    return stringBuffer.toString();
  }
}
