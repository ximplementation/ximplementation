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
 * The invocation info of an <i>implementee</i> method.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-6
 *
 */
public class ImplementeeMethodInvocationInfo
{
	private ImplementMethodInfo implementMethodInfo;

	private Object implementorBean;

	public ImplementeeMethodInvocationInfo()
	{
		super();
	}

	public ImplementeeMethodInvocationInfo(ImplementMethodInfo implementMethodInfo, Object implementorBean)
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