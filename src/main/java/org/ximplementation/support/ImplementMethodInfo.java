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

import org.ximplementation.Priority;

/**
 * Implement method info.
 * <p>
 * It describes an <i>implement method</i> info of a given <i>implementee
 * method</i>.
 * </p>
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
	private int priorityValue = Priority.DEFAULT;

	/** priority method */
	private Method priorityMethod;

	/** priority method parameter indexes */
	private int[] priorityParamIndexes;

	/**
	 * Create an {@code ImplementMethodInfo} instance.
	 */
	public ImplementMethodInfo()
	{
		super();
	}

	/**
	 * Create an {@code ImplementMethodInfo} instance.
	 * 
	 * @param implementor
	 *            The <i>implementor</i>.
	 * @param implementMethod
	 *            One of the <i>implement method</i>s in the <i>implementor</i>.
	 */
	public ImplementMethodInfo(Class<?> implementor, Method implementMethod)
	{
		super();
		this.implementor = implementor;
		this.implementMethod = implementMethod;
	}

	/**
	 * Get the <i>implementor</i>.
	 * 
	 * @return
	 */
	public Class<?> getImplementor()
	{
		return implementor;
	}

	/**
	 * Set the <i>implementor</i>.
	 * 
	 * @param implementor
	 */
	public void setImplementor(Class<?> implementor)
	{
		this.implementor = implementor;
	}

	/**
	 * Get the <i>implement method</i>.
	 * 
	 * @return
	 */
	public Method getImplementMethod()
	{
		return implementMethod;
	}

	/**
	 * Set the <i>implement method</i>.
	 * 
	 * @param implementMethod
	 */
	public void setImplementMethod(Method implementMethod)
	{
		this.implementMethod = implementMethod;
	}

	/**
	 * Get the parameter types about the <i>implement method</i>.
	 * 
	 * @return
	 */
	public Class<?>[] getParamTypes()
	{
		return paramTypes;
	}

	/**
	 * Set the parameter types about the <i>implement method</i>.
	 * 
	 * @param paramTypes
	 */
	public void setParamTypes(Class<?>[] paramTypes)
	{
		this.paramTypes = paramTypes;
	}

	/**
	 * Get the generic parameter types about the <i>implement method</i>.
	 * 
	 * @return
	 */
	public Type[] getGenericParamTypes()
	{
		return genericParamTypes;
	}

	/**
	 * Set the generic parameter types about the <i>implement method</i>.
	 * 
	 * @param genericParamTypes
	 */
	public void setGenericParamTypes(Type[] genericParamTypes)
	{
		this.genericParamTypes = genericParamTypes;
	}

	/**
	 * Get the parameter indexes about the <i>implement method</i>.
	 * @return
	 */
	public int[] getParamIndexes()
	{
		return paramIndexes;
	}

	/**
	 * Set the parameter indexes about the <i>implement method</i>.
	 * 
	 * @param paramIndexes
	 */
	public void setParamIndexes(int[] paramIndexes)
	{
		this.paramIndexes = paramIndexes;
	}

	/**
	 * Get the parameter array of this <i>implement method</i> for given
	 * <i>implementee method</i> parameter array.
	 * 
	 * @param implementeeMethodParams
	 *            The <i>implementee method</i> parameter array.
	 * @return An parameter array for this <i>implement method</i> invocation,
	 *         {@code 0} length if this <i>implement method</i> has no
	 *         parameter.
	 */
	public Object[] getParams(Object[] implementeeMethodParams)
	{
		return copyArrayByIndex(implementeeMethodParams, this.paramIndexes);
	}

	/**
	 * Return if the <i>implement method</i> has a validity method.
	 * 
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean hasValidityMethod()
	{
		return (this.validityMethod != null);
	}

	/**
	 * Get the validity method for the <i>implement method</i>.
	 * 
	 * @return The validity method, {@code null} if none.
	 */
	public Method getValidityMethod()
	{
		return validityMethod;
	}

	/**
	 * Set the validity method for the <i>implement method</i>.
	 * 
	 * @param validityMethod
	 */
	public void setValidityMethod(Method validityMethod)
	{
		this.validityMethod = validityMethod;
	}

	/**
	 * Get the validity method parameter indexes about the <i>implement
	 * method</i>.
	 * 
	 * @return
	 */
	public int[] getValidityParamIndexes()
	{
		return validityParamIndexes;
	}

	/**
	 * Set the validity method parameter indexes about the <i>implement
	 * method</i>.
	 * 
	 * @param validityParamIndexes
	 */
	public void setValidityParamIndexes(int[] validityParamIndexes)
	{
		this.validityParamIndexes = validityParamIndexes;
	}

	/**
	 * Get the parameter array of the validity method for given <i>implementee
	 * method</i> parameter array.
	 * 
	 * @param implementeeMethodParams
	 *            The <i>implementee method</i> parameter array.
	 * @return An parameter array for the validity method invocation, {@code 0}
	 *         length if {@linkplain #getValidityParamIndexes()} is {@code null}
	 *         .
	 */
	public Object[] getValidityParams(Object[] implementeeMethodParams)
	{
		return copyArrayByIndex(implementeeMethodParams,
				this.validityParamIndexes);
	}

	/**
	 * Get the fixed priority value for the <i>implement method</i>.
	 * 
	 * @return The fixed priority value.
	 */
	public int getPriorityValue()
	{
		return priorityValue;
	}

	/**
	 * Set the fixed priority value for the <i>implement method</i>.
	 * 
	 * @param priorityValue
	 */
	public void setPriorityValue(int priorityValue)
	{
		this.priorityValue = priorityValue;
	}

	/**
	 * Return if the <i>implement method</i> has a priority method.
	 * 
	 * @return {@code true} if yes, {@code false} if no.
	 */
	public boolean hasPriorityMethod()
	{
		return (this.priorityMethod != null);
	}

	/**
	 * Get the priority method for the <i>implement method</i>.
	 * 
	 * @return The priority method, {@code null} if none.
	 */
	public Method getPriorityMethod()
	{
		return priorityMethod;
	}

	/**
	 * Set the priority method for the <i>implement method</i>.
	 * 
	 * @param priorityMethod
	 */
	public void setPriorityMethod(Method priorityMethod)
	{
		this.priorityMethod = priorityMethod;
	}

	/**
	 * Get the priority method parameter indexes about the <i>implement
	 * method</i>.
	 * 
	 * @return
	 */
	public int[] getPriorityParamIndexes()
	{
		return priorityParamIndexes;
	}

	/**
	 * Set the priority method parameter indexes about the <i>implement
	 * method</i>.
	 * 
	 * @param priorityParamIndexes
	 */
	public void setPriorityParamIndexes(int[] priorityParamIndexes)
	{
		this.priorityParamIndexes = priorityParamIndexes;
	}

	/**
	 * Get the parameter array of the priority method for given <i>implementee
	 * method</i> parameter array.
	 * 
	 * @param implementeeMethodParams
	 *            The <i>implementee method</i> parameter array.
	 * @return An parameter array for the priority method invocation, {@code 0}
	 *         length if {@linkplain #getPriorityParamIndexes()} is {@code null}
	 *         .
	 */
	public Object[] getPriorityParams(Object[] implementeeMethodParams)
	{
		return this.copyArrayByIndex(implementeeMethodParams,
				this.priorityParamIndexes);
	}

	/**
	 * Copy array by indexes.
	 * <P>
	 * Return an array of {@code 0} length if the {@code indexes} is
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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((implementMethod == null) ? 0 : implementMethod.hashCode());
		result = prime * result
				+ ((implementor == null) ? 0 : implementor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImplementMethodInfo other = (ImplementMethodInfo) obj;
		if (implementMethod == null)
		{
			if (other.implementMethod != null)
				return false;
		}
		else if (!implementMethod.equals(other.implementMethod))
			return false;
		if (implementor == null)
		{
			if (other.implementor != null)
				return false;
		}
		else if (!implementor.equals(other.implementor))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [implementor=" + implementor
				+ ", implementMethod=" + implementMethod + "]";
	}
}
