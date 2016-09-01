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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;

/**
 * {@linkplain PreparedImplementorBeanFactory} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-9-1
 *
 */
public class PreparedImplementorBeanFactoryTest extends AbstractTestSupport
{
	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void createPreparedImplementorBeanFactoryTestByImplementorSet()
	{
		HashSet<Class<?>> expected = new HashSet<Class<?>>();
		expected.add(
				CreatePreparedImplementorBeanFactoryTestByImplementorSet.Implementor0.class);
		expected.add(
				CreatePreparedImplementorBeanFactoryTestByImplementorSet.Implementor1.class);

		PreparedImplementorBeanFactory factory = new PreparedImplementorBeanFactory(
				expected);

		assertEquals(expected,
				new HashSet<Class<?>>(factory.getAllImplementors()));
	}

	public static class CreatePreparedImplementorBeanFactoryTestByImplementorSet
	{
		public static class Implementor0
		{
		}

		public static class Implementor1
		{
		}
	}

	@Test
	public void createPreparedImplementorBeanFactoryTestByImplementation()
	{
		HashSet<Class<?>> expected = new HashSet<Class<?>>();
		expected.add(
				CreatePreparedImplementorBeanFactoryTestByImplementation.Implementor0.class);
		expected.add(
				CreatePreparedImplementorBeanFactoryTestByImplementation.Implementor1.class);
		expected.add(
				CreatePreparedImplementorBeanFactoryTestByImplementation.Implementor2.class);

		PreparedImplementorBeanFactory factory = new PreparedImplementorBeanFactory(
				new ImplementationResolver().resolve(CreatePreparedImplementorBeanFactoryTestByImplementation.Implementee.class, expected));

		assertEquals(expected,
				new HashSet<Class<?>>(factory.getAllImplementors()));
	}

	public static class CreatePreparedImplementorBeanFactoryTestByImplementation
	{
		public static interface Implementee
		{
			void plus(int a, int b);

			void minus(int a, int b);
		}

		public static class Implementor0 implements Implementee
		{
			@Override
			public void plus(int a, int b)
			{

			}

			@Override
			public void minus(int a, int b)
			{
			}
		}

		public static class Implementor1 implements Implementee
		{
			@Override
			public void plus(int a, int b)
			{

			}

			@Override
			public void minus(int a, int b)
			{
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			public void minus(int a, int b)
			{
			}
		}
	}

	@Test
	public void acceptTestByImplementor()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(
				AcceptTest.Implementor0.class);
		implementors.add(
				AcceptTest.Implementor1.class);

		PreparedImplementorBeanFactory factory = new PreparedImplementorBeanFactory(
				implementors);

		assertTrue(factory.accept(AcceptTest.Implementor0.class));
		assertTrue(factory.accept(AcceptTest.Implementor1.class));
		assertFalse(factory.accept(AcceptTest.Implementor2.class));
	}

	@Test
	public void acceptTestByImplementorBean()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(AcceptTest.Implementor0.class);
		implementors.add(AcceptTest.Implementor1.class);

		PreparedImplementorBeanFactory factory = new PreparedImplementorBeanFactory(
				implementors);

		assertTrue(factory.accept(new AcceptTest.Implementor0()));
		assertTrue(factory.accept(new AcceptTest.Implementor1()));
		assertFalse(factory.accept(new AcceptTest.Implementor2()));
	}

	public static class AcceptTest
	{
		public static class Implementor0
		{
		}

		public static class Implementor1
		{
		}

		public static class Implementor2
		{
		}
	}

	@Test
	public void addImplementorBeanTest()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(AddImplementorBeanTest.Implementor0.class);

		AddImplementorBeanTest.Implementor0 expected0 = new AddImplementorBeanTest.Implementor0();
		AddImplementorBeanTest.Implementor0 expected1 = new AddImplementorBeanTest.Implementor0();

		PreparedImplementorBeanFactory factory = new PreparedImplementorBeanFactory(
				implementors);

		assertTrue(factory
				.addImplementorBean(expected0));
		assertTrue(factory
				.addImplementorBean(expected1));
		assertFalse(factory
				.addImplementorBean(new AddImplementorBeanTest.Implementor1()));

		ArrayList<?> actual = (ArrayList<?>) factory
				.getImplementorBeans(AddImplementorBeanTest.Implementor0.class);

		assertEquals(2, actual.size());
		assertEquals(expected0, actual.get(0));
		assertEquals(expected1, actual.get(1));
	}

	public static class AddImplementorBeanTest
	{
		public static class Implementor0
		{
		}

		public static class Implementor1
		{
		}
	}
}
