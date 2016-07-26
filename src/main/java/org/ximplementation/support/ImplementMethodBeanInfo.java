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

import java.util.Collection;

/**
 * 实现方法Bean信息。
 * <p>
 * 它封装实现方法与其对应的实现者Bean集合信息。
 * </p>
 *
 * @author earthangry@gmail.com
 * @date 2015年12月6日
 *
 */
public class ImplementMethodBeanInfo
{
	/** 实现方法信息 */
	private ImplementMethodInfo implementMethodInfo;

	/** 实现者Bean集合 */
	private Collection<?> implementorBeans;

	public ImplementMethodBeanInfo()
	{
		super();
	}

	public ImplementMethodBeanInfo(ImplementMethodInfo implementMethodInfo, Collection<?> implementorBeans)
	{
		super();
		this.implementMethodInfo = implementMethodInfo;
		this.implementorBeans = implementorBeans;
	}

	public ImplementMethodInfo getImplementMethodInfo()
	{
		return implementMethodInfo;
	}

	public void setImplementMethodInfo(ImplementMethodInfo implementMethodInfo)
	{
		this.implementMethodInfo = implementMethodInfo;
	}

	public Collection<?> getImplementorBeans()
	{
		return implementorBeans;
	}

	public void setImplementorBeans(Collection<?> implementorBeans)
	{
		this.implementorBeans = implementorBeans;
	}
}
