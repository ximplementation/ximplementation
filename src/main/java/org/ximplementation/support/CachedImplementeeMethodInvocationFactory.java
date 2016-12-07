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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cached {@linkplain ImplementeeMethodInvocationFactory}.
 * <p>
 * The <i>implement method</i> evaluating rule is the same as
 * {@linkplain SimpleImplementeeMethodInvocationFactory} except caching some
 * static process info for performance (eg. <i>implementor method</i> parameter
 * type validity checking and priority evaluation).
 * 
 * @author earthangry@gmail.com
 * @date 2016-12-6
 *
 */
public class CachedImplementeeMethodInvocationFactory
		extends AbstractImplementeeMethodInvocationFactory
{
	private ConcurrentHashMap<InvocationCacheKey, InvocationCacheValue> cachedStaticValidAndDescPrioritizeds = new ConcurrentHashMap<InvocationCacheKey, InvocationCacheValue>();

	public CachedImplementeeMethodInvocationFactory()
	{
		super();
	}

	@Override
	public ImplementeeMethodInvocation get(
			Implementation<?> implementation, Method implementeeMethod,
			Object[] invocationParams,
			ImplementorBeanFactory implementorBeanFactory) throws Throwable
	{
		ImplementInfo implementInfo = findImplementInfo(implementation,
				implementeeMethod);
	
		if (implementInfo == null || !implementInfo.hasImplementMethodInfo())
			return null;
	
		Class<?>[] invocationParamTypes = extractTypes(invocationParams);

		InvocationCacheKey invocationCacheKey = new InvocationCacheKey(implementation, implementInfo,
				invocationParamTypes);
		InvocationCacheValue invocationCacheValue = getCachedStaticValidAndDescPrioritizeds(
				invocationCacheKey);

		if (invocationCacheValue == null)
		{
			invocationCacheValue = evalInvocationCacheValue(implementation, implementInfo,
					invocationParamTypes);
			cacheStaticValidAndDescPrioritizeds(invocationCacheKey, invocationCacheValue);
		}
	
		ImplementMethodInfo[] staticValidAndDescPrioritizeds = invocationCacheValue
				.getStaticValidAndDescPrioritizeds();

		if (staticValidAndDescPrioritizeds == null
				|| staticValidAndDescPrioritizeds.length == 0)
			return null;

		if (!invocationCacheValue.isValidityMethodPresents()
				&& !invocationCacheValue.isPriorityMethodPresents())
		{
			return createBySelectingFromValidAndDescPrioritizeds(implementation,
					implementInfo, invocationParams,
					invocationParamTypes, staticValidAndDescPrioritizeds,
					implementorBeanFactory);
		}
		else
		{
			return createByEvalingFromValidAndDescPrioritizeds(implementation,
					implementInfo, invocationParams,
					invocationParamTypes, staticValidAndDescPrioritizeds,
					implementorBeanFactory);
		}
	}

	/**
	 * Evaluate {@linkplain InvocationCacheValue}.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param invocationParamTypes
	 * @return
	 */
	protected InvocationCacheValue evalInvocationCacheValue(Implementation<?> implementation,
			ImplementInfo implementInfo,
			Class<?>[] invocationParamTypes)
	{
		List<ImplementMethodInfo> staticValidAndDescPrioritizeds = new ArrayList<ImplementMethodInfo>();
		boolean validityMethodPresents = false;
		boolean priorityMethodPresents = false;
	
		if(implementInfo.hasImplementMethodInfo())
		{
			ImplementMethodInfo[] implementMethodInfos = implementInfo
					.getImplementMethodInfos();
			
			for (ImplementMethodInfo implementMethodInfo : implementMethodInfos)
			{
				// ignore parameter type not valid
				if (!isImplementMethodParamTypeValid(implementation,
						implementInfo, implementMethodInfo,
						invocationParamTypes))
					continue;
	
				if (!validityMethodPresents
						&& implementMethodInfo.hasValidityMethod())
					validityMethodPresents = true;
	
				if (!priorityMethodPresents
						&& implementMethodInfo.hasPriorityMethod())
					priorityMethodPresents = true;
	
				staticValidAndDescPrioritizeds.add(implementMethodInfo);
			}
		}
		
		ImplementMethodInfo[] staticValidAndDescPrioritizedAry = staticValidAndDescPrioritizeds.toArray(
				new ImplementMethodInfo[staticValidAndDescPrioritizeds.size()]);
	
		// sort by static priority
		sortByStaticPriority(implementation, implementInfo,
				invocationParamTypes, staticValidAndDescPrioritizedAry);
	
		return new InvocationCacheValue(staticValidAndDescPrioritizedAry,
				validityMethodPresents, priorityMethodPresents);
	}

	/**
	 * Create {@linkplain ImplementeeMethodInvocation} by selecting from valid
	 * and descendent prioritized {@linkplain ImplementMethodInfo} array.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param invocationParams
	 * @param invocationParamTypes
	 * @param validAndDescPrioritizeds
	 * @param implementorBeanFactory
	 * @return
	 */
	protected ImplementeeMethodInvocation createBySelectingFromValidAndDescPrioritizeds(
			Implementation<?> implementation, ImplementInfo implementInfo,
			Object[] invocationParams,
			Class<?>[] invocationParamTypes,
			ImplementMethodInfo[] validAndDescPrioritizeds,
			ImplementorBeanFactory implementorBeanFactory)
	{
		ImplementMethodInfo finalMethodInfo = null;
		Collection<?> finalBeans = null;

		for (int i = 0; i < validAndDescPrioritizeds.length; i++)
		{
			ImplementMethodInfo myMethodInfo = validAndDescPrioritizeds[i];
			Collection<?> myBeans = implementorBeanFactory
					.getImplementorBeans(myMethodInfo.getImplementor());

			if (myBeans != null && !myBeans.isEmpty())
			{
				finalMethodInfo = myMethodInfo;
				finalBeans = myBeans;

				break;
			}
		}

		if (finalMethodInfo == null)
			return null;

		Object finalBean = getRandomElement(finalBeans);

		return new SimpleImplementeeMethodInvocation(implementation,
				implementInfo, invocationParams, finalMethodInfo,
				finalBean);
	}

	/**
	 * Create {@linkplain ImplementeeMethodInvocation} by evaluating from valid
	 * and descendent prioritized {@linkplain ImplementMethodInfo} array.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param invocationParams
	 * @param invocationParamTypes
	 * @param validAndDescPrioritizeds
	 * @param implementorBeanFactory
	 * @return
	 * @throws Throwable
	 */
	protected ImplementeeMethodInvocation createByEvalingFromValidAndDescPrioritizeds(
			Implementation<?> implementation, ImplementInfo implementInfo,
			Object[] invocationParams,
			Class<?>[] invocationParamTypes,
			ImplementMethodInfo[] validAndDescPrioritizeds,
			ImplementorBeanFactory implementorBeanFactory) throws Throwable
	{
		ImplementMethodInfo implementMethodInfo = null;
		Object implementorBean = null;
		int priority = Integer.MIN_VALUE;
	
		for (ImplementMethodInfo myImplementMethodInfo : validAndDescPrioritizeds)
		{
			Collection<?> implementorBeans = implementorBeanFactory
					.getImplementorBeans(
							myImplementMethodInfo.getImplementor());
	
			if (implementorBeans == null || implementorBeans.isEmpty())
				continue;
	
			Method validityMethod = myImplementMethodInfo.getValidityMethod();
			Object[] validityMethodParams = myImplementMethodInfo
					.getValidityParams(invocationParams);
			Method priorityMethod = myImplementMethodInfo.getPriorityMethod();
			Object[] priorityMethodParams = myImplementMethodInfo
					.getPriorityParams(invocationParams);
	
			for (Object myImplementorBean : implementorBeans)
			{
				if (validityMethod != null)
				{
					boolean isValid = invokeValidityMethod(implementation,
							implementInfo, myImplementMethodInfo,
							validityMethod, validityMethodParams,
							myImplementorBean);
	
					if (!isValid)
						continue;
				}
	
				int myPriority = myImplementMethodInfo.getPriorityValue();
	
				if (priorityMethod != null)
				{
					myPriority = invokePriorityMethod(implementation,
							implementInfo, myImplementMethodInfo,
							myImplementMethodInfo.getPriorityMethod(),
							priorityMethodParams, myImplementorBean);
				}
	
				boolean replace = false;
	
				if (implementMethodInfo == null)
					replace = true;
				else
				{
					replace = (myPriority > priority);
				}
	
				if (replace)
				{
					implementMethodInfo = myImplementMethodInfo;
					implementorBean = myImplementorBean;
					priority = myPriority;
				}
			}
		}
	
		return (implementMethodInfo == null ? null
				: new SimpleImplementeeMethodInvocation(implementation,
						implementInfo,
						invocationParams,
						implementMethodInfo, implementorBean));
	}

	/**
	 * Sort {@linkplain ImplementMethodInfo} array by static priority.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param invocationParamTypes
	 * @param implementMethodInfos
	 */
	protected void sortByStaticPriority(Implementation<?> implementation,
			ImplementInfo implementInfo,
			Class<?>[] invocationParamTypes,
			ImplementMethodInfo[] implementMethodInfos)
	{
		Arrays.sort(implementMethodInfos,
				new StaticPriorityComparator(implementation,
						implementInfo, invocationParamTypes));
	}

	/**
	 * Get randon one element.
	 * 
	 * @param objs
	 * @return
	 */
	protected Object getRandomElement(Collection<?> objs)
	{
		if (objs == null || objs.size() == 0)
			return null;

		if (objs instanceof List<?>)
			return ((List<?>) objs).get(0);

		for (Object obj : objs)
			return obj;

		return null;
	}

	protected InvocationCacheValue getCachedStaticValidAndDescPrioritizeds(
			InvocationCacheKey cacheKey)
	{
		return this.cachedStaticValidAndDescPrioritizeds.get(cacheKey);
	}

	protected void cacheStaticValidAndDescPrioritizeds(
			InvocationCacheKey cacheKey, InvocationCacheValue cacheValue)
	{
		this.cachedStaticValidAndDescPrioritizeds.put(cacheKey, cacheValue);
	}

	/**
	 * Static priority comparator.
	 * 
	 * @author earthangry@gmail.com
	 * @date 2016-12-6
	 *
	 */
	private class StaticPriorityComparator
			implements Comparator<ImplementMethodInfo>
	{
		private Implementation<?> implementation;
		private ImplementInfo implementInfo;
		private Class<?>[] invocationParamTypes;

		public StaticPriorityComparator(Implementation<?> implementation,
				ImplementInfo implementInfo,
				Class<?>[] invocationParamTypes)
		{
			super();
			this.implementation = implementation;
			this.implementInfo = implementInfo;
			this.invocationParamTypes = invocationParamTypes;
		}

		@Override
		public int compare(ImplementMethodInfo a, ImplementMethodInfo b)
		{
			int re = a.getPriorityValue() - b.getPriorityValue();

			if (re == 0)
			{
				re = compareImplementMethodInfoPriority(this.implementation,
						this.implementInfo,
						this.invocationParamTypes,
						a, b);
			}

			return (0 - re);
		}
	}

	/**
	 * Invocation cache key.
	 * <p>
	 * It encapsulates the static input info of an <i>implementee method</i>
	 * invocation and be used as the cache key.
	 * </p>
	 * 
	 * @author earthangry@gmail.com
	 * @date 2016-12-6
	 *
	 */
	protected static class InvocationCacheKey
	{
		/** the Implementation of the invocation */
		private Implementation<?> implementation;

		/** the ImplementInfo of the invocation */
		private ImplementInfo implementInfo;

		/** the invocation parameter types */
		private Class<?>[] invocationParamTypes;

		public InvocationCacheKey()
		{
			super();
		}

		public InvocationCacheKey(Implementation<?> implementation,
				ImplementInfo implementInfo,
				Class<?>[] invocationParamTypes)
		{
			super();
			this.implementation = implementation;
			this.implementInfo = implementInfo;
			this.invocationParamTypes = invocationParamTypes;
		}

		/**
		 * Get the Implementation of the invocation.
		 * 
		 * @return
		 */
		public Implementation<?> getImplementation()
		{
			return implementation;
		}

		/**
		 * Set the Implementation of the invocation.
		 * 
		 * @param implementation
		 */
		public void setImplementation(Implementation<?> implementation)
		{
			this.implementation = implementation;
		}

		/**
		 * Get the ImplementInfo of the invocation.
		 * 
		 * @return
		 */
		public ImplementInfo getImplementInfo()
		{
			return implementInfo;
		}

		/**
		 * Set the ImplementInfo of the invocation.
		 * 
		 * @param implementInfo
		 */
		public void setImplementInfo(ImplementInfo implementInfo)
		{
			this.implementInfo = implementInfo;
		}

		/**
		 * Get the invocation parameter types.
		 * 
		 * @return
		 */
		public Class<?>[] getInvocationParamTypes()
		{
			return invocationParamTypes;
		}

		/**
		 * Set the invocation parameter types.
		 * 
		 * @param invocationParamTypes
		 */
		public void setInvocationParamTypes(Class<?>[] invocationParamTypes)
		{
			this.invocationParamTypes = invocationParamTypes;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((implementInfo == null) ? 0 : implementInfo.hashCode());
			result = prime * result + ((implementation == null) ? 0
					: implementation.hashCode());
			result = prime * result
					+ Arrays.hashCode(invocationParamTypes);
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
			InvocationCacheKey other = (InvocationCacheKey) obj;
			if (implementInfo == null)
			{
				if (other.implementInfo != null)
					return false;
			}
			else if (!implementInfo.equals(other.implementInfo))
				return false;
			if (implementation == null)
			{
				if (other.implementation != null)
					return false;
			}
			else if (!implementation.equals(other.implementation))
				return false;
			if (!Arrays.equals(invocationParamTypes,
					other.invocationParamTypes))
				return false;
			return true;
		}
	}

	/**
	 * Invocation cache value.
	 * <p>
	 * It encapsulates the static process info of an <i>implementee method</i>
	 * invocation and be used as the cache value.
	 * </p>
	 * <p>
	 * Each element in
	 * {@linkplain InvocationCacheValue#getStaticValidAndDescPrioritizeds()} 's
	 * {@linkplain ImplementMethodInfo#getImplementMethod()} parameter types are
	 * valid to corresponding
	 * {@linkplain InvocationCacheKey#getInvocationParamTypes()}.
	 * </p>
	 * <p>
	 * The preceding element in
	 * {@linkplain InvocationCacheValue#getStaticValidAndDescPrioritizeds()} 's
	 * static priority is higher than subsequent element to corresponding
	 * {@linkplain InvocationCacheKey#getInvocationParamTypes()} (eg.
	 * {@linkplain ImplementMethodInfo#getPriorityValue()} is higher or
	 * {@linkplain ImplementMethodInfo#getImplementMethod()} parameter types are
	 * closer).
	 * </p>
	 * 
	 * @author earthangry@gmail.com
	 * @date 2016-12-6
	 *
	 */
	protected static class InvocationCacheValue
	{
		/** the static valid and descendent prioritized ImplementMethodInfo */
		private ImplementMethodInfo[] staticValidAndDescPrioritizeds;

		/**
		 * if validity method presents in any of above
		 * staticValidAndDescPrioritizeds
		 */
		private boolean validityMethodPresents;

		/**
		 * if priority method presents in any of above
		 * staticValidAndDescPrioritizeds
		 */
		private boolean priorityMethodPresents;

		public InvocationCacheValue()
		{
			super();
		}

		public InvocationCacheValue(
				ImplementMethodInfo[] staticValidAndDescPrioritizeds,
				boolean validityMethodPresents,
				boolean priorityMethodPresents)
		{
			super();
			this.staticValidAndDescPrioritizeds = staticValidAndDescPrioritizeds;
			this.validityMethodPresents = validityMethodPresents;
			this.priorityMethodPresents = priorityMethodPresents;
		}

		/**
		 * Get the static valid and descendent prioritized
		 * {@linkplain ImplementMethodInfo}s.
		 * 
		 * @return
		 */
		public ImplementMethodInfo[] getStaticValidAndDescPrioritizeds()
		{
			return staticValidAndDescPrioritizeds;
		}

		/**
		 * Set the static valid and descendent prioritized
		 * {@linkplain ImplementMethodInfo}s.
		 * 
		 * @param staticValidAndDescPrioritizeds
		 */
		public void setStaticValidAndDescPrioritizeds(
				ImplementMethodInfo[] staticValidAndDescPrioritizeds)
		{
			this.staticValidAndDescPrioritizeds = staticValidAndDescPrioritizeds;
		}

		/**
		 * Returns if validity method presents in any of the
		 * {@linkplain #getStaticValidAndDescPrioritizeds()}.
		 * <p>
		 * If {@code true}, validity should be checked again for every
		 * invocation.
		 * </p>
		 * 
		 * @return
		 */
		public boolean isValidityMethodPresents()
		{
			return validityMethodPresents;
		}

		/**
		 * Set if validity method presents in any of the
		 * {@linkplain #getStaticValidAndDescPrioritizeds()}.
		 * 
		 * @param validityMethodPresents
		 */
		public void setValidityMethodPresents(boolean validityMethodPresents)
		{
			this.validityMethodPresents = validityMethodPresents;
		}

		/**
		 * Returns if priority method presents in any of the
		 * {@linkplain #getStaticValidAndDescPrioritizeds()}.
		 * <p>
		 * If {@code true}, priority should be checked again for every
		 * invocation.
		 * </p>
		 * 
		 * @return
		 */
		public boolean isPriorityMethodPresents()
		{
			return priorityMethodPresents;
		}

		/**
		 * Set if priority method presents in any of the
		 * {@linkplain #getStaticValidAndDescPrioritizeds()}.
		 * 
		 * @param priorityMethodPresents
		 */
		public void setPriorityMethodPresents(boolean priorityMethodPresents)
		{
			this.priorityMethodPresents = priorityMethodPresents;
		}
	}
}
