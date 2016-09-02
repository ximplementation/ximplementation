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
 * Implement method info.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
 *
 */
public class ImplementMethodInfo implements Serializable
{
	protected static final Object[] EMPTY_PARAMS = {};

	private static final long serialVersionUID = 1L;

	/** implementor */
	private Class<?> implementor;

	/** implement method */
	private Method implementMethod;

	/** implement method parameter types */
	private Class<?>[] paramTypes;

	/** generic implement method parameter types */
	private Type[] genericParamTypes;

	/** implement method parameter indexes */
	private int[] paramIndexes;

	/** validity method */
	private Method validityMethod;

	/** validity method parameter indexes */
	private int[] validityParamIndexes;

	/** priority value */
	private int priorityValue;

	/** priority method */
	private Method priorityMethod;

	/** priority method parameter indexes */
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

	/**
	 * Get parameter arrays of this <i>implement method</i> for the specified
	 * <i>implementee method</i> parameter array.
	 * <p>
	 * Returns an array of {@code 0} length if this <i>implement method</i> has
	 * no parameter.
	 * </p>
	 * 
	 * @param implementeeMethodParams
	 * @return
	 */
	public Object[] getParams(Object[] implementeeMethodParams)
	{
		return copyArrayByIndex(implementeeMethodParams, this.paramIndexes);
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

	/**
	 * Get parameter arrays of the <i>validity method</i> for the specified
	 * <i>implementee method</i> parameter array.
	 * <p>
	 * Returns an array of {@code 0} length if there is no <i>validity
	 * method</i>.
	 * </p>
	 * 
	 * @param implementeeMethodParams
	 * @return
	 */
	public Object[] getValidityParams(Object[] implementeeMethodParams)
	{
		return copyArrayByIndex(implementeeMethodParams,
				this.validityParamIndexes);
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

	/**
	 * Get parameter arrays of the <i>priority method</i> for the specified
	 * <i>implementee method</i> parameter array.
	 * <p>
	 * Returns an array of {@code 0} length if there is no <i>priority
	 * method</i>.
	 * </p>
	 * 
	 * @param implementeeMethodParams
	 * @return
	 */
	public Object[] getPriorityParams(Object[] implementeeMethodParams)
	{
		return this.copyArrayByIndex(implementeeMethodParams,
				this.priorityParamIndexes);
	}

	/**
	 * Copy array by indexes.
	 * <P>
	 * Returns an array of {@code 0} length if the {@code indexes} is
	 * {@code null} or empty.
	 * </P>
	 * 
	 * @param source
	 * @param indexes
	 * @return
	 */
	protected Object[] copyArrayByIndex(Object[] source, int[] indexes)
	{
		if (indexes == null || indexes.length == 0)
			return EMPTY_PARAMS;

		Object[] copied = new Object[indexes.length];

		for (int i = 0; i < indexes.length; i++)
		{
			copied[i] = source[indexes[i]];
		}

		return copied;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [implementor=" + implementor
				+ ", implementMethod=" + implementMethod + "]";
	}
}
