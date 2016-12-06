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

import java.lang.reflect.Method;

/**
 * Simple {@linkplain ImplementeeMethodInvocation}.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 *
 */
public class SimpleImplementeeMethodInvocation
		implements ImplementeeMethodInvocation
{
	/** The Implementation */
	private Implementation<?> implementation;

	/** The implement info */
	private ImplementInfo implementInfo;

	/** The implementee method parameters */
	private Object[] implementeeMethodParams;

	/** The ImplementMethodInfo to be invoked */
	private ImplementMethodInfo implementMethodInfo;

	/** The implementor bean */
	private Object implementorBean;

	/**
	 * Create a new empty instance.
	 */
	public SimpleImplementeeMethodInvocation()
	{
		super();
	}

	/**
	 * Create a new instance.
	 * 
	 * @param implementation
	 *            The {@linkplain Implementation} in this invocation.
	 * @param implementInfo
	 *            The {@linkplain ImplementInfo} in this invocation.
	 * @param implementeeMethodParams
	 *            The <i>implementee method</i> parameters in this invocation.
	 * @param implementMethodInfo
	 *            The {@linkplain ImplementMethodInfo} to be invoked in this
	 *            invocation.
	 * @param implementorBean
	 *            The <i>implementor</i> bean to be invoked in this invocation.
	 */
	public SimpleImplementeeMethodInvocation(Implementation<?> implementation,
			ImplementInfo implementInfo,
			Object[] implementeeMethodParams,
			ImplementMethodInfo implementMethodInfo, Object implementorBean)
	{
		super();
		this.implementeeMethodParams = implementeeMethodParams;
		this.implementMethodInfo = implementMethodInfo;
		this.implementorBean = implementorBean;
	}

	/**
	 * Get the {@linkplain Implementation} in this invocation.
	 * 
	 * @return
	 */
	public Implementation<?> getImplementation()
	{
		return implementation;
	}

	/**
	 * Set the {@linkplain Implementation} in this invocation.
	 * 
	 * @param implementation
	 */
	public void setImplementation(Implementation<?> implementation)
	{
		this.implementation = implementation;
	}

	/**
	 * Get the {@linkplain ImplementInfo} in this invocation.
	 * 
	 * @return
	 */
	public ImplementInfo getImplementInfo()
	{
		return implementInfo;
	}

	/**
	 * Set the {@linkplain ImplementInfo} in this invocation.
	 * 
	 * @param implementInfo
	 */
	public void setImplementInfo(ImplementInfo implementInfo)
	{
		this.implementInfo = implementInfo;
	}

	/**
	 * Get the <i>implementee method</i> parameters in this invocation.
	 * 
	 * @return
	 */
	public Object[] getImplementeeMethodParams()
	{
		return implementeeMethodParams;
	}

	/**
	 * Set the <i>implementee method</i> parameters in this invocation.
	 * 
	 * @param implementeeMethodParams
	 */
	public void setImplementeeMethodParams(Object[] implementeeMethodParams)
	{
		this.implementeeMethodParams = implementeeMethodParams;
	}

	/**
	 * Get the {@linkplain ImplementMethodInfo} to be invoked in this
	 * invocation.
	 * 
	 * @return
	 */
	public ImplementMethodInfo getImplementMethodInfo()
	{
		return implementMethodInfo;
	}

	/**
	 * Set the {@linkplain ImplementMethodInfo} to be invoked in this
	 * invocation.
	 * 
	 * @param implementMethodInfo
	 */
	public void setImplementMethodInfo(ImplementMethodInfo implementMethodInfo)
	{
		this.implementMethodInfo = implementMethodInfo;
	}

	/**
	 * Get the <i>implementor</i> bean to be invoked in this invocation.
	 * 
	 * @return
	 */
	public Object getImplementorBean()
	{
		return implementorBean;
	}

	/**
	 * Set the <i>implementor</i> bean to be invoked in this invocation.
	 * 
	 * @param implementorBean
	 */
	public void setImplementorBean(Object implementorBean)
	{
		this.implementorBean = implementorBean;
	}

	@Override
	public Object invoke() throws Throwable
	{
		Object[] implementMethodParams = this.implementMethodInfo
				.getParams(this.implementeeMethodParams);

		Method implementMethod = this.implementMethodInfo.getImplementMethod();

		if (!implementMethod.isAccessible())
			implementMethod.setAccessible(true);

		return implementMethod.invoke(this.implementorBean,
				implementMethodParams);
	}
}
