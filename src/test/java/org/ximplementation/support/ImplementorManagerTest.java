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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
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
	public void getImplementorsMapTest()
	{
		Map<Class<?>, Set<Class<?>>> implementorsMap = new HashMap<Class<?>, Set<Class<?>>>();

		ImplementorManager implementorManager = new ImplementorManager(
				implementorsMap);

		assertTrue(implementorsMap == implementorManager.getImplementorsMap());
	}

	@Test
	public void setImplementorsMapTest()
	{
		Map<Class<?>, Set<Class<?>>> implementorsMap = new HashMap<Class<?>, Set<Class<?>>>();

		ImplementorManager implementorManager = new ImplementorManager();
		implementorManager.setImplementorsMap(implementorsMap);

		assertTrue(implementorsMap == implementorManager.getImplementorsMap());
	}

	@Test
	public void getAllImplementeesTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		assertTrue(implementorManager.getImplementorsMap()
				.keySet() == implementorManager.getAllImplementees());
	}

	@Test
	public void getTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();
		implementorManager.addFor(Object.class, String.class);

		Set<Class<?>> implementors = implementorManager.get(Object.class);

		assertEquals(1, implementors.size());
		assertTrue(implementors.contains(String.class));
	}

	@Test
	public void addTest_Array()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.add(String.class, Number.class);

		HashSet<Class<?>> expected = new HashSet<Class<?>>();
		expected.add(Object.class);
		expected.add(Serializable.class);
		expected.add(Comparable.class);
		expected.add(CharSequence.class);

		HashSet<Class<?>> actual = new HashSet<Class<?>>(
				implementorManager.getAllImplementees());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void addTest_Collection()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.add(Arrays.asList(String.class, Number.class));

		HashSet<Class<?>> expected = new HashSet<Class<?>>();
		expected.add(Object.class);
		expected.add(Serializable.class);
		expected.add(Comparable.class);
		expected.add(CharSequence.class);

		HashSet<Class<?>> actual = new HashSet<Class<?>>(
				implementorManager.getAllImplementees());

		assertEquals(expected, actual);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void addForTest_Array()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class, String.class, Number.class);

		Set<Class<?>> implementors = implementorManager.get(Object.class);

		assertEquals(2, implementors.size());
		assertThat(implementors,
				(Matcher) Matchers.containsInAnyOrder(String.class,
						Number.class));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void addForTest_Collection()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		Set<Class<?>> implementors = implementorManager.get(Object.class);

		assertEquals(2, implementors.size());
		assertThat(implementors,
				(Matcher) Matchers.containsInAnyOrder(String.class,
						Number.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void removeTest_Array()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.addFor(Serializable.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.addFor(Comparable.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.remove(String.class, Number.class);

		assertEquals(0, implementorManager.get(Object.class).size());
		assertEquals(0, implementorManager.get(Serializable.class).size());
		assertEquals(0, implementorManager.get(Comparable.class).size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void removeTest_Collection()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.addFor(Serializable.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.addFor(Comparable.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.remove(Arrays.asList(String.class, Number.class));

		assertEquals(0, implementorManager.get(Object.class).size());
		assertEquals(0, implementorManager.get(Serializable.class).size());
		assertEquals(0, implementorManager.get(Comparable.class).size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void removeFor_AllImplementors()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.removeFor(Object.class);

		assertNull(implementorManager.get(Object.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void removeFor_ImplementorArray()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.removeFor(Object.class, String.class, Number.class);

		assertEquals(0, implementorManager.get(Object.class).size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void removeFor_ImplementorCollection()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.removeFor(Object.class,
				Arrays.asList(String.class, Number.class));

		assertEquals(0, implementorManager.get(Object.class).size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void hasImplementorTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.addFor(Serializable.class, new Class<?>[0]);

		assertFalse(implementorManager.hasImplementor(Integer.class));
		assertFalse(implementorManager.hasImplementor(Serializable.class));
		assertTrue(implementorManager.hasImplementor(Object.class));
	}

	@Test
	public void doAddTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.doAdd(String.class, Number.class);

		HashSet<Class<?>> expected = new HashSet<Class<?>>();
		expected.add(Object.class);
		expected.add(Serializable.class);
		expected.add(Comparable.class);
		expected.add(CharSequence.class);

		HashSet<Class<?>> actual = new HashSet<Class<?>>(
				implementorManager.getAllImplementees());

		assertEquals(expected, actual);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void doAddForTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.doAddFor(Object.class, String.class, Integer.class);

		Set<Class<?>> implementors = implementorManager.get(Object.class);

		assertEquals(2, implementors.size());
		assertThat(implementors,
				(Matcher) Matchers.containsInAnyOrder(String.class,
						Integer.class));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void doRemoveTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.addFor(Serializable.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.addFor(Comparable.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.doRemove(String.class, Number.class);

		assertEquals(0, implementorManager.get(Object.class).size());
		assertEquals(0, implementorManager.get(Serializable.class).size());
		assertEquals(0, implementorManager.get(Comparable.class).size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void doRemoveForTest()
	{
		ImplementorManager implementorManager = new ImplementorManager();

		implementorManager.addFor(Object.class,
				Arrays.asList(String.class, Number.class));

		implementorManager.doRemoveFor(Object.class, String.class,
				Number.class);

		assertEquals(0, implementorManager.get(Object.class).size());
	}

	@Test
	public void doResolveImplementeesTest()
	{
		Set<Class<?>> implementees = new ImplementorManager()
				.doResolveImplementees(
				ResolveImplementeesTest.Implementor0.class);

		HashSet<Class<?>> actual = new HashSet<Class<?>>();
		actual.addAll(implementees);

		HashSet<Class<?>> expected = new HashSet<Class<?>>();
		expected.add(Object.class);
		expected.add(ResolveImplementeesTest.Implementee0.class);
		expected.add(ResolveImplementeesTest.Implementee1.class);
		expected.add(ResolveImplementeesTest.Implementee2.class);
		expected.add(ResolveImplementeesTest.AbstractImplementee.class);

		assertEquals(expected, actual);
	}

	@Test
	public void resolveImplementeesTest()
	{
		Set<Class<?>> implementees = ImplementorManager.resolveImplementees(
						ResolveImplementeesTest.Implementor0.class);

		HashSet<Class<?>> actual = new HashSet<Class<?>>();
		actual.addAll(implementees);

		HashSet<Class<?>> expected = new HashSet<Class<?>>();
		expected.add(Object.class);
		expected.add(ResolveImplementeesTest.Implementee0.class);
		expected.add(ResolveImplementeesTest.Implementee1.class);
		expected.add(ResolveImplementeesTest.Implementee2.class);
		expected.add(ResolveImplementeesTest.AbstractImplementee.class);
	}

	@Test
	public void resolveImplementeesTest_saveIntoCollection()
	{
		HashSet<Class<?>> actual = new HashSet<Class<?>>();

		ImplementorManager.resolveImplementees(
				ResolveImplementeesTest.Implementor0.class, actual);

		HashSet<Class<?>> expected = new HashSet<Class<?>>();
		expected.add(Object.class);
		expected.add(ResolveImplementeesTest.Implementee0.class);
		expected.add(ResolveImplementeesTest.Implementee1.class);
		expected.add(ResolveImplementeesTest.Implementee2.class);
		expected.add(ResolveImplementeesTest.AbstractImplementee.class);

		assertEquals(expected, actual);
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
		Class<?>[] actual = ImplementorManager.getDiectLangSuperClasses(
				GetDiectLangSuperClassesTest.Implementor0.class);

		Class<?>[] expected = { GetDiectLangSuperClassesTest.Implementee1.class,
				GetDiectLangSuperClassesTest.Implementee2.class,
				GetDiectLangSuperClassesTest.AbstractImplementee.class };

		assertArrayEquals(expected, actual);
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
		// implementorAno != null
		{
			Class<?>[] actual = ImplementorManager.getAnnotationImplementees(
					GetAnnotationImplementeesTest.Implementor0.class);

			Class<?>[] expected = {
					GetAnnotationImplementeesTest.Implementee0.class,
					GetAnnotationImplementeesTest.Implementee1.class };

			assertArrayEquals(expected, actual);
		}

		// Arrays.equals(DEFAULT_IMPLEMENTOR_INTERFACECLASSES, annoImplementees)
		{
			Class<?>[] actual = ImplementorManager.getAnnotationImplementees(
					GetAnnotationImplementeesTest.Implementor1.class);

			Class<?>[] expected = {};

			assertArrayEquals(expected, actual);
		}

		// implementorAno == null
		{
			Class<?>[] actual = ImplementorManager.getAnnotationImplementees(
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
