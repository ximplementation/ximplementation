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
	private Method interfaceMethod;

	private ImplementMethodInfo[] implementMethodInfos;

	public ImplementInfo()
	{
		super();
	}

	public ImplementInfo(Method interfaceMethod)
	{
		super();
		this.interfaceMethod = interfaceMethod;
	}

	public ImplementInfo(Method interfaceMethod, ImplementMethodInfo[] implementMethodInfos)
	{
		super();
		this.interfaceMethod = interfaceMethod;
		this.implementMethodInfos = implementMethodInfos;
	}

	public Method getInterfaceMethod()
	{
		return interfaceMethod;
	}

	public void setInterfaceMethod(Method interfaceMethod)
	{
		this.interfaceMethod = interfaceMethod;
	}

	public ImplementMethodInfo[] getImplementMethodInfos()
	{
		return implementMethodInfos;
	}

	public void setImplementMethodInfos(ImplementMethodInfo[] implementMethodInfos)
	{
		this.implementMethodInfos = implementMethodInfos;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [interfaceMethod=" + interfaceMethod + ", implementMethodInfos="
				+ implementMethodInfos + "]";
	}
}
