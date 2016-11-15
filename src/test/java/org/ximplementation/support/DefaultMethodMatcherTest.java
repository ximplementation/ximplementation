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
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.ximplementation.support.DefaultMethodMatcher.MethodPattern;

/**
 * {@linkplain DefaultMethodMatcher} unit tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-11-15
 *
 */
public class DefaultMethodMatcherTest extends AbstractTestSupport
{
	private DefaultMethodMatcher defaultMethodMatcher;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception
	{
		this.defaultMethodMatcher = new DefaultMethodMatcher();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void matchTest()
	{
		assertTrue(this.defaultMethodMatcher.match("m",
				getMethodByName(MatchTest.class, "m"),
				MatchTest.class));

		assertFalse(this.defaultMethodMatcher.match("mm",
				getMethodByName(MatchTest.class, "m"),
				MatchTest.class));
	}

	protected static interface MatchTest
	{
		void m();
	}
	
	@Test
	public void doMatchTest()
	{
		// namePattern == null || namePattern.isEmpty()
		{
			MethodPattern methodPattern0 = new MethodPattern();
			MethodPattern methodPattern1 = new MethodPattern("", null);

			assertFalse(
					this.defaultMethodMatcher
							.doMatch(methodPattern0,
									getMethodByNameAndType(DoMatchTest.class,
											"m", new Class<?>[0]),
									DoMatchTest.class));
			assertFalse(
					this.defaultMethodMatcher
							.doMatch(methodPattern1,
									getMethodByNameAndType(DoMatchTest.class,
											"m", new Class<?>[0]),
									DoMatchTest.class));
		}

		// !fullMethodName.endsWith(namePattern)
		{
			assertFalse(
					this.defaultMethodMatcher
							.doMatch(this.defaultMethodMatcher
									.parseMethodPattern("m0"),
									getMethodByNameAndType(DoMatchTest.class,
											"m", new Class<?>[0]),
									DoMatchTest.class));
		}

		// paramPatterns == null
		{
			assertTrue(this.defaultMethodMatcher.doMatch(
					this.defaultMethodMatcher.parseMethodPattern("m"),
					getMethodByNameAndType(DoMatchTest.class, "m",
							new Class<?>[0]),
					DoMatchTest.class));
			assertTrue(this.defaultMethodMatcher.doMatch(
					this.defaultMethodMatcher.parseMethodPattern("m"),
					getMethodByNameAndType(DoMatchTest.class, "m", int.class),
					DoMatchTest.class));
			assertTrue(
					this.defaultMethodMatcher
							.doMatch(this.defaultMethodMatcher
									.parseMethodPattern("DoMatchTest.m"),
									getMethodByNameAndType(DoMatchTest.class,
											"m", new Class<?>[0]),
									DoMatchTest.class));
		}

		// paramTypes.length != paramPatterns.length
		{
			assertFalse(
					this.defaultMethodMatcher
							.doMatch(
									this.defaultMethodMatcher
											.parseMethodPattern("m()"),
									getMethodByNameAndType(DoMatchTest.class,
											"m", int.class),
									DoMatchTest.class));
		}

		{
			assertTrue(this.defaultMethodMatcher.doMatch(
					this.defaultMethodMatcher.parseMethodPattern("m()"),
					getMethodByNameAndType(DoMatchTest.class, "m",
							new Class<?>[0]),
					DoMatchTest.class));

			assertTrue(this.defaultMethodMatcher.doMatch(
					this.defaultMethodMatcher.parseMethodPattern("m(int)"),
					getMethodByNameAndType(DoMatchTest.class, "m", int.class),
					DoMatchTest.class));

			assertTrue(this.defaultMethodMatcher.doMatch(
					this.defaultMethodMatcher
							.parseMethodPattern("m(String, Long)"),
					getMethodByNameAndType(DoMatchTest.class, "m", String.class,
							Long.class),
					DoMatchTest.class));

			assertTrue(this.defaultMethodMatcher.doMatch(
					this.defaultMethodMatcher.parseMethodPattern(
							"m(lang.String, java.lang.Long)"),
					getMethodByNameAndType(DoMatchTest.class, "m", String.class,
							Long.class),
					DoMatchTest.class));
		}
	}

	protected static interface DoMatchTest
	{
		void m();

		void m(int a);

		void m(String s, Long l);
	}

	@Test
	public void parseMethodPatternTest()
	{
		// m
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("m");

			assertEquals("m", methodPattern.getNamePattern());
			assertNull(methodPattern.getParamPatterns());
		}

		// Foo.m
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("Foo.m");

			assertEquals("Foo.m", methodPattern.getNamePattern());
			assertNull(methodPattern.getParamPatterns());
		}

