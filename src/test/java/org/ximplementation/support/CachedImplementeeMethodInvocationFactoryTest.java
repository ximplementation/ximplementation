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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.Priority;
import org.ximplementation.Validity;
import org.ximplementation.support.CachedImplementeeMethodInvocationFactory.StaticInvocationInputInfo;
import org.ximplementation.support.CachedImplementeeMethodInvocationFactory.StaticInvocationProcessInfo;

/**
 * {@linkplain CachedImplementeeMethodInvocationFactory} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-12-6
 *
 */
public class CachedImplementeeMethodInvocationFactoryTest
		extends AbstractTestSupport
{
	private CachedImplementeeMethodInvocationFactory cachedImplementeeMethodInvocationFactory;
	private ImplementationResolver implementationResolver;

	@Before
	public void setUp() throws Exception
	{
		this.cachedImplementeeMethodInvocationFactory = new CachedImplementeeMethodInvocationFactory();
		this.implementationResolver = new ImplementationResolver();
	}

	@After
	public void tearDown() throws Exception
	{
		this.cachedImplementeeMethodInvocationFactory = null;
		this.implementationResolver = null;
	}

	@Test
	public void getTest() throws Throwable
	{
		// implementInfo == null || !implementInfo.hasImplementMethodInfo()
		{
			Class<?> implementee = GetTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver.resolve(implementee);
			Method implementeeMethod = getMethodByName(implementee, "plus");

			assertNull(this.cachedImplementeeMethodInvocationFactory.get(
					implementation,
					implementeeMethod, new Object[] { "a", "b" },
					new SimpleImplementorBeanFactory()));
		}

		// invocationCacheValue == null
		{
			Class<?> implementee = GetTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee, GetTest.Implementor0.class,
							GetTest.Implementor1.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			StaticInvocationInputInfo key = new StaticInvocationInputInfo(
					implementation, implementInfo,
					new Class<?>[] { java.util.Date.class,
							java.util.Date.class });

			assertNull(this.cachedImplementeeMethodInvocationFactory
					.getCachedStaticValidAndDescPrioritizeds(key));

			assertNull(this.cachedImplementeeMethodInvocationFactory.get(
					implementation,
					implementeeMethod,
					new Object[] { new java.util.Date(), new java.util.Date() },
					new SimpleImplementorBeanFactory()));
			
			assertNotNull(this.cachedImplementeeMethodInvocationFactory
					.getCachedStaticValidAndDescPrioritizeds(key));
		}

		// staticValidAndDescPrioritizeds == null ||
		// staticValidAndDescPrioritizeds.length == 0
		{
			Class<?> implementee = GetTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee, GetTest.Implementor0.class,
							GetTest.Implementor1.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");

			assertNull(this.cachedImplementeeMethodInvocationFactory.get(
					implementation, implementeeMethod,
					new Object[] { "a", "b" },
					new SimpleImplementorBeanFactory()));
		}

		// !invocationCacheValue.isValidityMethodPresents() &&
		// !invocationCacheValue.isPriorityMethodPresents()
		{
			Class<?> implementee = GetTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee, GetTest.Implementor0.class,
							GetTest.Implementor1.class,
							GetTest.Implementor2.class,
							GetTest.Implementor3.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			
			CountImplementorBeanFactory implementorBeanFactory = CountImplementorBeanFactory.valueOf(
					new GetTest.Implementor0(),
					new GetTest.Implementor1(),
					new GetTest.Implementor2(),
					new GetTest.Implementor3());

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.get(
					implementation,
					implementeeMethod,
							new Object[] { 1, 2 },
							implementorBeanFactory);

			assertEquals(GetTest.Implementor3.class,
					invocation.getImplementMethodInfo().getImplementor());

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.getCachedStaticValidAndDescPrioritizeds(new StaticInvocationInputInfo(
							implementation, implementInfo,
									new Class<?>[] { Integer.class,
											Integer.class }));
			assertEquals(3,
					processInfo.getStaticValidAndDescPrioritizeds().length);
			assertEquals(GetTest.Implementor3.class,
					processInfo.getStaticValidAndDescPrioritizeds()[0]
							.getImplementor());

			// createBySelectingFromValidAndDescPrioritizeds(...) only get
			// implementor beans with the max priority
			assertEquals(0, implementorBeanFactory
					.getCount(GetTest.Implementor0.class));
			assertEquals(0, implementorBeanFactory
					.getCount(GetTest.Implementor1.class));
			assertEquals(0, implementorBeanFactory
					.getCount(GetTest.Implementor2.class));
			assertEquals(1, implementorBeanFactory
					.getCount(GetTest.Implementor3.class));

			this.cachedImplementeeMethodInvocationFactory.get(implementation,
					implementeeMethod, new Object[] { 1, 2 },
					implementorBeanFactory);

			assertEquals(0, implementorBeanFactory
					.getCount(GetTest.Implementor0.class));
			assertEquals(0, implementorBeanFactory
					.getCount(GetTest.Implementor1.class));
			assertEquals(0, implementorBeanFactory
					.getCount(GetTest.Implementor2.class));
			assertEquals(2, implementorBeanFactory
					.getCount(GetTest.Implementor3.class));
		}

		// else
		{
			Class<?> implementee = GetTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee, GetTest.Implementor0.class,
							GetTest.Implementor1.class,
							GetTest.Implementor2.class,
							GetTest.Implementor3.class,
							GetTest.Implementor4.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			CountImplementorBeanFactory implementorBeanFactory = CountImplementorBeanFactory
					.valueOf(new GetTest.Implementor0(),
							new GetTest.Implementor1(),
							new GetTest.Implementor2(),
							new GetTest.Implementor3(),
							new GetTest.Implementor4());

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.get(implementation, implementeeMethod,
							new Object[] { 1, 2 }, implementorBeanFactory);

			assertEquals(GetTest.Implementor3.class,
					invocation.getImplementMethodInfo().getImplementor());

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.getCachedStaticValidAndDescPrioritizeds(
							new StaticInvocationInputInfo(implementation,
									implementInfo, new Class<?>[] {
											Integer.class, Integer.class }));

			assertEquals(4,
					processInfo.getStaticValidAndDescPrioritizeds().length);
			assertEquals(GetTest.Implementor3.class,
					processInfo.getStaticValidAndDescPrioritizeds()[0]
							.getImplementor());

			// createByEvalingFromValidAndDescPrioritizeds(...) get
			// all implementor beans to evaluate the max priority
			assertEquals(1, implementorBeanFactory
					.getCount(GetTest.Implementor0.class));
			assertEquals(1, implementorBeanFactory
					.getCount(GetTest.Implementor1.class));
			assertEquals(0, implementorBeanFactory
					.getCount(GetTest.Implementor2.class));
			assertEquals(1, implementorBeanFactory
					.getCount(GetTest.Implementor3.class));
			assertEquals(1, implementorBeanFactory
					.getCount(GetTest.Implementor4.class));

			this.cachedImplementeeMethodInvocationFactory.get(implementation,
					implementeeMethod, new Object[] { 1, 2 },
					implementorBeanFactory);

			assertEquals(2, implementorBeanFactory
					.getCount(GetTest.Implementor0.class));
			assertEquals(2, implementorBeanFactory
					.getCount(GetTest.Implementor1.class));
			assertEquals(0, implementorBeanFactory
					.getCount(GetTest.Implementor2.class));
			assertEquals(2, implementorBeanFactory
					.getCount(GetTest.Implementor3.class));
			assertEquals(2, implementorBeanFactory
					.getCount(GetTest.Implementor4.class));
		}
	}

	public static class GetTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);

			Number minus(Number a, Number b);
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			public int plus(Integer a, Integer b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			public float plus(Float a, Float b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			@Priority(priority = Integer.MAX_VALUE)
			public Number plus(Number a, Number b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor4
		{
			@Implement
			@Validity("isValid")
			public float plus(Integer a, Integer b)
			{
				return 0;
			}

			public boolean isValid(Integer a)
			{
				return a > 0;
			}
		}
	}

	@Test
	public void evalStaticInvocationProcessInfoTest() throws Throwable
	{
		// !implementInfo.hasImplementMethodInfo()
		{
			Class<?> implementee = EvalInvocationCacheValueTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							new Class<?>[] { Integer.class, Integer.class });

			assertEquals(0,
					processInfo.getStaticValidAndDescPrioritizeds().length);
		}

		// !isImplementMethodParamTypeValid(...)
		{
			Class<?> implementee = EvalInvocationCacheValueTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							EvalInvocationCacheValueTest.Implementor0.class,
							EvalInvocationCacheValueTest.Implementor1.class,
							EvalInvocationCacheValueTest.Implementor2.class,
							EvalInvocationCacheValueTest.Implementor3.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							new Class<?>[] { Integer.class, Integer.class });
			
			assertFalse(processInfo.isValidityMethodPresents());
			assertFalse(processInfo.isPriorityMethodPresents());

			assertEquals(3,
					processInfo.getStaticValidAndDescPrioritizeds().length);
			assertEquals(EvalInvocationCacheValueTest.Implementor3.class,
					processInfo.getStaticValidAndDescPrioritizeds()[0]
							.getImplementor());
			assertEquals(EvalInvocationCacheValueTest.Implementor1.class,
					processInfo.getStaticValidAndDescPrioritizeds()[1]
							.getImplementor());
			assertEquals(EvalInvocationCacheValueTest.Implementor0.class,
					processInfo.getStaticValidAndDescPrioritizeds()[2]
							.getImplementor());
		}
		
		// !validityMethodPresents && implementMethodInfo.hasValidityMethod()
		{
			Class<?> implementee = EvalInvocationCacheValueTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							EvalInvocationCacheValueTest.Implementor0.class,
							EvalInvocationCacheValueTest.Implementor1.class,
							EvalInvocationCacheValueTest.Implementor2.class,
							EvalInvocationCacheValueTest.Implementor4.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							new Class<?>[] { Integer.class, Integer.class });

			assertTrue(processInfo.isValidityMethodPresents());
			assertFalse(processInfo.isPriorityMethodPresents());

			assertEquals(3,
					processInfo.getStaticValidAndDescPrioritizeds().length);
			assertEquals(EvalInvocationCacheValueTest.Implementor4.class,
					processInfo.getStaticValidAndDescPrioritizeds()[0]
							.getImplementor());
			assertEquals(EvalInvocationCacheValueTest.Implementor1.class,
					processInfo.getStaticValidAndDescPrioritizeds()[1]
							.getImplementor());
			assertEquals(EvalInvocationCacheValueTest.Implementor0.class,
					processInfo.getStaticValidAndDescPrioritizeds()[2]
							.getImplementor());
		}

		// !priorityMethodPresents && implementMethodInfo.hasPriorityMethod()
		{
			Class<?> implementee = EvalInvocationCacheValueTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							EvalInvocationCacheValueTest.Implementor0.class,
							EvalInvocationCacheValueTest.Implementor1.class,
							EvalInvocationCacheValueTest.Implementor2.class,
							EvalInvocationCacheValueTest.Implementor5.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							new Class<?>[] { Integer.class, Integer.class });

			assertTrue(processInfo.isValidityMethodPresents());
			assertTrue(processInfo.isPriorityMethodPresents());

			assertEquals(3,
					processInfo.getStaticValidAndDescPrioritizeds().length);
			assertEquals(EvalInvocationCacheValueTest.Implementor5.class,
					processInfo.getStaticValidAndDescPrioritizeds()[0]
							.getImplementor());
			assertEquals(EvalInvocationCacheValueTest.Implementor1.class,
					processInfo.getStaticValidAndDescPrioritizeds()[1]
							.getImplementor());
			assertEquals(EvalInvocationCacheValueTest.Implementor0.class,
					processInfo.getStaticValidAndDescPrioritizeds()[2]
							.getImplementor());
		}
	}

	protected static class EvalInvocationCacheValueTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);

			Number minus(Number a, Number b);
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			public int plus(Integer a, Integer b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			public float plus(Float a, Float b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			@Priority(priority = Integer.MAX_VALUE)
			public Number plus(Number a, Number b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor4
		{
			@Implement
			@Validity("isValid")
			public float plus(Integer a, Integer b)
			{
				return 0;
			}

			public boolean isValid(Integer a)
			{
				return a > 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor5
		{
			@Implement
			@Validity("isValid")
			@Priority("getPriority")
			public float plus(Integer a, Integer b)
			{
				return 0;
			}

			public boolean isValid(Integer a)
			{
				return a > 0;
			}

			public int getPriority()
			{
				return 1;
			}
		}
	}

	@Test
	public void createBySelectingFromValidAndDescPrioritizedsTest()
	{
		// isStaticImplementMethod(myMethodInfo)
		{
			Class<?> implementee = CreateBySelectingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver.resolve(implementee,
					CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0.class,
					CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor3.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation.getImplementInfo(implementeeMethod);

			Object[] invocationParams = new Integer[] { 1, 2 };
			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class, Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo, invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo.getStaticValidAndDescPrioritizeds();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf(new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0());

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.createBySelectingFromValidAndDescPrioritizeds(implementation, implementInfo, invocationParams,
							invocationParamTypes, validAndDescPrioritizeds, implementorBeanFactory);

			assertEquals(CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor3.class,
					invocation.getImplementMethodInfo().getImplementor());
			assertNull(invocation.getImplementorBean());
		}

		// myBeans != null && !myBeans.isEmpty()
		{
			Class<?> implementee = CreateBySelectingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0.class,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor1.class,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor2.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			Object[] invocationParams = new Integer[] { 1, 2 };
			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo
					.getStaticValidAndDescPrioritizeds();

			Object expectedImplementorBean = new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor1();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf(
							new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0(),
							expectedImplementorBean,
							new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor2());

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.createBySelectingFromValidAndDescPrioritizeds(
							implementation, implementInfo, invocationParams,
							invocationParamTypes, validAndDescPrioritizeds,
							implementorBeanFactory);

			assertEquals(
					CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor1.class,
					invocation.getImplementMethodInfo().getImplementor());
			assertTrue(
					expectedImplementorBean == invocation.getImplementorBean());

		}

		// myBeans == null
		{
			Class<?> implementee = CreateBySelectingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0.class,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor1.class,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor2.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			Object[] invocationParams = new Integer[] { 1, 2 };
			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo
					.getStaticValidAndDescPrioritizeds();

			Object expectedImplementorBean = new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf(
							expectedImplementorBean,
							new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor2());

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.createBySelectingFromValidAndDescPrioritizeds(
							implementation, implementInfo, invocationParams,
							invocationParamTypes, validAndDescPrioritizeds,
							implementorBeanFactory);

			assertEquals(
					CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0.class,
					invocation.getImplementMethodInfo().getImplementor());
			assertTrue(
					expectedImplementorBean == invocation.getImplementorBean());
		}

		// finalMethodInfo == null
		{
			Class<?> implementee = CreateBySelectingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0.class,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor1.class,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor2.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			Object[] invocationParams = new Integer[] { 1, 2 };
			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo
					.getStaticValidAndDescPrioritizeds();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf();

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.createBySelectingFromValidAndDescPrioritizeds(
							implementation, implementInfo, invocationParams,
							invocationParamTypes, validAndDescPrioritizeds,
							implementorBeanFactory);

			assertNull(invocation);
		}

		{
			Class<?> implementee = CreateBySelectingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0.class,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor1.class,
							CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor2.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			Object[] invocationParams = new Integer[] { 1, 2 };
			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo
					.getStaticValidAndDescPrioritizeds();

			Object expectedImplementorBean = new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor1();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf(
							new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor0(),
							expectedImplementorBean,
							new CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor2());

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.createBySelectingFromValidAndDescPrioritizeds(
							implementation, implementInfo, invocationParams,
							invocationParamTypes, validAndDescPrioritizeds,
							implementorBeanFactory);

			assertEquals(
					CreateBySelectingFromValidAndDescPrioritizedsTest.Implementor1.class,
					invocation.getImplementMethodInfo().getImplementor());
			assertTrue(
					expectedImplementorBean == invocation.getImplementorBean());
		}
	}

	public static class CreateBySelectingFromValidAndDescPrioritizedsTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);

			Number minus(Number a, Number b);
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			public int plus(Integer a, Integer b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			public float plus(Float a, Float b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			public static int plus(Integer a, Integer b)
			{
				return 0;
			}
		}
	}

	@Test
	public void createByEvaluatingFromValidAndDescPrioritizedsTest()
			throws Throwable
	{
		// isStaticImplementMethod(myImplementMethodInfo)
		{
			Class<?> implementee = CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver.resolve(implementee,
					CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0.class,
					CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1.class,
					CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2.class,
					CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor6.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation.getImplementInfo(implementeeMethod);

			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class, Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo, invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo.getStaticValidAndDescPrioritizeds();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory.valueOf(
					new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0(),
					new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1(),
					new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2());

			Object[] invocationParams = new Integer[] { 1, 2 };

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.createByEvaluatingFromValidAndDescPrioritizeds(implementation, implementInfo, invocationParams,
							invocationParamTypes, validAndDescPrioritizeds, implementorBeanFactory);

			assertEquals(CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor6.class,
					invocation.getImplementMethodInfo().getImplementor());
			assertNull(invocation.getImplementorBean());
		}

		// implementorBeans = getImplementorBeansWithCache
		{
			Class<?> implementee = CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor3.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor4.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			Object[] invocationParams = new Integer[] { 1, 2 };
			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation,
							implementInfo, invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo
					.getStaticValidAndDescPrioritizeds();

			CountImplementorBeanFactory implementorBeanFactory = CountImplementorBeanFactory
					.valueOf(
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor3(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor4());

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.createByEvaluatingFromValidAndDescPrioritizeds(
							implementation, implementInfo, invocationParams,
							invocationParamTypes, validAndDescPrioritizeds,
							implementorBeanFactory);

			assertNotNull(invocation);
			assertEquals(1, implementorBeanFactory
					.getCount(
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0.class));
			assertEquals(1, implementorBeanFactory.getCount(
					CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1.class));
			assertEquals(0, implementorBeanFactory
					.getCount(
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2.class));
			assertEquals(1, implementorBeanFactory
					.getCount(
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor3.class));
			assertEquals(1, implementorBeanFactory
					.getCount(
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor4.class));
		}

		// implementorBeans == null || implementorBeans.isEmpty()
		{
			Class<?> implementee = CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor3.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor4.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			Object[] invocationParams = new Integer[] { 1, 2 };
			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo
					.getStaticValidAndDescPrioritizeds();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf();

			DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
					.createByEvaluatingFromValidAndDescPrioritizeds(
							implementation, implementInfo, invocationParams,
							invocationParamTypes, validAndDescPrioritizeds,
							implementorBeanFactory);

			assertNull(invocation);
		}

		// invokeValidityMethod(...)
		{
			Class<?> implementee = CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor4.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo
					.getStaticValidAndDescPrioritizeds();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf(
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest
									.Implementor1(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest
									.Implementor2(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest
									.Implementor4());

			// !isValid
			{
				Object[] invocationParams = new Integer[] { -1, 0 };

				DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
						.createByEvaluatingFromValidAndDescPrioritizeds(
								implementation, implementInfo, invocationParams,
								invocationParamTypes, validAndDescPrioritizeds,
								implementorBeanFactory);

				assertEquals(
						CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1.class,
						invocation.getImplementorBean().getClass());
			}

			// isValid
			{
				Object[] invocationParams = new Integer[] { 1, 2 };

				DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
						.createByEvaluatingFromValidAndDescPrioritizeds(
								implementation, implementInfo, invocationParams,
								invocationParamTypes, validAndDescPrioritizeds,
								implementorBeanFactory);

				assertEquals(
						CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor4.class,
						invocation.getImplementorBean().getClass());
			}
		}

		// invokePriorityMethod(...)
		{
			Class<?> implementee = CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2.class,
							CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor5.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			StaticInvocationProcessInfo processInfo = this.cachedImplementeeMethodInvocationFactory
					.evalStaticInvocationProcessInfo(implementation, implementInfo,
							invocationParamTypes);
			ImplementMethodInfo[] validAndDescPrioritizeds = processInfo
					.getStaticValidAndDescPrioritizeds();

			ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
					.valueOf(
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor0(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor2(),
							new CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor5());

			// myPriority < 0
			{
				Object[] invocationParams = new Integer[] { -1, 0 };

				DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
						.createByEvaluatingFromValidAndDescPrioritizeds(
								implementation, implementInfo, invocationParams,
								invocationParamTypes, validAndDescPrioritizeds,
								implementorBeanFactory);

				assertEquals(
						CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor1.class,
						invocation.getImplementorBean().getClass());
			}

			// myPriority > 0
			{
				Object[] invocationParams = new Integer[] { 1, 2 };

				DefaultImplementeeMethodInvocation invocation = (DefaultImplementeeMethodInvocation) this.cachedImplementeeMethodInvocationFactory
						.createByEvaluatingFromValidAndDescPrioritizeds(
								implementation, implementInfo, invocationParams,
								invocationParamTypes, validAndDescPrioritizeds,
								implementorBeanFactory);

				assertEquals(
						CreateByEvaluatingFromValidAndDescPrioritizedsTest.Implementor5.class,
						invocation.getImplementorBean().getClass());
			}
		}
	}
	
	public static class CreateByEvaluatingFromValidAndDescPrioritizedsTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);

			Number minus(Number a, Number b);
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			public int plus(Integer a, Integer b)
			{
				return 0;
			}

			@Implement("plus")
			public Number plus1(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			public float plus(Float a, Float b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			@Priority(priority = Integer.MAX_VALUE)
			public Number plus(Number a, Number b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor4
		{
			@Implement
			@Validity("isValid")
			public float plus(Integer a, Integer b)
			{
				return 0;
			}

			public boolean isValid(Integer a)
			{
				return a > 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor5
		{
			@Implement
			@Priority("getPriority")
			public float plus(Integer a, Integer b)
			{
				return 0;
			}

			public int getPriority(Integer a)
			{
				return a;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor6
		{
			@Implement
			@Priority("getPriority")
			public static float plus(Integer a, Integer b)
			{
				return 0;
			}

			public static int getPriority(Integer a)
			{
				return Integer.MAX_VALUE;
			}
		}
	}

	@Test
	public void sortByStaticPriorityTest()
	{
		// a.getPriorityValue() - b.getPriorityValue() != 0
		{
			Class<?> implementee = SortByStaticPriorityTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							SortByStaticPriorityTest.Implementor0.class,
							SortByStaticPriorityTest.Implementor1.class,
							SortByStaticPriorityTest.Implementor2.class,
							SortByStaticPriorityTest.Implementor3.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo[] implementMethodInfos = implementInfo
					.getImplementMethodInfos();

			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			this.cachedImplementeeMethodInvocationFactory.sortByStaticPriority(
					implementation, implementInfo, invocationParamTypes,
					implementMethodInfos);

			assertEquals(4, implementMethodInfos.length);
			assertEquals(SortByStaticPriorityTest.Implementor2.class,
					implementMethodInfos[0].getImplementor());
			assertEquals(SortByStaticPriorityTest.Implementor3.class,
					implementMethodInfos[1].getImplementor());
			assertEquals(SortByStaticPriorityTest.Implementor1.class,
					implementMethodInfos[2].getImplementor());
			assertEquals(SortByStaticPriorityTest.Implementor0.class,
					implementMethodInfos[3].getImplementor());
		}

		// a.getPriorityValue() - b.getPriorityValue() == 0
		{
			Class<?> implementee = SortByStaticPriorityTest.Implementee.class;
			Implementation<?> implementation = this.implementationResolver
					.resolve(implementee,
							SortByStaticPriorityTest.Implementor0.class,
							SortByStaticPriorityTest.Implementor1.class);
			Method implementeeMethod = getMethodByName(implementee, "plus");
			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);
			ImplementMethodInfo[] implementMethodInfos = implementInfo
					.getImplementMethodInfos();

			Class<?>[] invocationParamTypes = new Class<?>[] { Integer.class,
					Integer.class };

			this.cachedImplementeeMethodInvocationFactory.sortByStaticPriority(
					implementation, implementInfo, invocationParamTypes,
					implementMethodInfos);

			assertEquals(2, implementMethodInfos.length);
			assertEquals(SortByStaticPriorityTest.Implementor1.class,
					implementMethodInfos[0].getImplementor());
			assertEquals(SortByStaticPriorityTest.Implementor0.class,
					implementMethodInfos[1].getImplementor());
		}
	}

	public static class SortByStaticPriorityTest
	{
		public static interface Implementee
		{
			Number plus(Number a, Number b);

			Number minus(Number a, Number b);
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			public int plus(Integer a, Integer b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			@Priority(priority = Integer.MAX_VALUE)
			public Number plus(Number a, Number b)
			{
				return 0;
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			@Priority(priority = 1)
			public float plus(Integer a, Integer b)
			{
				return 0;
			}
		}
	}

	@Test
	public void getRandomElementTest()
	{
		// objs == null
		{
			assertNull(this.cachedImplementeeMethodInvocationFactory
					.getRandomElement(null));
		}

		// objs.isEmpty()
		{
			assertNull(this.cachedImplementeeMethodInvocationFactory
					.getRandomElement(new HashSet<Object>()));
		}

		// objs instanceof List<?>
		{
			Integer i0 = new Integer(0);
			Integer i1 = new Integer(0);
			List<Object> list = new ArrayList<Object>();
			list.add(i0);
			list.add(i1);

			assertTrue(i0 == this.cachedImplementeeMethodInvocationFactory
					.getRandomElement(list));
		}

		// for (Object obj : objs)
		{
			Integer i0 = new Integer(0);
			Integer i1 = new Integer(0);

			Set<Object> set = new HashSet<Object>();

			set.add(i0);
			set.add(i1);

			Object re = this.cachedImplementeeMethodInvocationFactory
					.getRandomElement(set);

			assertNotNull(re);
		}
	}

	protected static class CountImplementorBeanFactory
			extends SimpleImplementorBeanFactory
	{
		private Map<Class<?>, Integer> counts = new HashMap<Class<?>, Integer>();

		public CountImplementorBeanFactory()
		{
			super();
		}

		public CountImplementorBeanFactory(
				Map<Class<?>, ? extends Collection<?>> implementorBeansMap)
		{
			super(implementorBeansMap);
		}

		@Override
		public <T> Collection<T> getImplementorBeans(Class<T> implementor)
		{
			Integer count = getCount(implementor) + 1;
			counts.put(implementor, count);

			return super.getImplementorBeans(implementor);
		}

		public int getCount(Class<?> implementor)
		{
			Integer count = this.counts.get(implementor);

			return (count == null ? 0 : count.intValue());
		}

		public static CountImplementorBeanFactory valueOf(
				Object... implementorBeans)
		{
			Map<Class<?>, List<Object>> implementorBeansMap = new HashMap<Class<?>, List<Object>>();

			for (Object implementorBean : implementorBeans)
			{
				Class<?> myClass = implementorBean.getClass();

				List<Object> myBeanList = implementorBeansMap.get(myClass);

				if (myBeanList == null)
				{
					myBeanList = new ArrayList<Object>(1);
					implementorBeansMap.put(myClass, myBeanList);
				}

				myBeanList.add(implementorBean);
			}

			return new CountImplementorBeanFactory(implementorBeansMap);
		}
	}
}
