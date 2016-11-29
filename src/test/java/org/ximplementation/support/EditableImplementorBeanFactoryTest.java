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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@linkplain EditableImplementorBeanFactory} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-9-1
 *
 */
public class EditableImplementorBeanFactoryTest extends AbstractTestSupport
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
	public void addTest()
	{
		Implementor0 expected0 = new Implementor0();
		Implementor0 expected1 = new Implementor0();
	
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();
	
		factory.add(expected0);
		factory.add(expected1);
		factory.add(new Implementor1());
	
		ArrayList<?> actual = (ArrayList<?>) factory
				.getImplementorBeans(Implementor0.class);
	
		assertEquals(2, actual.size());
		assertEquals(expected0, actual.get(0));
		assertEquals(expected1, actual.get(1));
	}

	@Test
	public void addTest_Class_Objects()
	{
		Set<Class<?>> implementors = new HashSet<Class<?>>();
		implementors.add(Implementor0.class);
	
		Implementor0 expected0 = new Implementor0();
		Implementor0 expected1 = new Implementor0();
	
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();
	
		factory.add(Implementor0.class, expected0, expected1);
		factory.add(Implementor1.class, new Implementor1());
	
		ArrayList<?> actual = (ArrayList<?>) factory
				.getImplementorBeans(Implementor0.class);
	
		assertEquals(2, actual.size());
		assertEquals(expected0, actual.get(0));
		assertEquals(expected1, actual.get(1));
	}

	@Test
	public void addTest_Class_Collection()
	{
		Implementor0 expected0 = new Implementor0();
		Implementor0 expected1 = new Implementor0();
	
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();
	
		factory.add(Implementor0.class,
				Arrays.asList(expected0, expected1));
		factory.add(Implementor1.class, Arrays.asList(new Implementor1()));
	
		ArrayList<?> actual = (ArrayList<?>) factory
				.getImplementorBeans(Implementor0.class);
	
		assertEquals(2, actual.size());
		assertEquals(expected0, actual.get(0));
		assertEquals(expected1, actual.get(1));
	}

	@Test
	public void addForTest()
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		assertFalse(factory.addFor(new Implementor0()));

		factory.add(new Implementor0());

		assertTrue(factory.addFor(new Implementor0()));

		assertEquals(2, factory.get(Implementor0.class).size());
	}

	@Test
	public void addForTest_Class_Objects()
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		assertFalse(factory.addFor(Implementor0.class, new Implementor0(),
				new Implementor0()));

		factory.add(Implementor0.class, new Implementor0());

		assertTrue(factory.addFor(Implementor0.class, new Implementor0(),
				new Implementor0()));

		assertEquals(3, factory.get(Implementor0.class).size());
	}

	@Test
	public void addForTest_Class_Collection()
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		assertFalse(factory
				.addFor(Arrays.asList(new Implementor0(), new Implementor0())));

		factory.add(new Implementor0());

		assertTrue(factory
				.addFor(Implementor0.class,
						Arrays.asList(new Implementor0(), new Implementor0())));

		assertEquals(3, factory.get(Implementor0.class).size());
	}

	@Test
	public void removeTest()
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		factory.add(new Integer(1), new Float(1));

		assertEquals(1, factory.get(Integer.class).size());
		assertEquals(1, factory.get(Float.class).size());

		factory.remove(new Integer(1), new Float(1));

		assertEquals(0, factory.get(Integer.class).size());
		assertEquals(0, factory.get(Float.class).size());
	}

	@Test
	public void removeTest_Class_Objects()
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		factory.add(new Integer(1), new Integer(2), new Float(1), new Float(2));

		assertEquals(2, factory.get(Integer.class).size());
		assertEquals(2, factory.get(Float.class).size());

		factory.remove(Integer.class, new Integer(1), new Integer(2));
		factory.remove(Float.class, new Float(1), new Float(2));

		assertEquals(0, factory.get(Integer.class).size());
		assertEquals(0, factory.get(Float.class).size());
	}

	@Test
	public void removeTest_Class_Collection()
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		factory.add(new Integer(1), new Integer(2), new Float(1), new Float(2));

		assertEquals(2, factory.get(Integer.class).size());
		assertEquals(2, factory.get(Float.class).size());

		factory.remove(Integer.class,
				Arrays.asList(new Integer(1), new Integer(2)));
		factory.remove(Float.class, Arrays.asList(new Float(1), new Float(2)));

		assertEquals(0, factory.get(Integer.class).size());
		assertEquals(0, factory.get(Float.class).size());
	}

	@Test
	public void containsTest_Object()
	{
		Integer bean = new Integer(1);
	
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();
	
		assertFalse(factory.contains(bean));
	
		factory.add(bean);
	
		assertTrue(factory.contains(bean));
		assertFalse(factory.contains(new Integer(2)));
	}

	@Test
	public void containsTest_Class_Object()
	{
		Integer bean = new Integer(1);
	
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();
	
		assertFalse(factory.contains(Integer.class, bean));
	
		factory.add(bean);
	
		assertTrue(factory.contains(Integer.class, bean));
		assertFalse(factory.contains(Integer.class, new Integer(2)));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void getTest()
	{
		EditableImplementorBeanFactory factory = new EditableImplementorBeanFactory();

		assertNull(factory.get(Integer.class));

		factory.add(new Integer(1), new Integer(2));
		factory.add(new Float(1));

		assertThat(factory.get(Integer.class),
				(Matcher) Matchers.contains(new Integer(1), new Integer(2)));
	}

	@Test
	public void getImplementorBeansTest()
	{
		EditableImplementorBeanFactory beanFactory = new EditableImplementorBeanFactory();
		
		Collection<Integer> implementorBeans = beanFactory
				.getImplementorBeans(Integer.class);

		assertNull(implementorBeans);

		beanFactory.add(new Integer(1));
		beanFactory.add(new Float(1));

		implementorBeans = beanFactory.getImplementorBeans(Integer.class);

		assertEquals(1, implementorBeans.size());
		assertThat(implementorBeans, Matchers.contains(new Integer(1)));
	}

	protected static class Implementor0
	{
	}

	protected static class Implementor1
	{
	}
}
