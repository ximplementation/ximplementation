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
 * static processing for performance (eg. <i>implementor method</i> parameter
 * type validity checking and priority evaluation).
 * 
 * @author earthangry@gmail.com
 * @date 2016-12-6
 *
 */
public class CachedImplementeeMethodInvocationFactory
		extends AbstractImplementeeMethodInvocationFactory
{
	private ConcurrentHashMap<CacheKey, CacheValue> cachedStaticValidAndDescPrioritizeds = new ConcurrentHashMap<CacheKey, CacheValue>();

	public CachedImplementeeMethodInvocationFactory()
	{
		super();
	}

	@Override
	public ImplementeeMethodInvocation get(
			Implementation<?> implementation, Method implementeeMethod,
			Object[] implementeeMethodParams,
			ImplementorBeanFactory implementorBeanFactory) throws Throwable
	{
		ImplementInfo implementInfo = findImplementInfo(implementation,
				implementeeMethod);
	
		if (implementInfo == null || !implementInfo.hasImplementMethodInfo())
			return null;
	
		Class<?>[] implementeeMethodParamTypes = extractTypes(
				implementeeMethodParams);

		CacheKey cacheKey = new CacheKey(implementation, implementInfo,
				implementeeMethodParamTypes);
		CacheValue cacheValue = getCachedStaticValidAndDescPrioritizeds(
				cacheKey);

		if (cacheValue == null)
		{
			cacheValue = evalCacheValue(implementation, implementInfo,
					implementeeMethodParamTypes);
			cacheStaticValidAndDescPrioritizeds(cacheKey, cacheValue);
		}
	
		ImplementMethodInfo[] staticValidAndDescPrioritizeds = cacheValue
				.getStaticValidAndDescPrioritizeds();

		if (staticValidAndDescPrioritizeds == null
				|| staticValidAndDescPrioritizeds.length == 0)
			return null;

		if (!cacheValue.isNeedInvokeValidityMethod()
				&& !cacheValue.isNeedInvokePriorityMethod())
		{
			return createBySelectingFromValidAndDescPrioritizeds(implementation,
					implementInfo, implementeeMethodParams,
					implementeeMethodParamTypes, staticValidAndDescPrioritizeds,
					implementorBeanFactory);
		}
		else
		{
			return createByEvalingFromValidAndDescPrioritizeds(implementation,
					implementInfo, implementeeMethodParams,
					implementeeMethodParamTypes, staticValidAndDescPrioritizeds,
					implementorBeanFactory);
		}
	}

	/**
	 * Create {@linkplain ImplementeeMethodInvocation} by evaluating from valid
	 * and descendent prioritized {@linkplain ImplementMethodInfo} array.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementeeMethodParams
	 * @param implementeeMethodParamTypes
	 * @param validAndDescPrioritizeds
	 * @param implementorBeanFactory
	 * @return
	 * @throws Throwable
	 */
	protected ImplementeeMethodInvocation createByEvalingFromValidAndDescPrioritizeds(
			Implementation<?> implementation, ImplementInfo implementInfo,
			Object[] implementeeMethodParams,
			Class<?>[] implementeeMethodParamTypes,
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
					.getValidityParams(implementeeMethodParams);
			Method priorityMethod = myImplementMethodInfo.getPriorityMethod();
			Object[] priorityMethodParams = myImplementMethodInfo
					.getPriorityParams(implementeeMethodParams);

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
						implementeeMethodParams,
						implementMethodInfo, implementorBean));
	}

	/**
	 * Create {@linkplain ImplementeeMethodInvocation} by selecting from valid
	 * and descendent prioritized {@linkplain ImplementMethodInfo} array.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementeeMethodParams
	 * @param implementeeMethodParamTypes
	 * @param validAndDescPrioritizeds
	 * @param implementorBeanFactory
	 * @return
	 */
	protected ImplementeeMethodInvocation createBySelectingFromValidAndDescPrioritizeds(
			Implementation<?> implementation, ImplementInfo implementInfo,
			Object[] implementeeMethodParams,
			Class<?>[] implementeeMethodParamTypes,
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
				implementInfo, implementeeMethodParams, finalMethodInfo,
				finalBean);
	}

	/**
	 * Evaluate {@linkplain CacheValue}.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementeeMethodParamTypes
	 * @return
	 */
	protected CacheValue evalCacheValue(Implementation<?> implementation,
			ImplementInfo implementInfo,
			Class<?>[] implementeeMethodParamTypes)
	{
		List<ImplementMethodInfo> staticValidAndDescPrioritizeds = new ArrayList<ImplementMethodInfo>();
		boolean needInvokeValidityMethod = false;
		boolean needInvokePriorityMethod = false;

		if(implementInfo.hasImplementMethodInfo())
		{
			ImplementMethodInfo[] implementMethodInfos = implementInfo
					.getImplementMethodInfos();
			
			for (ImplementMethodInfo implementMethodInfo : implementMethodInfos)
			{
				// ignore parameter type not valid
				if (!isImplementMethodParamTypeValid(implementation,
						implementInfo, implementMethodInfo,
						implementeeMethodParamTypes))
					continue;

				if (!needInvokeValidityMethod
						&& implementMethodInfo.hasValidityMethod())
					needInvokeValidityMethod = true;

				if (!needInvokePriorityMethod
						&& implementMethodInfo.hasPriorityMethod())
					needInvokePriorityMethod = true;

				staticValidAndDescPrioritizeds.add(implementMethodInfo);
			}
		}
		
		ImplementMethodInfo[] staticValidAndDescPrioritizedAry = staticValidAndDescPrioritizeds.toArray(
				new ImplementMethodInfo[staticValidAndDescPrioritizeds.size()]);

		// sort by static priority
		sortByStaticPriority(implementation, implementInfo,
				implementeeMethodParamTypes, staticValidAndDescPrioritizedAry);

		return new CacheValue(staticValidAndDescPrioritizedAry,
				needInvokeValidityMethod, needInvokePriorityMethod);
	}

	/**
	 * Sort {@linkplain ImplementMethodInfo} array by static priority.
	 * 
	 * @param implementation
	 * @param implementInfo
	 * @param implementeeMethodParamTypes
	 * @param implementMethodInfos
	 */
	protected void sortByStaticPriority(Implementation<?> implementation,
			ImplementInfo implementInfo,
			Class<?>[] implementeeMethodParamTypes,
			ImplementMethodInfo[] implementMethodInfos)
	{
		Arrays.sort(implementMethodInfos,
				new StaticPriorityComparator(implementation,
						implementInfo, implementeeMethodParamTypes));
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

	protected CacheValue getCachedStaticValidAndDescPrioritizeds(
			CacheKey cacheKey)
	{
		return this.cachedStaticValidAndDescPrioritizeds.get(cacheKey);
	}

	protected void cacheStaticValidAndDescPrioritizeds(
			CacheKey cacheKey, CacheValue cacheValue)
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
	protected class StaticPriorityComparator
			implements Comparator<ImplementMethodInfo>
	{
		private Implementation<?> implementation;
		private ImplementInfo implementInfo;
		private Class<?>[] implementeeMethodParamTypes;

		public StaticPriorityComparator(Implementation<?> implementation,
				ImplementInfo implementInfo,
				Class<?>[] implementeeMethodParamTypes)
		{
			super();
			this.implementation = implementation;
			this.implementInfo = implementInfo;
			this.implementeeMethodParamTypes = implementeeMethodParamTypes;
		}

		@Override
		public int compare(ImplementMethodInfo a, ImplementMethodInfo b)
		{
			int re = a.getPriorityValue() - b.getPriorityValue();

			if (re == 0)
			{
				re = compareImplementMethodInfoPriority(this.implementation,
						this.implementInfo,
						this.implementeeMethodParamTypes,
						a, b);
			}

			return (0 - re);
		}
	}

	/**
	 * Cache key.
	 * 
	 * @author earthangry@gmail.com
	 * @date 2016-12-6
	 *
	 */
	protected static class CacheKey
	{
		private Implementation<?> implementation;
		private ImplementInfo implementInfo;
		private Class<?>[] implementeeMethodParamTypes;

		public CacheKey()
		{
			super();
		}

		public CacheKey(Implementation<?> implementation,
				ImplementInfo implementInfo,
				Class<?>[] implementeeMethodParamTypes)
		{
			super();
			this.implementation = implementation;
			this.implementInfo = implementInfo;
			this.implementeeMethodParamTypes = implementeeMethodParamTypes;
		}

		public Implementation<?> getImplementation()
		{
			return implementation;
		}

		public void setImplementation(Implementation<?> implementation)
		{
			this.implementation = implementation;
		}

		public ImplementInfo getImplementInfo()
		{
			return implementInfo;
		}

		public void setImplementInfo(ImplementInfo implementInfo)
		{
			this.implementInfo = implementInfo;
		}

		public Class<?>[] getImplementeeMethodParamTypes()
		{
			return implementeeMethodParamTypes;
		}

		public void setImplementeeMethodParamTypes(
				Class<?>[] implementeeMethodParamTypes)
		{
			this.implementeeMethodParamTypes = implementeeMethodParamTypes;
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
					+ Arrays.hashCode(implementeeMethodParamTypes);
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
			CacheKey other = (CacheKey) obj;
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
			if (!Arrays.equals(implementeeMethodParamTypes,
					other.implementeeMethodParamTypes))
				return false;
			return true;
		}
	}

	/**
	 * Cache value.
	 * 
	 * @author earthangry@gmail.com
	 * @date 2016-12-6
	 *
	 */
	protected static class CacheValue
	{
		private ImplementMethodInfo[] staticValidAndDescPrioritizeds;

		private boolean needInvokeValidityMethod;

		private boolean needInvokePriorityMethod;

		public CacheValue()
		{
			super();
		}

		public CacheValue(
				ImplementMethodInfo[] staticValidAndDescPrioritizeds,
				boolean needInvokeValidityMethod,
				boolean needInvokePriorityMethod)
		{
			super();
			this.staticValidAndDescPrioritizeds = staticValidAndDescPrioritizeds;
			this.needInvokeValidityMethod = needInvokeValidityMethod;
			this.needInvokePriorityMethod = needInvokePriorityMethod;
		}

		public ImplementMethodInfo[] getStaticValidAndDescPrioritizeds()
		{
			return staticValidAndDescPrioritizeds;
		}

		public void setStaticValidAndDescPrioritizeds(
				ImplementMethodInfo[] staticValidAndDescPrioritizeds)
		{
			this.staticValidAndDescPrioritizeds = staticValidAndDescPrioritizeds;
		}

		public boolean isNeedInvokeValidityMethod()
		{
			return needInvokeValidityMethod;
		}

		public void setNeedInvokeValidityMethod(
				boolean needInvokeValidityMethod)
		{
			this.needInvokeValidityMethod = needInvokeValidityMethod;
		}

		public boolean isNeedInvokePriorityMethod()
		{
			return needInvokePriorityMethod;
		}

		public void setNeedInvokePriorityMethod(
				boolean needInvokePriorityMethod)
		{
			this.needInvokePriorityMethod = needInvokePriorityMethod;
		}
	}
}
