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
import java.util.Arrays;

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
	 * Get the {@code ImplementMethodInfo} for given <i>implementor</i> and
	 * <i>implement method</i>.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementMethod
	 *            The <i>implement method</i>.
	 * @return The {@code ImplementMethodInfo}, {@code null} if none.
	 */
	public ImplementMethodInfo getImplementMethodInfo(Class<?> implementor,
			Method implementMethod)
	{
		if (this.implementMethodInfos == null)
			return null;

		for (ImplementMethodInfo implementMethodInfo : this.implementMethodInfos)
		{
			if (implementMethodInfo.getImplementMethod()
					.equals(implementMethod)
					&& implementMethodInfo.getImplementor().equals(implementor))
				return implementMethodInfo;
		}

		return null;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(implementMethodInfos);
		result = prime * result + ((implementeeMethod == null) ? 0
				: implementeeMethod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImplementInfo other = (ImplementInfo) obj;
		if (!Arrays.equals(implementMethodInfos, other.implementMethodInfos))
			return false;
		if (implementeeMethod == null)
		{
			if (other.implementeeMethod != null)
				return false;
		}
		else if (!implementeeMethod.equals(other.implementeeMethod))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [implementeeMethod=" + implementeeMethod + ", implementMethodInfos="
				+ Arrays.toString(implementMethodInfos) + "]";
	}
}
