/*******************************************************************************
 * Copyright (c) 2004 Andrei Loskutov.
 * Copyright (c) 2010 Jens Cornelis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSD License
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/bsd-license.php
 * Contributor:  Andrei Loskutov - initial API and implementation
 *******************************************************************************/

/**
 * Created on 31.12.2002
 */
package de.plugins.eclipse.depclipse.model;
import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

public class TreeLeaf extends TreeObject {
    protected IJavaElement javaElement;

    /**
     * Constructor for TreeLeaf.
     * @param javaElement
     */
    public TreeLeaf(IJavaElement javaElement) {
        super();
        this.javaElement = javaElement;
    }

    public TreeLeaf(IResource iResource) {
        super();
        this.iResource = iResource;
    }

    public String getName() {
        if(javaElement != null){
            return javaElement.getElementName();
        }
        return iResource == null? "" : iResource.getName(); //$NON-NLS-1$
    }

    public boolean isLeaf() {
        return true;
    }

    public IJavaElement getIJavaElement(){
        return javaElement;
    }

    public IResource getIResource(){
        if(javaElement == null){
            return super.getIResource();
        }
        try {
            return javaElement.getCorrespondingResource();
        } catch (JavaModelException e) {
            return super.getIResource();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(! (obj instanceof TreeLeaf)){
            return false;
        }
        TreeLeaf treeObj = (TreeLeaf) obj;

        return getName().equals(treeObj.getName());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getName().hashCode();
    }

    public boolean hasCycle(){
        return false;
    }

    /* (non-Javadoc)
     * @see de.plugins.eclipse.depclipse.views.TreeObject#getPackageName()
     */
    public String getPackageName() {
        if(getIJavaElement() != null){
            return getJavaPackageName(getIJavaElement());
        }
        if(iResource != null){
            String path = iResource.getFullPath().removeFirstSegments(1).toString();
            return path.replace('/', '.');
        }
        return "";  //$NON-NLS-1$
    }

    public String getByteCodePath() throws JavaModelException {
        String filePath = "";  //$NON-NLS-1$
        if(javaElement != null){
            String packagePath = "";  //$NON-NLS-1$
            packagePath = getPackageOutputPath(javaElement);
            StringBuffer sb = new StringBuffer(packagePath);
            sb.append(File.separator).append(
                javaElement.getPath().removeFileExtension().lastSegment()).append(
                ".class"); //$NON-NLS-1$
            filePath = sb.toString();
        } else {
            if(iResource != null){
                filePath = iResource.getLocation().makeAbsolute().toOSString();
            }
        }
        return filePath;
    }


    @SuppressWarnings("rawtypes")
	public Object getAdapter(Class key) {
        if(key == IJavaElement.class){
            return getIJavaElement();
        }
        if(key == IResource.class){
            return getIResource();
        }
        return null;
    }

}