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
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation info for an <i>implementee</i>.
 * <p>
 * It contains an array of {@linkplain ImplementInfo}s, which each element
 * contains an <i>implementee method</i> and all of its <i>implement method</i>
 * s.
 * </p>
 * <p>
 * Instances of this class can be created by {@linkplain ImplementationResolver}
 * .
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
 * @see ImplementInfo
 * @see ImplementationResolver
 */
public class Implementation<T> implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** implementee */
	private Class<T> implementee;

	/** implement info */
	private ImplementInfo[] implementInfos;

	public Implementation()
	{
		super();
	}

	public Implementation(Class<T> implementee, ImplementInfo[] implementInfos)
	{
		super();
		this.implementee = implementee;
		this.implementInfos = implementInfos;
	}

	/**
	 * Get the <i>implementee</i>.
	 * 
	 * @return
	 */
	public Class<T> getImplementee()
	{
		return implementee;
	}

	/**
	 * Set the <i>implementee</i>.
	 * 
	 * @param implementee
	 */
	public void setImplementee(Class<T> implementee)
	{
		this.implementee = implementee;
	}

	/**
	 * Get the {@code ImplementInfo}s about the <i>implementee</i>.
	 * 
	 * @return
	 */
	public ImplementInfo[] getImplementInfos()
	{
		return implementInfos;
	}

	/**
	 * Set the {@code ImplementInfo}s about the <i>implementee</i>.
	 * 
	 * @param implementInfos
	 */
	public void setImplementInfos(ImplementInfo[] implementInfos)
	{
		this.implementInfos = implementInfos;
	}

	/**
	 * Get the {@code ImplementInfo} for given <i>implementee</i> method.
	 * 
	 * @param implementeeMethod
	 *            The <i>implementee method</i> int the <i>implementee</i>.
	 * @return The {@code ImplementInfo} for the <i>implementee method</i>,
	 *         {@code null} if no.
	 */
	public ImplementInfo getImplementInfo(Method implementeeMethod)
	{
		if (this.implementInfos == null)
			return null;

		for (ImplementInfo implementInfo : this.implementInfos)
		{
			if (implementInfo.getImplementeeMethod().equals(implementeeMethod))
				return implementInfo;
		}

		return null;
	}

	/**
	 * Get all <i>implementor</i>s of the <i>implementee</i>.
	 * 
	 * @return An <i>implementor</i> set, empty if no <i>implementor</i>s.
	 */
	public Set<Class<?>> getImplementors()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();

		if (this.implementInfos == null)
			return implementors;

		for (ImplementInfo implementInfo : this.implementInfos)
		{
			if (!implementInfo.hasImplementMethodInfo())
				continue;

			for (ImplementMethodInfo implementMethodInfo : implementInfo
					.getImplementMethodInfos())
			{
				Class<?> implementor = implementMethodInfo.getImplementor();

				implementors.add(implementor);
			}
		}

		return implementors;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((implementee == null) ? 0 : implementee.hashCode());
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
		Implementation<?> other = (Implementation<?>) obj;
		if (!Arrays.equals(implementInfos, other.implementInfos))
			return false;
		if (implementee == null)
		{
			if (other.implementee != null)
				return false;
		}
		else if (!implementee.equals(other.implementee))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [implementee=" + implementee
				+ ", implementInfos=" + Arrays.toString(implementInfos) + "]";
	}
}
