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

/**
 * 接口方法调用信息。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月6日
 *
 */
public class InterfaceMethodInvocationInfo
{
	/** 实现方法信息 */
	private ImplementMethodInfo implementMethodInfo;

	/** 实现Bean */
	private Object implementorBean;

	public InterfaceMethodInvocationInfo()
	{
		super();
	}

	public InterfaceMethodInvocationInfo(ImplementMethodInfo implementMethodInfo, Object implementorBean)
	{
		super();
		this.implementMethodInfo = implementMethodInfo;
		this.implementorBean = implementorBean;
	}

	public ImplementMethodInfo getImplementMethodInfo()
	{
		return implementMethodInfo;
	}

	public void setImplementMethodInfo(ImplementMethodInfo implementMethodInfo)
	{
		this.implementMethodInfo = implementMethodInfo;
	}

	public Object getImplementorBean()
	{
		return implementorBean;
	}

	public void setImplementorBean(Object implementorBean)
	{
		this.implementorBean = implementorBean;
	}
}