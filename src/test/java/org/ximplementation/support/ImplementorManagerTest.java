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

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implementor;

/**
 * {@linkplain ImplementorManager} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-29
 *
 */
public class ImplementorManagerTest extends AbstractTestSupport
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
	public void addImplementorTest()
	{
		// onlyInterfaceForLang = true
		{
			ImplementorManager implementorManager = new ImplementorManager();

			implementorManager.add(AddImplementorTest.Implementor0.class);
			implementorManager
					.add(AddImplementorTest.Implementor1.class);

			HashSet<Class<?>> expected = new HashSet<Class<?>>();
			expected.add(AddImplementorTest.Implementee0.class);
			expected.add(AddImplementorTest.Implementee1.class);
			expected.add(AddImplementorTest.Implementee2.class);

			HashSet<Class<?>> actual = new HashSet<Class<?>>(
					implementorManager.getImplementees());

			assertEquals(expected, actual);

			{
				HashSet<Class<?>> expectedImplementors = new HashSet<Class<?>>();
				expectedImplementors.add(AddImplementorTest.Implementor0.class);
				expectedImplementors.add(AddImplementorTest.Implementor1.class);

				assertEquals(expectedImplementors,
						implementorManager.get(
								AddImplementorTest.Implementee0.class));
			}

			{
				HashSet<Class<?>> expectedImplementors = new HashSet<Class<?>>();
				expectedImplementors.add(AddImplementorTest.Implementor0.class);
				expectedImplementors.add(AddImplementorTest.Implementor1.class);

				assertEquals(expectedImplementors,
						implementorManager.get(
								AddImplementorTest.Implementee1.class));
			}

			{
				HashSet<Class<?>> expectedImplementors = new HashSet<Class<?>>();
				expectedImplementors.add(AddImplementorTest.Implementor0.class);

				assertEquals(expectedImplementors,
						implementorManager.get(
								AddImplementorTest.Implementee2.class));
			}
		}

		// onlyInterfaceForLang = false
		{
			ImplementorManager implementorManager = new ImplementorManager();
			implementorManager.setOnlyInterfaceForLang(false);

			implementorManager
					.add(AddImplementorTest.Implementor0.class);
			implementorManager
					.add(AddImplementorTest.Implementor1.class);

			HashSet<Class<?>> expected = new HashSet<Class<?>>();
			expected.add(AddImplementorTest.Implementee0.class);
			expected.add(AddImplementorTest.Implementee1.class);
			expected.add(AddImplementorTest.Implementee2.class);
			expected.add(AddImplementorTest.AbstractImplementee.class);

			HashSet<Class<?>> actual = new HashSet<Class<?>>(
					implementorManager.getImplementees());

			assertEquals(expected, actual);

			{
				HashSet<Class<?>> expectedImplementors = new HashSet<Class<?>>();
				expectedImplementors.add(AddImplementorTest.Implementor0.class);
				expectedImplementors.add(AddImplementorTest.Implementor1.class);

				assertEquals(expectedImplementors,
						implementorManager.get(
								AddImplementorTest.Implementee0.class));
			}

			{
				HashSet<Class<?>> expectedImplementors = new HashSet<Class<?>>();
				expectedImplementors.add(AddImplementorTest.Implementor0.class);
				expectedImplementors.add(AddImplementorTest.Implementor1.class);

				assertEquals(expectedImplementors,
						implementorManager.get(
								AddImplementorTest.Implementee1.class));
			}

			{
				HashSet<Class<?>> expectedImplementors = new HashSet<Class<?>>();
				expectedImplementors.add(AddImplementorTest.Implementor0.class);

				assertEquals(expectedImplementors,
						implementorManager.get(
								AddImplementorTest.Implementee2.class));
			}

			{
				HashSet<Class<?>> expectedImplementors = new HashSet<Class<?>>();
				expectedImplementors.add(AddImplementorTest.Implementor0.class);

				assertEquals(expectedImplementors,
						implementorManager.get(
								AddImplementorTest.AbstractImplementee.class));
			}
		}
	}

	public static class AddImplementorTest
	{
		public static interface Implementee0
		{

		}

		public static interface Implementee1 extends Implementee0
		{
		}

		public static interface Implementee2
		{
		}

		public static abstract class AbstractImplementee
		{
		}

		@Implementor(Implementee2.class)
		public static class Implementor0 extends AbstractImplementee
				implements Implementee1
		{
		}

		public static class Implementor1 implements Implementee1
		{
		}
	}

	@Test
	public void resolveImplementeesTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		// onlyInterfaceForLang = true
		{
			HashSet<Class<?>> actual = new HashSet<Class<?>>();

			implementorManager.resolveImplementees(
					ResolveImplementeesTest.Implementor0.class, actual,
					true);

			HashSet<Class<?>> expected = new HashSet<Class<?>>();
			expected.add(ResolveImplementeesTest.Implementee0.class);
			expected.add(ResolveImplementeesTest.Implementee1.class);
			expected.add(ResolveImplementeesTest.Implementee2.class);

			assertEquals(expected, actual);
		}

		// onlyInterfaceForLang = false
		{
			HashSet<Class<?>> actual = new HashSet<Class<?>>();

			implementorManager.resolveImplementees(
					ResolveImplementeesTest.Implementor0.class, actual, false);

			HashSet<Class<?>> expected = new HashSet<Class<?>>();
			expected.add(ResolveImplementeesTest.Implementee0.class);
			expected.add(ResolveImplementeesTest.Implementee1.class);
			expected.add(ResolveImplementeesTest.Implementee2.class);
			expected.add(ResolveImplementeesTest.AbstractImplementee.class);

			assertEquals(expected, actual);
		}
	}

	public static class ResolveImplementeesTest
	{
		public static interface Implementee0
		{

		}

		public static interface Implementee1 extends Implementee0
		{
		}

		public static interface Implementee2
		{
		}

		public static abstract class AbstractImplementee
		{
		}

		@Implementor(Implementee2.class)
		public static class Implementor0 extends AbstractImplementee
				implements Implementee1
		{
		}
	}

	@Test
	public void getDiectLangSuperClassesTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		// onlyInterface = true
		{
			Class<?>[] actual = implementorManager
					.getDiectLangSuperClasses(
					GetDiectLangSuperClassesTest.Implementor0.class, true);

			Class<?>[] expected = {
					GetDiectLangSuperClassesTest.Implementee1.class,
					GetDiectLangSuperClassesTest.Implementee2.class };

			assertArrayEquals(expected, actual);
		}

		// onlyInterface = false
		{
			Class<?>[] actual = implementorManager.getDiectLangSuperClasses(
					GetDiectLangSuperClassesTest.Implementor0.class, false);

			Class<?>[] expected = {
					GetDiectLangSuperClassesTest.Implementee1.class,
					GetDiectLangSuperClassesTest.Implementee2.class,
					GetDiectLangSuperClassesTest.AbstractImplementee.class };

			assertArrayEquals(expected, actual);
		}
	}

	public static class GetDiectLangSuperClassesTest
	{
		public static interface Implementee0
		{

		}

		public static interface Implementee1 extends Implementee0
		{
		}

		public static interface Implementee2
		{
		}

		public static abstract class AbstractImplementee
		{
		}

		public static class Implementor0 extends AbstractImplementee
				implements Implementee1, Implementee2
		{
		}
	}

	@Test
	public void getAnnotationImplementeesTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		// implementorAno != null
		{
			Class<?>[] actual = implementorManager.getAnnotationImplementees(
					GetAnnotationImplementeesTest.Implementor0.class);

			Class<?>[] expected = {
					GetAnnotationImplementeesTest.Implementee0.class,
					GetAnnotationImplementeesTest.Implementee1.class };

			assertArrayEquals(expected, actual);
		}

		// Arrays.equals(DEFAULT_IMPLEMENTOR_INTERFACECLASSES, annoImplementees)
		{
			Class<?>[] actual = implementorManager.getAnnotationImplementees(
					GetAnnotationImplementeesTest.Implementor1.class);

			Class<?>[] expected = {};

			assertArrayEquals(expected, actual);
		}

		// implementorAno == null
		{
			Class<?>[] actual = implementorManager.getAnnotationImplementees(
					GetAnnotationImplementeesTest.Implementor2.class);

			Class<?>[] expected = {};

			assertArrayEquals(expected, actual);
		}
	}

	public static class GetAnnotationImplementeesTest
	{
		public static interface Implementee0
		{

		}

		public static interface Implementee1
		{

		}

		@Implementor({ Implementee0.class, Implementee1.class })
		public static class Implementor0
		{
		}

		@Implementor(Object.class)
		public static class Implementor1
		{
		}

		public static class Implementor2
		{
		}

	}
}
