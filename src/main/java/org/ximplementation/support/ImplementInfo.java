/**
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *  
  * 	http://www.apache.org/licenses/LICENSE-2.0
  *  
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License. 
  */

package org.ximplementation.support;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Implement info of an <i>implementee method</i>.
 * <p>
 * It contains an array of {@linkplain ImplementMethodInfo}s, which each element
 * describes the <i>implement method</i> info of the <i>implementee method</i>.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
 * @see ImplementMethodInfo
 */
public class ImplementInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** implementee method */
	private Method implementeeMethod;

	private ImplementMethodInfo[] implementMethodInfos;

	public ImplementInfo()
	{
		super();
	}

	/**
	 * Create an {@code ImplementInfo} instance.
	 * 
	 * @param implementeeMethod
	 *            An <i>implementee method</i>.
	 */
	public ImplementInfo(Method implementeeMethod)
	{
		super();
		this.implementeeMethod = implementeeMethod;
	}

	/**
	 * Create an {@code ImplementInfo} instance.
	 * 
	 * @param implementeeMethod
	 *            An <i>implementee method</i>.
	 * @param implementMethodInfos
	 *            The {@code ImplementMethodInfo}s about the <i>implementee
	 *            method</i>.
	 */
	public ImplementInfo(Method implementeeMethod, ImplementMethodInfo[] implementMethodInfos)
	{
		super();
		this.implementeeMethod = implementeeMethod;
		this.implementMethodInfos = implementMethodInfos;
	}

	/**
	 * Get the <i>implementee method</i>.
	 * 
	 * @return
	 */
	public Method getImplementeeMethod()
	{
		return implementeeMethod;
	}

	/**
	 * Set the <i>implementee method</i>.
	 * 
	 * @param implementeeMethod
	 */
	public void setImplementeeMethod(Method implementeeMethod)
	{
		this.implementeeMethod = implementeeMethod;
	}

	/**
	 * Get the {@code ImplementMethodInfo}s.
	 * 
	 * @return
	 */
	public ImplementMethodInfo[] getImplementMethodInfos()
	{
		return implementMethodInfos;
	}

	/**
	 * Set the {@code ImplementMethodInfo}s.
	 * 
	 * @param implementMethodInfos
	 */
	public void setImplementMethodInfos(ImplementMethodInfo[] implementMethodInfos)
	{
		this.implementMethodInfos = implementMethodInfos;
	}

	/**
	 * Return if it has implement method info.
	 * 
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean hasImplementMethodInfo()
	{
		return (this.implementMethodInfos != null
				&& this.implementMethodInfos.length > 0);
	}

	/**
	 * Get the {@code ImplementMethodInfo} for a specified <i>implement
	 * method</i>.
	 * 
	 * @param implementMethod
	 *            The <i>implement method</i>.
	 * @return The {@code ImplementMethodInfo} about the <i>implement method</i>
	 *         , {@code null} if no.
	 */
	public ImplementMethodInfo getImplementMethodInfo(Method implementMethod)
	{
		if (this.implementMethodInfos == null)
			return null;

		for (ImplementMethodInfo implementMethodInfo : this.implementMethodInfos)
		{
			if (implementMethodInfo.getImplementMethod()
					.equals(implementMethod))
				return implementMethodInfo;
		}

		return null;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [implementeeMethod=" + implementeeMethod + ", implementMethodInfos="
				+ implementMethodInfos + "]";
	}
}
