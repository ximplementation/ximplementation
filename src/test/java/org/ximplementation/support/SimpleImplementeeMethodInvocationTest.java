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

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implement;
import org.ximplementation.Implementor;

/**
 * {@linkplain SimpleImplementeeMethodInvocation} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-11-11
 *
 */
public class SimpleImplementeeMethodInvocationTest extends AbstractTestSupport
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
	public void invokeTest() throws Throwable
	{
		// public method
		{
			Implementation<InvokeTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokeTest.Implementee.class,
							InvokeTest.Implementor0.class);

			Method implementeeMethod = getMethodByName(
					InvokeTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] implementeeMethodParams = {};
			Object implementorBean = new InvokeTest.Implementor0();
			
			SimpleImplementeeMethodInvocation invocation = new SimpleImplementeeMethodInvocation(
					implementation, implementInfo,
					implementeeMethodParams, implementMethodInfo, implementorBean);

			assertEquals(InvokeTest.Implementor0.class.getSimpleName(),
					invocation.invoke());
		}

		// default method
		{
			Implementation<InvokeTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokeTest.Implementee.class,
							InvokeTest.Implementor1.class);

			Method implementeeMethod = getMethodByName(
					InvokeTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] implementeeMethodParams = {};
			Object implementorBean = new InvokeTest.Implementor1();

			SimpleImplementeeMethodInvocation invocation = new SimpleImplementeeMethodInvocation(
					implementation, implementInfo, implementeeMethodParams,
					implementMethodInfo, implementorBean);

			assertEquals(InvokeTest.Implementor1.class.getSimpleName(),
					invocation.invoke());
		}

		// protected method
		{
			Implementation<InvokeTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokeTest.Implementee.class,
							InvokeTest.Implementor2.class);

			Method implementeeMethod = getMethodByName(
					InvokeTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] implementeeMethodParams = {};
			Object implementorBean = new InvokeTest.Implementor2();

			SimpleImplementeeMethodInvocation invocation = new SimpleImplementeeMethodInvocation(
					implementation, implementInfo, implementeeMethodParams,
					implementMethodInfo, implementorBean);

			assertEquals(InvokeTest.Implementor2.class.getSimpleName(),
					invocation.invoke());
		}

		// private method
		{
			Implementation<InvokeTest.Implementee> implementation = this.implementationResolver
					.resolve(InvokeTest.Implementee.class,
							InvokeTest.Implementor3.class);

			Method implementeeMethod = getMethodByName(
					InvokeTest.Implementee.class, "handle");

			ImplementInfo implementInfo = implementation
					.getImplementInfo(implementeeMethod);

			ImplementMethodInfo implementMethodInfo = implementInfo
					.getImplementMethodInfos()[0];
			Object[] implementeeMethodParams = {};
			Object implementorBean = new InvokeTest.Implementor3();

			SimpleImplementeeMethodInvocation invocation = new SimpleImplementeeMethodInvocation(
					implementation, implementInfo, implementeeMethodParams,
					implementMethodInfo, implementorBean);

			assertEquals(InvokeTest.Implementor3.class.getSimpleName(),
					invocation.invoke());
		}
	}

	protected static class InvokeTest
	{
		public static class Implementee
		{
			public String handle()
			{
				return Implementee.class.getSimpleName();
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor0
		{
			@Implement
			public String handle()
			{
				return Implementor0.class.getSimpleName();
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor1
		{
			@Implement
			String handle()
			{
				return Implementor1.class.getSimpleName();
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor2
		{
			@Implement
			protected String handle()
			{
				return Implementor2.class.getSimpleName();
			}
		}

		@Implementor(Implementee.class)
		public static class Implementor3
		{
			@Implement
			private String handle()
			{
				return Implementor3.class.getSimpleName();
			}
		}
	}
}