		// m()
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("m()");

			assertEquals("m", methodPattern.getNamePattern());
			assertEquals(0, methodPattern.getParamPatterns().length);
		}

		// Foo.m()
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("Foo.m()");

			assertEquals("Foo.m", methodPattern.getNamePattern());
			assertEquals(0, methodPattern.getParamPatterns().length);
		}

		// m(int)
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("m(int)");

			assertEquals("m", methodPattern.getNamePattern());
			assertEquals(1, methodPattern.getParamPatterns().length);
			assertEquals("int", methodPattern.getParamPatterns()[0]);
		}

		// Foo.m(int)
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("Foo.m(int)");

			assertEquals("Foo.m", methodPattern.getNamePattern());
			assertEquals(1, methodPattern.getParamPatterns().length);
			assertEquals("int", methodPattern.getParamPatterns()[0]);
		}

		// m(int, long)
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("m(int, long)");

			assertEquals("m", methodPattern.getNamePattern());
			assertEquals(2, methodPattern.getParamPatterns().length);
			assertEquals("int", methodPattern.getParamPatterns()[0]);
			assertEquals("long", methodPattern.getParamPatterns()[1]);
		}

		// Foo.m(int, long)
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("Foo.m(int, long)");

			assertEquals("Foo.m", methodPattern.getNamePattern());
			assertEquals(2, methodPattern.getParamPatterns().length);
			assertEquals("int", methodPattern.getParamPatterns()[0]);
			assertEquals("long", methodPattern.getParamPatterns()[1]);
		}

		// m(lang.Integer, java.lang.Long)
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("m(lang.Integer, java.lang.Long)");

			assertEquals("m", methodPattern.getNamePattern());
			assertEquals(2, methodPattern.getParamPatterns().length);
			assertEquals("lang.Integer", methodPattern.getParamPatterns()[0]);
			assertEquals("java.lang.Long", methodPattern.getParamPatterns()[1]);
		}

		// Foo.m(lang.Integer, java.lang.Long)
		{
			MethodPattern methodPattern = this.defaultMethodMatcher
					.parseMethodPattern("Foo.m(lang.Integer, java.lang.Long)");

			assertEquals("Foo.m", methodPattern.getNamePattern());
			assertEquals(2, methodPattern.getParamPatterns().length);
			assertEquals("lang.Integer", methodPattern.getParamPatterns()[0]);
			assertEquals("java.lang.Long", methodPattern.getParamPatterns()[1]);
		}
	}

	@Test
	public void parseMethodPatternTest_noNamePattern()
	{
		expectedException.expect(IllegalMethodPatternException.class);
		expectedException.expectMessage(
				"method name pattern should be present before '('");

		this.defaultMethodMatcher
				.parseMethodPattern("()");
	}

	@Test
	public void parseMethodPatternTest_noParamTypePattern()
	{
		expectedException.expect(IllegalMethodPatternException.class);
		expectedException.expectMessage(
				"parameter type pattern should be present before ','");

		this.defaultMethodMatcher.parseMethodPattern("m(,)");
	}

	@Test
	public void parseMethodPatternTest_noLeftBracket()
	{
		expectedException.expect(IllegalMethodPatternException.class);
		expectedException.expectMessage(
				", '(' should be present before ')'");

		this.defaultMethodMatcher.parseMethodPattern("m)");
	}
}
