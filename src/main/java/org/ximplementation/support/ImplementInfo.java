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
 * 接口方法实现信息。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月5日
 *
 */
public class ImplementInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 接口方法 */
	private Method implementeeMethod;

	private ImplementMethodInfo[] implementMethodInfos;

	public ImplementInfo()
	{
		super();
	}

	public ImplementInfo(Method implementeeMethod)
	{
		super();
		this.implementeeMethod = implementeeMethod;
	}

	public ImplementInfo(Method implementeeMethod, ImplementMethodInfo[] implementMethodInfos)
	{
		super();
		this.implementeeMethod = implementeeMethod;
		this.implementMethodInfos = implementMethodInfos;
	}

	public Method getImplementeeMethod()
	{
		return implementeeMethod;
	}

	public void setImplementeeMethod(Method implementeeMethod)
	{
		this.implementeeMethod = implementeeMethod;
	}

	public ImplementMethodInfo[] getImplementMethodInfos()
	{
		return implementMethodInfos;
	}

	public void setImplementMethodInfos(ImplementMethodInfo[] implementMethodInfos)
	{
		this.implementMethodInfos = implementMethodInfos;
	}

	/**
	 * 是否有实现方法信息。
	 * 
	 * @return
	 */
	public boolean hasImplementMethodInfo()
	{
		return (this.implementMethodInfos != null
				&& this.implementMethodInfos.length > 0);
	}

	/**
	 * 获取指定实现方法的实现方法信息。
	 * <p>
	 * 如果没有实现方法信息，此方法将返回{@code null}。
	 * </p>
	 * 
	 * @param implementMethod
	 * @return
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
