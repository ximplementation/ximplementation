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
	 * Get the {@code ImplementInfo} for a specified <i>implementee</i> method.
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

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [implementee=" + implementee
				+ ", implementInfos=" + Arrays.toString(implementInfos) + "]";
	}
}
