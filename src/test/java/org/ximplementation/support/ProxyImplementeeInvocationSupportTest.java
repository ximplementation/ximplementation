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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;
import org.ximplementation.NotImplement;

/**
 * {@linkplain ProxyImplementeeInvocationSupport} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-12-15
 *
 */
public class ProxyImplementeeInvocationSupportTest extends AbstractTestSupport
{
	private ImplementationResolver implementationResolver;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

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
	public void invokeTest() throws Throwable
	{
		Implementation<Implementee> implementation = this.implementationResolver
				.resolve(Implementee.class, Implementor0.class,
						Implementor1.class);
		ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
				.valueOf(new Implementor0(), new Implementor1());
		ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory = new CachedImplementeeMethodInvocationFactory();

		ProxyImplementeeInvocationSupport support = new ProxyImplementeeInvocationSupport(
				implementation, implementorBeanFactory,
				implementeeMethodInvocationFactory);

		Method implementeeMethod = getMethodByName(Implementee.class, "plus");

		Number re = (Number) support.invoke(implementeeMethod,
				new Object[] { 1, 2 });

		Assert.assertEquals(Implementor1.RE, re);
	}

	@Test
	public void invokeTest_throw() throws Throwable
	{
		Implementation<Implementee> implementation = this.implementationResolver
				.resolve(Implementee.class, Implementor0.class,
						Implementor1.class);
		ImplementorBeanFactory implementorBeanFactory = SimpleImplementorBeanFactory
				.valueOf(new Implementor0(), new Implementor1());
		ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory = new CachedImplementeeMethodInvocationFactory();

		ProxyImplementeeInvocationSupport support = new ProxyImplementeeInvocationSupport(
				implementation, implementorBeanFactory,
				implementeeMethodInvocationFactory);

		Method implementeeMethod = getMethodByName(Implementee.class, "minus");

		expectedException.expect(UnsupportedOperationException.class);
		expectedException.expectMessage("No valid implement method found");

		Number re = (Number) support.invoke(implementeeMethod,
				new Object[] { 1, 2 });

		Assert.assertEquals(Implementor1.RE, re);
	}

	public static interface Implementee
	{
		Number plus(Number a, Number b);

		Number minus(Number a, Number b);
	}

	public static class Implementor0 implements Implementee
	{
		public static final int RE = 1;

		@Override
		public Number plus(Number a, Number b)
		{
			return RE;
		}

		@NotImplement
		@Override
		public Number minus(Number a, Number b)
		{
			return null;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor1
	{
		public static final int RE = 2;

		@Implement
		public Number plus(Integer a, Integer b)
		{
			return RE;
		}
	}

	@Test
	public void isEqualsMethodTest()
	{
		ProxyImplementeeInvocationSupport support = new ProxyImplementeeInvocationSupport();

		Assert.assertFalse(support.isEqualsMethod(null));
		Assert.assertFalse(support.isEqualsMethod(getMethodByName(
				IsEqualsMethodTest.class, "equalsTo")));
		Assert.assertFalse(support.isEqualsMethod(
				getMethodByNameAndType(IsEqualsMethodTest.class, "equals",
						String.class)));
		Assert.assertTrue(support.isEqualsMethod(getMethodByNameAndType(
				IsEqualsMethodTest.class, "equals", Object.class)));
		Assert.assertTrue(support.isEqualsMethod(getMethodByNameAndType(
				Object.class, "equals", Object.class)));
	}

	protected static class IsEqualsMethodTest
	{
		public boolean equals(String s)
		{
			return true;
		}

		public boolean equalsTo(String s)
		{
			return true;
		}

		@Override
		public boolean equals(Object obj)
		{
			return true;
		}
	}

	@Test
	public void isHashCodeMethodTest()
	{
		ProxyImplementeeInvocationSupport support = new ProxyImplementeeInvocationSupport();

		Assert.assertFalse(support.isHashCodeMethod(null));
		Assert.assertFalse(support.isHashCodeMethod(
				getMethodByName(IsHashCodeMethodTest.class, "hashCodeOf")));
		Assert.assertFalse(support.isHashCodeMethod(getMethodByNameAndType(
				IsHashCodeMethodTest.class, "hashCode", int.class)));
		Assert.assertTrue(support.isHashCodeMethod(getMethodByNameAndType(
				IsHashCodeMethodTest.class, "hashCode", new Class[] {})));
		Assert.assertTrue(support.isHashCodeMethod(
				getMethodByNameAndType(Object.class, "hashCode",
						new Class[] {})));
	}

	protected static class IsHashCodeMethodTest
	{
		public int hashCodeOf()
		{
			return 1;
		}

		public int hashCode(int a)
		{
			return 1;
		}

		@Override
		public int hashCode()
		{
			return 1;
		}
	}

	@Test
	public void isToStringMethodTest()
	{
		ProxyImplementeeInvocationSupport support = new ProxyImplementeeInvocationSupport();

		Assert.assertFalse(support.isToStringMethod(null));
		Assert.assertFalse(support.isToStringMethod(
				getMethodByName(IsToStringMethodTest.class, "toStringOf")));
		Assert.assertFalse(support.isToStringMethod(getMethodByNameAndType(
				IsToStringMethodTest.class, "toString", int.class)));
		Assert.assertTrue(support.isToStringMethod(getMethodByNameAndType(
				IsToStringMethodTest.class, "toString", new Class[] {})));
		Assert.assertTrue(support.isToStringMethod(getMethodByNameAndType(
				Object.class, "toString", new Class[] {})));
	}

	protected static class IsToStringMethodTest
	{
		public String toStringOf()
		{
			return "";
		}

		public String toString(int a)
		{
			return "";
		}

		@Override
		public String toString()
		{
			return "";
		}
	}
}
