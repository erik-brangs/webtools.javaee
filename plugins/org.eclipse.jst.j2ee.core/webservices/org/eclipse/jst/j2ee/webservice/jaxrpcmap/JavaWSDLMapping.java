/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.webservice.jaxrpcmap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jst.j2ee.webservice.internal.jaxrpcmap.JaxrpcmapPackage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Java WSDL Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JavaWSDLMapping#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JavaWSDLMapping#getPackageMappings <em>Package Mappings</em>}</li>
 *   <li>{@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JavaWSDLMapping#getJavaXMLTypeMappings <em>Java XML Type Mappings</em>}</li>
 *   <li>{@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JavaWSDLMapping#getExceptionMappings <em>Exception Mappings</em>}</li>
 *   <li>{@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JavaWSDLMapping#getInterfaceMappings <em>Interface Mappings</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JaxrpcmapPackage#getJavaWSDLMapping()
 * @model 
 * @generated
 */
public interface JavaWSDLMapping extends EObject{
  /**
   * Returns the value of the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Id</em>' attribute.
   * @see #setId(String)
   * @see org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JaxrpcmapPackage#getJavaWSDLMapping_Id()
   * @model 
   * @generated
   */
  String getId();

  /**
   * Sets the value of the '{@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JavaWSDLMapping#getId <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Id</em>' attribute.
   * @see #getId()
   * @generated
   */
  void setId(String value);

  /**
   * Returns the value of the '<em><b>Package Mappings</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.PackageMapping}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Package Mappings</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Package Mappings</em>' containment reference list.
   * @see org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JaxrpcmapPackage#getJavaWSDLMapping_PackageMappings()
   * @model type="org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.PackageMapping" containment="true" required="true"
   * @generated
   */
  EList getPackageMappings();

  /**
   * Returns the value of the '<em><b>Java XML Type Mappings</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JavaXMLTypeMapping}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Java XML Type Mappings</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Java XML Type Mappings</em>' containment reference list.
   * @see org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JaxrpcmapPackage#getJavaWSDLMapping_JavaXMLTypeMappings()
   * @model type="org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JavaXMLTypeMapping" containment="true"
   * @generated
   */
  EList getJavaXMLTypeMappings();

  /**
   * Returns the value of the '<em><b>Exception Mappings</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.ExceptionMapping}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Exception Mappings</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exception Mappings</em>' containment reference list.
   * @see org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JaxrpcmapPackage#getJavaWSDLMapping_ExceptionMappings()
   * @model type="org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.ExceptionMapping" containment="true"
   * @generated
   */
  EList getExceptionMappings();

  /**
   * Returns the value of the '<em><b>Interface Mappings</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.InterfaceMapping}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Interface Mappings</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Interface Mappings</em>' containment reference list.
   * @see org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.JaxrpcmapPackage#getJavaWSDLMapping_InterfaceMappings()
   * @model type="org.eclipse.jst.j2ee.internal.webservice.jaxrpcmap.InterfaceMapping" containment="true"
   * @generated
   */
  EList getInterfaceMappings();

} // JavaWSDLMapping
