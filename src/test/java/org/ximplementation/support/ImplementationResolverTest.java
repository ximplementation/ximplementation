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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.NotImplement;
import org.ximplementation.ParamIndex;
import org.ximplementation.Priority;
import org.ximplementation.Refered;
import org.ximplementation.Validity;

/**
 * {@linkplain ImplementationResolver} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-5
 *
 */
public class ImplementationResolverTest extends AbstractTestSupport
{
	private ImplementationResolver implementationResolver;

	@Before
	public void setUp() throws Exception
	{
		this.implementationResolver = new ImplementationResolver();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void resolveTestArray()
	{
		Class<?> implementee = ResolveTestArray.Implementee.class;

		Implementation implementation = this.implementationResolver
				.resolve(implementee, ResolveTestArray.Implementor0.class,
						ResolveTestArray.Implementor1.class);

		assertEquals(ResolveTestArray.Implementee.class,
				implementation.getImplementee());
		assertEquals(1, implementation.getImplementInfos().length);

		Set<Class<?>> expectedImplementors = new HashSet<Class<?>>();
		expectedImplementors.add(ResolveTestArray.Implementor0.class);
		expectedImplementors.add(ResolveTestArray.Implementor1.class);

		Set<Class<?>> actualImplementors = new HashSet<Class<?>>();
		for (ImplementMethodInfo implementMethodInfo : implementation
				.getImplementInfos()[0].getImplementMethodInfos())
		{
			actualImplementors.add(implementMethodInfo.getImplementor());
		}

		assertEquals(expectedImplementors, actualImplementors);
	}

	@Test
	public void resolveTestSet()
	{
		Class<?> implementee = ResolveTestArray.Implementee.class;

		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(ResolveTestArray.Implementor0.class);
		implementors.add(ResolveTestArray.Implementor1.class);

		Implementation implementation = this.implementationResolver.resolve(
				implementee, implementors);

		assertEquals(ResolveTestArray.Implementee.class,
				implementation.getImplementee());
		assertEquals(1, implementation.getImplementInfos().length);

		Set<Class<?>> actualImplementors = new HashSet<Class<?>>();
		for (ImplementMethodInfo implementMethodInfo : implementation
				.getImplementInfos()[0].getImplementMethodInfos())
		{
			actualImplementors.add(implementMethodInfo.getImplementor());
		}

		assertEquals(implementors, actualImplementors);
	}

	public static class ResolveTestArray
	{
		public static class Implementee
		{
			public void handle()
			{
			}
		}

		public static class Implementor0 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}

		public static class Implementor1 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}
	}

	@Test
	public void doResolveTest()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(DoResolveTest.Implementor0.class);
		implementors.add(DoResolveTest.Implementor1.class);

		Implementation implementation = this.implementationResolver
				.doResolve(DoResolveTest.Implementee.class, implementors);
		
		assertEquals(DoResolveTest.Implementee.class,
				implementation.getImplementee());
		assertEquals(1, implementation.getImplementInfos().length);
		assertEquals("handle", implementation.getImplementInfos()[0]
				.getImplementeeMethod().getName());
	}

	public static class DoResolveTest
	{
		public static class Implementee
		{
			public static void notImplementable()
			{
			}

			public void handle()
			{
			}
		}

		public static class Implementor0 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}

		public static class Implementor1 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}
	}

	@Test
	public void resolveImplementInfoTest()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(ResolveImplementInfoTest.Implementor0.class);
		implementors.add(ResolveImplementInfoTest.Implementor1.class);
		implementors.add(ResolveImplementInfoTest.NotImplementor.class);

		Method[] implementeeMethods = ResolveImplementInfoTest.Implementee.class.getMethods();
		Method implementeeMethod = getMethodByName(implementeeMethods,
				"handle");
		
		ImplementInfo implementInfo = this.implementationResolver.resolveImplementInfo(
						ResolveImplementInfoTest.Implementee.class,
						implementeeMethods,
						implementeeMethod,
						implementors);

		assertEquals(implementeeMethod, implementInfo.getImplementeeMethod());
		assertEquals(2, implementInfo.getImplementMethodInfos().length);
	}

	public static class ResolveImplementInfoTest
	{
		public static class Implementee
		{
			public void handle()
			{
			}
		}

		public static class Implementor0 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}

		public static class Implementor1 extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}

		public static class NotImplementor
		{
			public void handle()
			{
			}
		}
	}

	@Test
	public void resolveImplementMethodInfoTest()
	{
		Class<?> implementee = ResolveImplementMethodInfoTest.Implementee.class;
		Method[] implementeeMethods = implementee.getMethods();
		Method implementeeMethod = getMethodByName(implementeeMethods,
				"handle");
		Class<?> implementor = ResolveImplementMethodInfoTest.Implementor.class;

		Collection<ImplementMethodInfo> implementMethodInfos = this.implementationResolver
				.resolveImplementMethodInfo(implementee, implementeeMethods,
						implementeeMethod, implementor);

		assertEquals(2, implementMethodInfos.size());

		Set<Method> expectMethods = new HashSet<Method>();
		expectMethods.add(getMethodByName(implementor, "handle"));
		expectMethods.add(getMethodByName(implementor, "handleAnother"));

		Set<Method> actualMethods = new HashSet<Method>();
		for (ImplementMethodInfo implementMethodInfo : implementMethodInfos)
		{
			actualMethods.add(implementMethodInfo.getImplementMethod());
		}

		assertEquals(expectMethods, actualMethods);
	}

	public static class ResolveImplementMethodInfoTest
	{
		public static class Implementee
		{
			public void handle()
			{
			}
		}

		public static class Implementor extends Implementee
		{
			@Override
			public void handle()
			{
			}

			@Implement("handle")
			public void handleAnother()
			{
			}

			public void notImplementHandle()
			{
			}
		}
	}

	@Test
	public void buildImplementMethodInfoTest()
	{
		Class<?> implementee = BuildImplementMethodInfoTest.Implementee.class;
		Method[] implementeeMethods = implementee.getMethods();
		Method implementeeMethod = getMethodByName(implementeeMethods,
				"handle");
		Class<?> implementor = BuildImplementMethodInfoTest.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = this.implementationResolver
				.buildImplementMethodInfo(implementee, implementeeMethod,
						implementor, implementMethod);

		assertEquals(implementor, implementMethodInfo.getImplementor());
		assertEquals(implementMethod, implementMethodInfo.getImplementMethod());
	}

	public static class BuildImplementMethodInfoTest
	{
		public static class Implementee
		{
			public void handle()
			{
			}
		}

		public static class Implementor extends Implementee
		{
			@Override
			public void handle()
			{
			}
		}
	}

	@Test
	public void resolveImplementMethodInfoPropertiesTest()
	{
		Class<?> implementee = ResolveImplementMethodInfoPropertiesTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "handle");
		Class<?> implementor = ResolveImplementMethodInfoPropertiesTest.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				implementor, implementMethod);

		this.implementationResolver.resolveImplementMethodInfoProperties(
				implementee, implementeeMethod, implementMethodInfo);

		assertNotNull(implementMethodInfo.getParamTypes());
		assertNotNull(implementMethodInfo.getGenericParamTypes());
		assertNotNull(implementMethodInfo.getParamIndexes());
		assertNotNull(implementMethodInfo.getValidityMethod());
		assertNotNull(implementMethodInfo.getValidityParamIndexes());
		assertNotNull(implementMethodInfo.getPriorityMethod());
		assertEquals(1, implementMethodInfo.getPriorityValue());
		assertNotNull(implementMethodInfo.getPriorityParamIndexes());
	}

	public static class ResolveImplementMethodInfoPropertiesTest
	{
		public static class Implementee
		{
			public void handle(int a)
			{
			}
		}

		public static class Implementor extends Implementee
		{
			@Override
			@Validity("isValid")
			@Priority(value = 1, method = "getPriority")
			public void handle(int a)
			{
			}

			public boolean isValid(int a)
			{
				return true;
			}

			public int getPriority(int a)
			{
				return 1;
			}
		}
	}

	@Test
	public void resolveImplementMethodInfoParamTypesTest()
	{
		Class<?> implementee = ResolveImplementMethodInfoPropertiesTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "handle");
		Class<?> implementor = ResolveImplementMethodInfoPropertiesTest.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				implementor, implementMethod);

		this.implementationResolver.resolveImplementMethodInfoParamTypes(
				implementee, implementeeMethod, implementMethodInfo);

		assertArrayEquals(implementeeMethod.getParameterTypes(),
				implementMethodInfo.getParamTypes());
	}

	@Test
	public void resolveImplementMethodInfoGenericParamTypesTest()
	{
		Class<?> implementee = ResolveImplementMethodInfoPropertiesTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "handle");
		Class<?> implementor = ResolveImplementMethodInfoPropertiesTest.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				implementor, implementMethod);

		this.implementationResolver.resolveImplementMethodInfoGenericParamTypes(
				implementee, implementeeMethod, implementMethodInfo);

		assertArrayEquals(implementeeMethod.getGenericParameterTypes(),
				implementMethodInfo.getGenericParamTypes());

	}

	@Test
	public void resolveImplementMethodInfoParamIndexesTest()
	{
		Class<?> implementee = ResolveImplementMethodInfoPropertiesTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "handle");
		Class<?> implementor = ResolveImplementMethodInfoPropertiesTest.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				implementor, implementMethod);

		this.implementationResolver.resolveImplementMethodInfoParamIndexes(
				implementee, implementeeMethod, implementMethodInfo);

		assertArrayEquals(new int[] { 0 },
				implementMethodInfo.getParamIndexes());
	}

	@Test
	public void resolveImplementMethodInfoValidityTest()
	{
		Class<?> implementee = ResolveImplementMethodInfoPropertiesTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "handle");
		Class<?> implementor = ResolveImplementMethodInfoPropertiesTest.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				implementor, implementMethod);

		this.implementationResolver.resolveImplementMethodInfoValidity(
				implementee, implementeeMethod, implementMethodInfo);

		assertEquals(getMethodByName(implementor, "isValid"),
				implementMethodInfo.getValidityMethod());
		assertNotNull(implementMethodInfo.getValidityParamIndexes());
	}

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void resolveImplementMethodInfoValidityTestThrow()
	{
		Class<?> implementee = ResolveImplementMethodInfoValidityThrow.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "handle");
		Class<?> implementor = ResolveImplementMethodInfoValidityThrow.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				implementor, implementMethod);

		expectedException.expect(ImplementationResolveException.class);

		this.implementationResolver.resolveImplementMethodInfoValidity(
				implementee, implementeeMethod, implementMethodInfo);
	}

	public static class ResolveImplementMethodInfoValidityThrow
	{
		public static class Implementee
		{
			public void handle(int a)
			{
			}
		}

		public static class Implementor extends Implementee
		{
			@Override
			@Validity("isValid")
			public void handle(int a)
			{
			}
		}
	}

	@Test
	public void resolveImplementMethodInfoPriorityTest()
	{
		Class<?> implementee = ResolveImplementMethodInfoPropertiesTest.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "handle");
		Class<?> implementor = ResolveImplementMethodInfoPropertiesTest.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				implementor, implementMethod);

		this.implementationResolver.resolveImplementMethodInfoPriority(
				implementee, implementeeMethod, implementMethodInfo);

		assertEquals(getMethodByName(implementor, "getPriority"),
				implementMethodInfo.getPriorityMethod());
		assertEquals(1, implementMethodInfo.getPriorityValue());
		assertNotNull(implementMethodInfo.getPriorityParamIndexes());
	}

	@Test
	public void resolveImplementMethodInfoPriorityTestThrow()
	{
		Class<?> implementee = ResolveImplementMethodInfoPriorityThrow.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "handle");
		Class<?> implementor = ResolveImplementMethodInfoPriorityThrow.Implementor.class;
		Method implementMethod = getMethodByName(implementor, "handle");

		ImplementMethodInfo implementMethodInfo = new ImplementMethodInfo(
				implementor, implementMethod);

		expectedException
				.expect(ImplementationResolveException.class);

		this.implementationResolver.resolveImplementMethodInfoPriority(
				implementee, implementeeMethod, implementMethodInfo);
	}

	public static class ResolveImplementMethodInfoPriorityThrow
	{
		public static class Implementee
		{
			public void handle(int a)
			{
			}
		}

		public static class Implementor extends Implementee
		{
			@Override
			@Priority(method = "getPriority")
			public void handle(int a)
			{
			}
		}
	}

	@Test
	public void getCandicateImplementeeMethodsTest()
	{
		Class<?> implementee = GetCandicateImplementeeMethodsTest.class;

		Method[] methods = this.implementationResolver
				.getCandicateImplementeeMethods(implementee);

		assertEquals(implementee.getMethods().length, methods.length);

		Set<Method> expected = new HashSet<Method>(
				Arrays.asList(implementee.getMethods()));
		Set<Method> actual = new HashSet<Method>(Arrays.asList(methods));
		assertEquals(expected, actual);
	}

	public static class GetCandicateImplementeeMethodsTest
	{
		public void implementeeMethod()
		{
		}
	}

	@Test
	public void getCandicateImplementMethodsTest()
	{
		Class<?> implementor = GetCandicateImplementMethodsTest.class;

		Method[] methods = this.implementationResolver
				.getCandicateImplementMethods(implementor);

		assertEquals(implementor.getMethods().length, methods.length);

		Set<Method> expected = new HashSet<Method>(
				Arrays.asList(implementor.getMethods()));
		Set<Method> actual = new HashSet<Method>(Arrays.asList(methods));
		assertEquals(expected, actual);
	}

	public static class GetCandicateImplementMethodsTest
	{
		public void implementMethod()
		{
		}
	}

	@Test
	public void isImplementeeMethodTest()
	{
		Class<?> implementee = IsImplementeeMethodTest.class;

		assertFalse(this.implementationResolver.isImplementeeMethod(implementee,
				getMethodByName(implementee, "notImplementeeMethodStatic")));

		assertTrue(this.implementationResolver.isImplementeeMethod(implementee,
				getMethodByName(implementee, "implementeeMethod")));

		assertFalse(this.implementationResolver.isImplementeeMethod(implementee,
				getMethodByName(implementee, "notImplementeeMethodProtected")));

		assertFalse(this.implementationResolver.isImplementeeMethod(implementee,
				getMethodByName(implementee, "notImplementeeMethodPrivate")));

		assertFalse(this.implementationResolver.isImplementeeMethod(implementee,
				getMethodByName(implementee, "notImplementeeMethodDefault")));

		assertFalse(this.implementationResolver.isImplementeeMethod(implementee,
				getMethodByName(implementee, "hashCode")));
	}

	public static class IsImplementeeMethodTest
	{
		public static void notImplementeeMethodStatic()
		{
		}

		public void implementeeMethod()
		{
		}

		protected void notImplementeeMethodProtected()
		{
		}

		@SuppressWarnings("unused")
		private void notImplementeeMethodPrivate()
		{
		}

		void notImplementeeMethodDefault()
		{
		}
	}

	@Test
	public void isImplementorTest()
	{
		Class<?> implementee = IsImplementorTest.Implementee.class;

		assertTrue(this.implementationResolver.isImplementor(implementee,
				IsImplementorTest.Implementee0.class));
		assertTrue(this.implementationResolver.isImplementor(implementee,
				IsImplementorTest.Implementor1.class));
		assertTrue(this.implementationResolver.isImplementor(implementee,
				IsImplementorTest.Implementor2.class));
		assertFalse(this.implementationResolver.isImplementor(implementee,
				IsImplementorTest.NotImplementor.class));
	}

	public static class IsImplementorTest
	{
		public interface Implementee
		{
		}

		public interface Implementee0 extends Implementee
		{
		}

		public static class Implementor0 implements Implementee
		{
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
		}

		@Implementor({ Implementee0.class })
		public static class Implementor2
		{
		}

		public static class NotImplementor
		{
		}
	}

	@Test
	public void isImplementMethodTest()
	{
		Class<?> implementee = IsImplementMethod.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "plus");
		String implementeeMethodName = implementeeMethod.getName();
		String implementeeMethodSignature = implementeeMethod.toString();
		String implementeeMethodRefered = "plus-ref";

		assertFalse(this.implementationResolver.isImplementMethod(implementee,
				implementeeMethod, implementeeMethodName,
				implementeeMethodSignature, implementeeMethodRefered,
				IsImplementMethod.Implementor0.class, getMethodByName(
						IsImplementMethod.Implementor0.class, "plusStatic")));

		assertTrue(this.implementationResolver.isImplementMethod(implementee,
				implementeeMethod, implementeeMethodName,
				implementeeMethodSignature, implementeeMethodRefered,
				IsImplementMethod.Implementor1.class,
				getMethodByName(IsImplementMethod.Implementor1.class, "plus")));

		assertTrue(this.implementationResolver.isImplementMethod(implementee,
				implementeeMethod, implementeeMethodName,
				implementeeMethodSignature, implementeeMethodRefered,
				IsImplementMethod.Implementor2.class, getMethodByName(
						IsImplementMethod.Implementor2.class, "myPlus")));

		assertTrue(this.implementationResolver.isImplementMethod(implementee,
				implementeeMethod, implementeeMethodName,
				implementeeMethodSignature, implementeeMethodRefered,
				IsImplementMethod.Implementor4.class, getMethodByName(
						IsImplementMethod.Implementor4.class, "myPlus")));

		assertTrue(this.implementationResolver.isImplementMethod(implementee,
				implementeeMethod, implementeeMethodName,
				implementeeMethodSignature, implementeeMethodRefered,
				IsImplementMethod.Implementor6.class,
				getMethodByName(IsImplementMethod.Implementor6.class, "plus")));

		assertTrue(this.implementationResolver.isImplementMethod(implementee,
				implementeeMethod, implementeeMethodName,
				implementeeMethodSignature, implementeeMethodRefered,
				IsImplementMethod.Implementor7.class,
				getMethodByName(IsImplementMethod.Implementor7.class, "plus")));
	}

	@Test
	public void isImplementMethodTestNotAbleToImplement()
	{
		Class<?> implementee = IsImplementMethod.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "plus");
		String implementeeMethodName = implementeeMethod.getName();
		String implementeeMethodSignature = implementeeMethod.toString();
		String implementeeMethodRefered = "plus-ref";

		expectedException
				.expect(ImplementationResolveException.class);
		expectedException
				.expectMessage("is not able to implement Method");
		
		this.implementationResolver.isImplementMethod(implementee,
				implementeeMethod, implementeeMethodName,
				implementeeMethodSignature, implementeeMethodRefered,
				IsImplementMethod.Implementor3.class, getMethodByName(
						IsImplementMethod.Implementor3.class, "myPlus"));

	}

	@Test
	public void isImplementMethodTestNotAbleToImplement1()
	{
		Class<?> implementee = IsImplementMethod.Implementee.class;
		Method implementeeMethod = getMethodByName(implementee, "plus");
		String implementeeMethodName = implementeeMethod.getName();
		String implementeeMethodSignature = implementeeMethod.toString();
		String implementeeMethodRefered = "plus-ref";

		expectedException.expect(ImplementationResolveException.class);
		expectedException.expectMessage("is not able to implement Method");

		this.implementationResolver.isImplementMethod(implementee,
				implementeeMethod, implementeeMethodName,
				implementeeMethodSignature, implementeeMethodRefered,
				IsImplementMethod.Implementor5.class, getMethodByName(
						IsImplementMethod.Implementor5.class, "myPlus"));
	}

	public static class IsImplementMethod
	{
		public static interface Implementee
		{
			@Refered("plus-ref")
			Number plus(Number a, Number b);
		}

		public static class Implementor0
		{
			public static Number plusStatic(Number a, Number b)
			{
				return null;
			}
		}

		public static class Implementor1
		{
			@Implement
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		public static class Implementor2
		{
			@Implement("plus-ref")
			public Number myPlus(Number a, Number b)
			{
				return null;
			}
		}

		public static class Implementor3
		{
			@Implement("plus-ref")
			public Number myPlus(String a, String b)
			{
				return null;
			}
		}

		public static class Implementor4
		{
			@Implement("public abstract java.lang.Number org.ximplementation.support.ImplementationResolverTest$IsImplementMethod$Implementee.plus(java.lang.Number,java.lang.Number)")
			public Number myPlus(Number a, Number b)
			{
				return null;
			}
		}

		public static class Implementor5
		{
			@Implement("public abstract java.lang.Number org.ximplementation.support.ImplementationResolverTest$IsImplementMethod$Implementee.plus(java.lang.Number,java.lang.Number)")
			public Number myPlus(String a, String b)
			{
				return null;
			}
		}

		public static class Implementor6
		{
			@Implement("plus")
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}

		public static class Implementor7 implements Implementee
		{
			@Override
			public Number plus(Number a, Number b)
			{
				return null;
			}
		}
	}

	@Test
	public void maybeImplementMethodTest()
	{
		Class<?> implement = MaybeImplementMethodTest.class;

		assertFalse(this.implementationResolver.maybeImplementMethod(implement,
				getMethodByName(implement, "notImplementMethodStatic")));

		assertTrue(this.implementationResolver.maybeImplementMethod(implement,
				getMethodByName(implement, "implementMethod")));

		assertFalse(this.implementationResolver.maybeImplementMethod(implement,
				getMethodByName(implement, "notImplementMethodProtected")));

		assertFalse(this.implementationResolver.maybeImplementMethod(implement,
				getMethodByName(implement, "notImplementMethodPrivate")));

		assertFalse(this.implementationResolver.maybeImplementMethod(implement,
				getMethodByName(implement, "notImplementMethodDefault")));

		assertFalse(this.implementationResolver.maybeImplementMethod(implement,
				getMethodByName(implement, "notImplementAnno")));

		assertFalse(this.implementationResolver.maybeImplementMethod(implement,
				getMethodByName(implement, "hashCode")));
	}

	public static class MaybeImplementMethodTest
	{
		public static void notImplementMethodStatic()
		{
		}

		public void implementMethod()
		{
		}

		protected void notImplementMethodProtected()
		{
		}

		@SuppressWarnings("unused")
		private void notImplementMethodPrivate()
		{
		}

		void notImplementMethodDefault()
		{
		}

		@NotImplement
		public void notImplementAnno()
		{
		}
	}

	@Test
	public void isOverridenMethodTest()
	{
		Class<?> implementee = IsOverridenMethodTest.Implementee.class;

		assertFalse(this.implementationResolver.isOverridenMethod(implementee,
				getMethodByName(implementee, "plus"),
				IsOverridenMethodTest.Implementor0.class, getMethodByName(
						IsOverridenMethodTest.Implementor0.class, "myPlus")));

		assertFalse(this.implementationResolver.isOverridenMethod(implementee,
				getMethodByName(implementee, "plus"),
				IsOverridenMethodTest.Implementor1.class, getMethodByName(
						IsOverridenMethodTest.Implementor1.class, "plus")));

		assertFalse(this.implementationResolver.isOverridenMethod(implementee,
				getMethodByName(implementee, "plus"),
				IsOverridenMethodTest.Implementor2.class, getMethodByName(
						IsOverridenMethodTest.Implementor2.class, "plus")));

		assertFalse(this.implementationResolver.isOverridenMethod(implementee,
				getMethodByName(implementee, "plus"),
				IsOverridenMethodTest.Implementor3.class, getMethodByName(
						IsOverridenMethodTest.Implementor3.class, "plus")));

		assertTrue(this.implementationResolver.isOverridenMethod(implementee,
				getMethodByName(implementee, "plus"),
				IsOverridenMethodTest.Implementor4.class, getMethodByName(
						IsOverridenMethodTest.Implementor4.class, "plus")));

		assertTrue(this.implementationResolver.isOverridenMethod(implementee,
				getMethodByName(implementee, "plus"),
				IsOverridenMethodTest.Implementor5.class, getMethodByName(
						IsOverridenMethodTest.Implementor5.class, "plus")));
	}

	public static class IsOverridenMethodTest
	{
		public static interface Implementee
		{
			Integer plus(Integer a, Integer b);
		}

		public static class Implementor0
		{
			public Integer myPlus(Integer a, Integer b)
			{
				return null;
			}
		}

		public static class Implementor1
		{
			public Integer plus(Integer a)
			{
				return null;
			}
		}

		public static class Implementor2
		{
			public Integer plus(Integer a, Number b)
			{
				return null;
			}
		}

		public static class Implementor3
		{
			public String plus(Integer a, Integer b)
			{
				return null;
			}
		}
		
		public static class Implementor4 implements Implementee
		{
			@Override
			public Integer plus(Integer a, Integer b)
			{
				return null;
			}
		}

		public static class Implementor5
		{
			public Integer plus(Integer a, Integer b)
			{
				return null;
			}
		}
	}

	@Test
	public void isInvokeFeasibleMethodTest()
	{
		Class<?> implementee = IsInvokeFeasibleMethodTest.Implementee.class;

		assertFalse(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor0.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor0.class,
								"plus")));

		assertFalse(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor1.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor1.class,
								"plus")));

		assertFalse(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor2.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor2.class,
								"plus")));

		assertFalse(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor3.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor3.class,
								"plus")));

		assertTrue(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor4.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor4.class,
								"plus")));

		assertTrue(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor5.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor5.class,
								"plus")));

		assertTrue(this.implementationResolver.isInvokeFeasibleMethod(
				implementee, getMethodByName(implementee, "plus"),
				IsInvokeFeasibleMethodTest.Implementor6.class,
						getMethodByName(
						IsInvokeFeasibleMethodTest.Implementor6.class,
								"plus")));

		assertTrue(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor7.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor7.class,
								"plus")));

		assertTrue(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor8.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor8.class,
								"plus")));

		assertTrue(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "plus"),
						IsInvokeFeasibleMethodTest.Implementor9.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor9.class,
								"plus")));

		assertTrue(
				this.implementationResolver.isInvokeFeasibleMethod(implementee,
						getMethodByName(implementee, "minus"),
						IsInvokeFeasibleMethodTest.Implementor10.class,
						getMethodByName(
								IsInvokeFeasibleMethodTest.Implementor10.class,
								"minus")));
	}

	public static class IsInvokeFeasibleMethodTest
	{
		public static interface Implementee
		{
			Integer plus(Integer a, Integer b);

			int minus(int a, int b);
		}

		public static class Implementor0
		{
			public Float plus(Integer a, Integer b)
			{
				return null;
			}
		}

		public static class Implementor1
		{
			public Integer plus(Integer a, Integer b, Integer c)
			{
				return null;
			}
		}

		public static class Implementor2
		{
			public Integer plus(@ParamIndex(2) Integer a, Integer b)
			{
				return null;
			}
		}

		public static class Implementor3
		{
			public Integer plus(Integer a, Float b)
			{
				return null;
			}
		}

		public static class Implementor4
		{
			public Integer plus(Integer a, Number b)
			{
				return null;
			}
		}

		public static class Implementor5
		{
			public int plus(Integer a, Number b)
			{
				return 0;
			}
		}

		public static class Implementor6
		{
			public Integer plus(Integer a, int b)
			{
				return null;
			}
		}

		public static class Implementor7
		{
			public Integer plus(Integer a, Number b)
			{
				return null;
			}
		}

		public static class Implementor8
		{
			public Integer plus(Integer a)
			{
				return null;
			}
		}

		public static class Implementor9
		{
			public Integer plus(@ParamIndex(1) Integer b)
			{
				return null;
			}
		}

		public static class Implementor10
		{
			public int minus(int a, int b)
			{
				return 0;
			}
		}
	}

	@Test
	public void findReferedMethodTest()
	{
		assertEquals(getMethodByName(FindReferedMethodTest.Test0.class, "test"), this.implementationResolver.findReferedMethod(
						FindReferedMethodTest.Test0.class, "my-ref",
						Void.class));

		assertEquals(
				getMethodByNameAndType(FindReferedMethodTest.Test1.class,
						"test", int.class),
				this.implementationResolver.findReferedMethod(
						FindReferedMethodTest.Test1.class, "test",
						int.class));

		assertEquals(
				getMethodByNameAndType(FindReferedMethodTest.Test1.class,
						"test", int.class),
				this.implementationResolver.findReferedMethod(
						FindReferedMethodTest.Test1.class, "test",
						Number.class));

		assertEquals(
				getMethodByNameAndType(FindReferedMethodTest.Test2.class,
						"test", int.class),
				this.implementationResolver.findReferedMethod(
						FindReferedMethodTest.Test2.class, "test", int.class));

		assertEquals(
				getMethodByNameAndType(FindReferedMethodTest.Test2.class,
						"test", int.class),
				this.implementationResolver.findReferedMethod(
						FindReferedMethodTest.Test2.class, "test",
						Number.class));
	}

	@Test
	public void findReferedMethodTestNoMethodIsFound()
	{
		expectedException.expect(ImplementationResolveException.class);
		expectedException.expectMessage("No method is found for");

		this.implementationResolver.findReferedMethod(
				FindReferedMethodTest.Test3.class, "test", Number.class);
	}

	@Test
	public void findReferedMethodTestMoreThanOneIsFound()
	{
		expectedException.expect(ImplementationResolveException.class);
		expectedException.expectMessage("More than one");

		this.implementationResolver.findReferedMethod(
				FindReferedMethodTest.Test4.class, "test", int.class);
	}

	public static class FindReferedMethodTest
	{
		public static class Test0
		{
			public void test0()
			{
			}

			@Refered("my-ref")
			public void test()
			{
			}
		}

		public static class Test1
		{
			public void test()
			{
			}

			public int test(int a)
			{
				return 0;
			}
		}

		public static class Test2
		{
			public void test()
			{
			}

			public Integer test(int a)
			{
				return 0;
			}
		}

		public static class Test3
		{
		}

		public static class Test4
		{
			public int test()
			{
				return 0;
			}

			public Integer test(int a)
			{
				return 0;
			}
		}
	}

	@Test
	public void getMethodSignatureTest()
	{
		Method method = getMethodByName(GetMethodSignatureTest.class, "test");
		
		assertEquals(method.toString(),
				this.implementationResolver.getMethodSignature(
				GetMethodSignatureTest.class,
						method));
	}

	public static class GetMethodSignatureTest
	{
		public void test(Integer a, Integer b)
		{
		}
	}

	@Test
	public void getReferedTest()
	{
		assertEquals("test", this.implementationResolver
				.getRefered(getMethodByName(GetReferedTest.class, "test0")));

		assertNull(this.implementationResolver
				.getRefered(getMethodByName(GetReferedTest.class, "test1")));
	}

	public static class GetReferedTest
	{
		@Refered("test")
		public void test0()
		{
		}

		public void test1()
		{
		}
	}

	@Test
	public void getMethodParamIndexesTest()
	{
		assertArrayEquals(new int[] { 0, 1, 2 },
				this.implementationResolver.getMethodParamIndexes(
						GetMethodParamIndexesTest.class, getMethodByName(
								GetMethodParamIndexesTest.class, "test0")));

		assertArrayEquals(new int[] { 1, 0, 2 },
				this.implementationResolver.getMethodParamIndexes(
						GetMethodParamIndexesTest.class, getMethodByName(
								GetMethodParamIndexesTest.class, "test1")));
	}

	@Test
	public void getMethodParamIndexesTestDupliateParamIndex()
	{
		expectedException.expect(ImplementationResolveException.class);
		expectedException
				.expectMessage("parameter index should not be duplicate with");

		this.implementationResolver.getMethodParamIndexes(
				GetMethodParamIndexesTest.class,
				getMethodByName(GetMethodParamIndexesTest.class, "test2"));
	}

	public static class GetMethodParamIndexesTest
	{
		public void test0(int a, int b, int c)
		{
			
		}

		public void test1(@ParamIndex(1) int a, @ParamIndex(0) int b, int c)
		{

		}

		public void test2(@ParamIndex(1) int a, int b, int c)
		{

		}
	}

	@Test
	public void getAnnotationTest()
	{
		assertEquals(GetAnnotationTest.class.getAnnotation(Implementor.class),
				this.implementationResolver.getAnnotation(
						GetAnnotationTest.class, Implementor.class));
	}

	@Implementor(Object.class)
	public static class GetAnnotationTest
	{

	}
}
