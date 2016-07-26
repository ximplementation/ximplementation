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
import java.lang.reflect.Type;

/**
 * 实现方法信息。
 * 
 * @author earthangry@gmail.com
 * @date 2015年12月5日
 *
 */
public class ImplementMethodInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 实现者 */
	private Class<?> implementor;

	/** 实现方法 */
	private Method implementMethod;

	/** 实现方法参数类型列表 */
	private Class<?>[] paramTypes;

	/** 实现方法参数泛型列表 */
	private Type[] genericParamTypes;

	/** 实现方法的参数索引数组 */
	private int[] paramIndexes;

	/** 有效性方法 */
	private Method validityMethod;

	/** 有效性方法的参数索引数组 */
	private int[] validityParamIndexes;

	/** 优先级值 */
	private int priorityValue;

	/** 优先级方法 */
	private Method priorityMethod;

	/** 优先级方法的参数索引数组 */
	private int[] priorityParamIndexes;

	public ImplementMethodInfo()
	{
		super();
	}

	public ImplementMethodInfo(Class<?> implementor, Method implementMethod)
	{
		super();
		this.implementor = implementor;
		this.implementMethod = implementMethod;
	}

	public Class<?> getImplementor()
	{
		return implementor;
	}

	public void setImplementor(Class<?> implementor)
	{
		this.implementor = implementor;
	}

	public Method getImplementMethod()
	{
		return implementMethod;
	}

	public void setImplementMethod(Method implementMethod)
	{
		this.implementMethod = implementMethod;
	}

	public Class<?>[] getParamTypes()
	{
		return paramTypes;
	}

	public void setParamTypes(Class<?>[] paramTypes)
	{
		this.paramTypes = paramTypes;
	}

	public Type[] getGenericParamTypes()
	{
		return genericParamTypes;
	}

	public void setGenericParamTypes(Type[] genericParamTypes)
	{
		this.genericParamTypes = genericParamTypes;
	}

	public int[] getParamIndexes()
	{
		return paramIndexes;
	}

	public void setParamIndexes(int[] paramIndexes)
	{
		this.paramIndexes = paramIndexes;
	}

	public boolean hasValidityMethod()
	{
		return (this.validityMethod != null);
	}

	public Method getValidityMethod()
	{
		return validityMethod;
	}

	public void setValidityMethod(Method validityMethod)
	{
		this.validityMethod = validityMethod;
	}

	public int[] getValidityParamIndexes()
	{
		return validityParamIndexes;
	}

	public void setValidityParamIndexes(int[] validityParamIndexes)
	{
		this.validityParamIndexes = validityParamIndexes;
	}

	public int getPriorityValue()
	{
		return priorityValue;
	}

	public void setPriorityValue(int priorityValue)
	{
		this.priorityValue = priorityValue;
	}

	public boolean hasPriorityMethod()
	{
		return (this.priorityMethod != null);
	}

	public Method getPriorityMethod()
	{
		return priorityMethod;
	}

	public void setPriorityMethod(Method priorityMethod)
	{
		this.priorityMethod = priorityMethod;
	}

	public int[] getPriorityParamIndexes()
	{
		return priorityParamIndexes;
	}

	public void setPriorityParamIndexes(int[] priorityParamIndexes)
	{
		this.priorityParamIndexes = priorityParamIndexes;
	}
}
