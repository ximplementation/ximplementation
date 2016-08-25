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
 * 接口实现信息。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月5日
 *
 */
public class Implementation implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 接口 */
	private Class<?> implementee;

	/** 实现信息 */
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
	 * 获取指定接口方法的实现信息。
	 * <p>
	 * 如果没有实现信息，此方法将返回{@code null}。
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
