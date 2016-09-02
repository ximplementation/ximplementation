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
 * Implement information for an <i>implementee</i>.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
 *
 */
public class Implementation implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** implementee */
	private Class<?> implementee;

	/** implement info */
	private ImplementInfo[] implementInfos;

	public Implementation()
	{
		super();
	}

	public Implementation(Class<?> implementee, ImplementInfo[] implementInfos)
	{
		super();
		this.implementee = implementee;
		this.implementInfos = implementInfos;
	}

	public Class<?> getImplementee()
	{
		return implementee;
	}

	public void setImplementee(Class<?> implementee)
	{
		this.implementee = implementee;
	}

	public ImplementInfo[] getImplementInfos()
	{
		return implementInfos;
	}

	public void setImplementInfos(ImplementInfo[] implementInfos)
	{
		this.implementInfos = implementInfos;
	}

	/**
	 * Get the {@linkplain ImplementInfo} for a specified <i>implementee</i>
	 * method.
	 * <p>
	 * Returns {@code null} if the <i>implementee</i> method not exists.
	 * </p>
	 * 
	 * @param implementeeMethod
	 * @return
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
