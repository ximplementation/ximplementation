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

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ximplementation.Implementor;
import org.ximplementation.Priority;
import org.ximplementation.Validity;

/**
 * {@linkplain SimpleImplementeeMethodInvocationFactory} and
 * {@linkplain CachedImplementeeMethodInvocationFactory} performance tests.
 * 
 * @author earthangry@gmail.com
 * @date 2016-12-8
 *
 */
public class ImplementeeMethodInvocationFactoryPerformanceTest
		extends AbstractTestSupport
{
	protected static final int TEST_TIMES = 10;

	protected static final int INVOCATION_COUNT = 100000;

	private ImplementationResolver implementationResolver;

	@SuppressWarnings("rawtypes")
	private Implementation<Implementee> implementation_fewImplementors_noValidityAndPriorityMethodPresents;
	private ImplementorBeanFactory implementorBeanFactory_fewImplementors_noValidityAndPriorityMethodPresents;

	@SuppressWarnings("rawtypes")
	private Implementation<Implementee> implementation_fewImplementors_withValidityAndPriorityMethodPresents;
	private ImplementorBeanFactory implementorBeanFactory_fewImplementors_withValidityAndPriorityMethodPresents;

	@SuppressWarnings("rawtypes")
	private Implementation<Implementee> implementation_lotsImplementors_noValidityAndPriorityMethodPresents;
	private ImplementorBeanFactory implementorBeanFactory_lotsImplementors_noValidityAndPriorityMethodPresents;

	@SuppressWarnings("rawtypes")
	private Implementation<Implementee> implementation_lotsImplementors_withValidityAndPriorityMethodPresents;
	private ImplementorBeanFactory implementorBeanFactory_lotsImplementors_withValidityAndPriorityMethodPresents;
	
	@SuppressWarnings("rawtypes")
	@Before
	public void setUp() throws Exception
	{
		this.implementationResolver = new ImplementationResolver();

		implementation_fewImplementors_noValidityAndPriorityMethodPresents = this.implementationResolver
				.resolve(Implementee.class, Implementor0.class,
						Implementor1.class, Implementor2.class);

		implementorBeanFactory_fewImplementors_noValidityAndPriorityMethodPresents = SimpleImplementorBeanFactory
				.valueOf(new Implementor0<Number>(), new Implementor0(),
						new Implementor1(), new Implementor2());

		implementation_fewImplementors_withValidityAndPriorityMethodPresents = this.implementationResolver
				.resolve(Implementee.class, Implementor0.class,
						Implementor1.class, Implementor100.class);

		implementorBeanFactory_fewImplementors_withValidityAndPriorityMethodPresents = SimpleImplementorBeanFactory
				.valueOf(new Implementor0<Number>(), new Implementor0(),
						new Implementor1(), new Implementor100());

		implementation_lotsImplementors_noValidityAndPriorityMethodPresents = this.implementationResolver
				.resolve(Implementee.class, Implementor0.class,
						Implementor1.class, Implementor2.class,
						Implementor3.class, Implementor4.class,
						Implementor5.class, Implementor6.class,
						Implementor7.class, Implementor8.class,
						Implementor9.class, Implementor10.class,
						Implementor11.class, Implementor12.class,
						Implementor13.class, Implementor14.class,
						Implementor15.class, Implementor16.class,
						Implementor17.class, Implementor18.class,
						Implementor19.class, Implementor20.class,
						Implementor21.class, Implementor22.class,
						Implementor23.class, Implementor24.class,
						Implementor25.class, Implementor26.class,
						Implementor27.class, Implementor28.class,
						Implementor29.class, Implementor30.class,
						Implementor31.class, Implementor32.class,
						Implementor33.class, Implementor34.class,
						Implementor35.class, Implementor36.class,
						Implementor37.class, Implementor38.class,
						Implementor39.class, Implementor40.class,
						Implementor41.class, Implementor42.class,
						Implementor43.class, Implementor44.class,
						Implementor45.class, Implementor46.class,
						Implementor47.class, Implementor48.class,
						Implementor49.class, Implementor50.class,
						Implementor51.class, Implementor52.class,
						Implementor53.class, Implementor54.class,
						Implementor55.class, Implementor56.class,
						Implementor57.class, Implementor58.class,
						Implementor59.class, Implementor60.class,
						Implementor61.class, Implementor62.class,
						Implementor63.class, Implementor64.class,
						Implementor65.class, Implementor66.class,
						Implementor67.class, Implementor68.class,
						Implementor69.class, Implementor70.class,
						Implementor71.class, Implementor72.class,
						Implementor73.class, Implementor74.class,
						Implementor75.class, Implementor76.class,
						Implementor77.class, Implementor78.class,
						Implementor79.class, Implementor80.class,
						Implementor81.class, Implementor82.class,
						Implementor83.class, Implementor84.class,
						Implementor85.class, Implementor86.class,
						Implementor87.class, Implementor88.class,
						Implementor89.class, Implementor90.class,
						Implementor91.class, Implementor92.class,
						Implementor93.class, Implementor94.class,
						Implementor95.class, Implementor96.class,
						Implementor97.class, Implementor98.class,
						Implementor99.class);

		implementorBeanFactory_lotsImplementors_noValidityAndPriorityMethodPresents = SimpleImplementorBeanFactory
				.valueOf(new Implementor0<Number>(), new Implementor0(),
						new Implementor1(), new Implementor2(),
						new Implementor3(), new Implementor4(),
						new Implementor5(), new Implementor6(),
						new Implementor7(), new Implementor8(),
						new Implementor9(), new Implementor10(),
						new Implementor11(), new Implementor12(),
						new Implementor13(), new Implementor14(),
						new Implementor15(), new Implementor16(),
						new Implementor17(), new Implementor18(),
						new Implementor19(), new Implementor20(),
						new Implementor21(), new Implementor22(),
						new Implementor23(), new Implementor24(),
						new Implementor25(), new Implementor26(),
						new Implementor27(), new Implementor28(),
						new Implementor29(), new Implementor30(),
						new Implementor31(), new Implementor32(),
						new Implementor33(), new Implementor34(),
						new Implementor35(), new Implementor36(),
						new Implementor37(), new Implementor38(),
						new Implementor39(), new Implementor40(),
						new Implementor41(), new Implementor42(),
						new Implementor43(), new Implementor44(),
						new Implementor45(), new Implementor46(),
						new Implementor47(), new Implementor48(),
						new Implementor49(), new Implementor50(),
						new Implementor51(), new Implementor52(),
						new Implementor53(), new Implementor54(),
						new Implementor55(), new Implementor56(),
						new Implementor57(), new Implementor58(),
						new Implementor59(), new Implementor60(),
						new Implementor61(), new Implementor62(),
						new Implementor63(), new Implementor64(),
						new Implementor65(), new Implementor66(),
						new Implementor67(), new Implementor68(),
						new Implementor69(), new Implementor70(),
						new Implementor71(), new Implementor72(),
						new Implementor73(), new Implementor74(),
						new Implementor75(), new Implementor76(),
						new Implementor77(), new Implementor78(),
						new Implementor79(), new Implementor80(),
						new Implementor81(), new Implementor82(),
						new Implementor83(), new Implementor84(),
						new Implementor85(), new Implementor86(),
						new Implementor87(), new Implementor88(),
						new Implementor89(), new Implementor90(),
						new Implementor91(), new Implementor92(),
						new Implementor93(), new Implementor94(),
						new Implementor95(), new Implementor96(),
						new Implementor97(), new Implementor98(),
						new Implementor99());

		implementation_lotsImplementors_withValidityAndPriorityMethodPresents = this.implementationResolver
				.resolve(Implementee.class, Implementor0.class,
						Implementor1.class, Implementor2.class,
						Implementor3.class, Implementor4.class,
						Implementor5.class, Implementor6.class,
						Implementor7.class, Implementor8.class,
						Implementor9.class, Implementor10.class,
						Implementor11.class, Implementor12.class,
						Implementor13.class, Implementor14.class,
						Implementor15.class, Implementor16.class,
						Implementor17.class, Implementor18.class,
						Implementor19.class, Implementor20.class,
						Implementor21.class, Implementor22.class,
						Implementor23.class, Implementor24.class,
						Implementor25.class, Implementor26.class,
						Implementor27.class, Implementor28.class,
						Implementor29.class, Implementor30.class,
						Implementor31.class, Implementor32.class,
						Implementor33.class, Implementor34.class,
						Implementor35.class, Implementor36.class,
						Implementor37.class, Implementor38.class,
						Implementor39.class, Implementor40.class,
						Implementor41.class, Implementor42.class,
						Implementor43.class, Implementor44.class,
						Implementor45.class, Implementor46.class,
						Implementor47.class, Implementor48.class,
						Implementor49.class, Implementor50.class,
						Implementor51.class, Implementor52.class,
						Implementor53.class, Implementor54.class,
						Implementor55.class, Implementor56.class,
						Implementor57.class, Implementor58.class,
						Implementor59.class, Implementor60.class,
						Implementor61.class, Implementor62.class,
						Implementor63.class, Implementor64.class,
						Implementor65.class, Implementor66.class,
						Implementor67.class, Implementor68.class,
						Implementor69.class, Implementor70.class,
						Implementor71.class, Implementor72.class,
						Implementor73.class, Implementor74.class,
						Implementor75.class, Implementor76.class,
						Implementor77.class, Implementor78.class,
						Implementor79.class, Implementor80.class,
						Implementor81.class, Implementor82.class,
						Implementor83.class, Implementor84.class,
						Implementor85.class, Implementor86.class,
						Implementor87.class, Implementor88.class,
						Implementor89.class, Implementor90.class,
						Implementor91.class, Implementor92.class,
						Implementor93.class, Implementor94.class,
						Implementor95.class, Implementor96.class,
						Implementor97.class, Implementor98.class,
						Implementor100.class);

		implementorBeanFactory_lotsImplementors_withValidityAndPriorityMethodPresents = SimpleImplementorBeanFactory
				.valueOf(new Implementor0<Number>(), new Implementor0(),
						new Implementor1(), new Implementor2(),
						new Implementor3(), new Implementor4(),
						new Implementor5(), new Implementor6(),
						new Implementor7(), new Implementor8(),
						new Implementor9(), new Implementor10(),
						new Implementor11(), new Implementor12(),
						new Implementor13(), new Implementor14(),
						new Implementor15(), new Implementor16(),
						new Implementor17(), new Implementor18(),
						new Implementor19(), new Implementor20(),
						new Implementor21(), new Implementor22(),
						new Implementor23(), new Implementor24(),
						new Implementor25(), new Implementor26(),
						new Implementor27(), new Implementor28(),
						new Implementor29(), new Implementor30(),
						new Implementor31(), new Implementor32(),
						new Implementor33(), new Implementor34(),
						new Implementor35(), new Implementor36(),
						new Implementor37(), new Implementor38(),
						new Implementor39(), new Implementor40(),
						new Implementor41(), new Implementor42(),
						new Implementor43(), new Implementor44(),
						new Implementor45(), new Implementor46(),
						new Implementor47(), new Implementor48(),
						new Implementor49(), new Implementor50(),
						new Implementor51(), new Implementor52(),
						new Implementor53(), new Implementor54(),
						new Implementor55(), new Implementor56(),
						new Implementor57(), new Implementor58(),
						new Implementor59(), new Implementor60(),
						new Implementor61(), new Implementor62(),
						new Implementor63(), new Implementor64(),
						new Implementor65(), new Implementor66(),
						new Implementor67(), new Implementor68(),
						new Implementor69(), new Implementor70(),
						new Implementor71(), new Implementor72(),
						new Implementor73(), new Implementor74(),
						new Implementor75(), new Implementor76(),
						new Implementor77(), new Implementor78(),
						new Implementor79(), new Implementor80(),
						new Implementor81(), new Implementor82(),
						new Implementor83(), new Implementor84(),
						new Implementor85(), new Implementor86(),
						new Implementor87(), new Implementor88(),
						new Implementor89(), new Implementor90(),
						new Implementor91(), new Implementor92(),
						new Implementor93(), new Implementor94(),
						new Implementor95(), new Implementor96(),
						new Implementor97(), new Implementor98(),
						new Implementor100());
	}

	@After
	public void tearDown() throws Exception
	{
		this.implementationResolver = null;
	}

	@Test
	public void test()
	{
		long raw = 0;
		long few_no_simple = 0;
		long few_no_cached = 0;
		long few_with_simple = 0;
		long few_with_cached = 0;
		long lots_no_simple = 0;
		long lots_no_cached = 0;
		long lots_with_simple = 0;
		long lots_with_cached = 0;

		for (int i = 0; i < TEST_TIMES; i++)
		{
			raw += rawProxy();
		}
		raw = raw / TEST_TIMES;

		for (int i = 0; i < TEST_TIMES; i++)
		{
			few_no_simple += fewImplementors_noValidityAndPriorityMethodPresents_simpleProxyImplementeeBeanBuilder();
		}
		few_no_simple = few_no_simple / TEST_TIMES;

		for (int i = 0; i < TEST_TIMES; i++)
		{
			few_no_cached += fewImplementors_noValidityAndPriorityMethodPresents_cachedProxyImplementeeBeanBuilder();
		}
		few_no_cached = few_no_cached / TEST_TIMES;

		for (int i = 0; i < TEST_TIMES; i++)
		{
			few_with_simple += fewImplementors_withValidityAndPriorityMethodPresents_simpleProxyImplementeeBeanBuilder();
		}
		few_with_simple = few_with_simple / TEST_TIMES;

		for (int i = 0; i < TEST_TIMES; i++)
		{
			few_with_cached += fewImplementors_withValidityAndPriorityMethodPresents_cachedProxyImplementeeBeanBuilder();
		}
		few_with_cached = few_with_cached / TEST_TIMES;


		for (int i = 0; i < TEST_TIMES; i++)
		{
			lots_no_simple += lotsImplementors_noValidityAndPriorityMethodPresents_simpleProxyImplementeeBeanBuilder();
		}
		lots_no_simple = lots_no_simple / TEST_TIMES;

		for (int i = 0; i < TEST_TIMES; i++)
		{
			lots_no_cached += lotsImplementors_noValidityAndPriorityMethodPresents_cachedProxyImplementeeBeanBuilder();
		}
		lots_no_cached = lots_no_cached / TEST_TIMES;

		for (int i = 0; i < TEST_TIMES; i++)
		{
			lots_with_simple += lotsImplementors_withValidityAndPriorityMethodPresents_simpleProxyImplementeeBeanBuilder();
		}
		lots_with_simple = lots_with_simple / TEST_TIMES;

		for (int i = 0; i < TEST_TIMES; i++)
		{
			lots_with_cached += lotsImplementors_withValidityAndPriorityMethodPresents_cachedProxyImplementeeBeanBuilder();
		}
		lots_with_cached = lots_with_cached / TEST_TIMES;

		println("------Performance test for ImplementeeMethodInvocationFactory------");
		println("*** avg of [" + TEST_TIMES + "] test times ***");
		println("*** total of [" + INVOCATION_COUNT + "] invocation count ***");
		println("*** [raw] : raw jdk proxy ***");
		println("*** [simple] : SimpleImplementeeMethodInvocationFactory ***");
		println("*** [cached] : CachedImplementeeMethodInvocationFactory ***");

		println();
		println("RESULT of [few implementors, with validity and priority method presents] : ");
		println("[raw] : " + raw);
		println("[simple] : " + few_with_simple);
		println("[cached] : " + few_with_cached);
		println("[simple]/[raw] : " + divide(few_with_simple, raw));
		println("[cached]/[raw] : " + divide(few_with_cached, raw));
		println("[cached]/[simple] : "
				+ divide(few_with_cached, few_with_simple));

		println();
		println("RESULT of [few implementors, no validity and priority method presents] : ");
		println("[raw] : " + raw);
		println("[simple] : " + few_no_simple);
		println("[cached] : " + few_no_cached);
		println("[simple]/[raw] : " + divide(few_no_simple, raw));
		println("[cached]/[raw] : " + divide(few_no_cached, raw));
		println("[cached]/[simple] : "
				+ divide(few_no_cached, few_no_simple));

		println();
		println("RESULT of [lots implementors, with validity and priority method presents] : ");
		println("[raw] : " + raw);
		println("[simple] : " + lots_with_simple);
		println("[cached] : " + lots_with_cached);
		println("[simple]/[raw] : " + divide(lots_with_simple, raw));
		println("[cached]/[raw] : " + divide(lots_with_cached, raw));
		println("[cached]/[simple] : "
				+ divide(lots_with_cached, lots_with_simple));

		println();
		println("RESULT of [lots implementors, no validity and priority method presents] : ");
		println("[raw] : " + raw);
		println("[simple] : " + lots_no_simple);
		println("[cached] : " + lots_no_cached);
		println("[simple]/[raw] : " + divide(lots_no_simple, raw));
		println("[cached]/[raw] : " + divide(lots_no_cached, raw));
		println("[cached]/[simple] : "
				+ divide(lots_no_cached, lots_no_simple));

		println();

		assertTrue(few_with_cached < few_with_simple);
		assertTrue(few_no_cached < few_no_simple);
		assertTrue(lots_with_cached < lots_with_simple);
		assertTrue(lots_no_cached < lots_no_simple);
	}

	protected void println(String s)
	{
		System.out.println(s);
	}

	protected void println()
	{
		System.out.println();
	}

	protected String divide(long a, long b)
	{
		return new DecimalFormat("#.##").format(a / (float) b);
	}

	protected long rawProxy()
	{
		Implementee<Number> implementee = createRawJdkProxy();

		return invoke(implementee);
	}

	protected long fewImplementors_noValidityAndPriorityMethodPresents_simpleProxyImplementeeBeanBuilder()
	{
		return fewImplementors_noValidityAndPriorityMethodPresents(
				new SimpleImplementeeMethodInvocationFactory());
	}

	protected long fewImplementors_noValidityAndPriorityMethodPresents_cachedProxyImplementeeBeanBuilder()
	{
		return fewImplementors_noValidityAndPriorityMethodPresents(
				new CachedImplementeeMethodInvocationFactory());
	}

	protected long fewImplementors_withValidityAndPriorityMethodPresents_simpleProxyImplementeeBeanBuilder()
	{
		return fewImplementors_withValidityAndPriorityMethodPresents(
				new SimpleImplementeeMethodInvocationFactory());
	}

	protected long fewImplementors_withValidityAndPriorityMethodPresents_cachedProxyImplementeeBeanBuilder()
	{
		return fewImplementors_withValidityAndPriorityMethodPresents(
				new CachedImplementeeMethodInvocationFactory());
	}

	protected long lotsImplementors_noValidityAndPriorityMethodPresents_simpleProxyImplementeeBeanBuilder()
	{
		return lotsImplementors_noValidityAndPriorityMethodPresents(
				new SimpleImplementeeMethodInvocationFactory());
	}

	protected long lotsImplementors_noValidityAndPriorityMethodPresents_cachedProxyImplementeeBeanBuilder()
	{
		return lotsImplementors_noValidityAndPriorityMethodPresents(
				new CachedImplementeeMethodInvocationFactory());
	}

	protected long lotsImplementors_withValidityAndPriorityMethodPresents_simpleProxyImplementeeBeanBuilder()
	{
		return lotsImplementors_withValidityAndPriorityMethodPresents(
				new SimpleImplementeeMethodInvocationFactory());
	}

	protected long lotsImplementors_withValidityAndPriorityMethodPresents_cachedProxyImplementeeBeanBuilder()
	{
		return lotsImplementors_withValidityAndPriorityMethodPresents(
				new CachedImplementeeMethodInvocationFactory());
	}

	@SuppressWarnings({ "unchecked" })
	protected long fewImplementors_noValidityAndPriorityMethodPresents(
			ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory)
	{
		ProxyImplementeeBeanBuilder proxyImplementeeBeanBuilder = new ProxyImplementeeBeanBuilder();
		proxyImplementeeBeanBuilder.setImplementeeMethodInvocationFactory(
				implementeeMethodInvocationFactory);

		Implementee<Number> implementee = proxyImplementeeBeanBuilder
				.build(implementation_fewImplementors_noValidityAndPriorityMethodPresents,
						implementorBeanFactory_fewImplementors_noValidityAndPriorityMethodPresents);

		return invoke(implementee);
	}

	@SuppressWarnings({ "unchecked" })
	protected long fewImplementors_withValidityAndPriorityMethodPresents(
			ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory)
	{
		ProxyImplementeeBeanBuilder proxyImplementeeBeanBuilder = new ProxyImplementeeBeanBuilder();
		proxyImplementeeBeanBuilder.setImplementeeMethodInvocationFactory(
				implementeeMethodInvocationFactory);

		Implementee<Number> implementee = proxyImplementeeBeanBuilder
				.build(implementation_fewImplementors_withValidityAndPriorityMethodPresents,
						implementorBeanFactory_fewImplementors_withValidityAndPriorityMethodPresents);

		return invoke(implementee);
	}

	@SuppressWarnings({ "unchecked" })
	protected long lotsImplementors_noValidityAndPriorityMethodPresents(
			ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory)
	{
		ProxyImplementeeBeanBuilder proxyImplementeeBeanBuilder = new ProxyImplementeeBeanBuilder();
		proxyImplementeeBeanBuilder.setImplementeeMethodInvocationFactory(
				implementeeMethodInvocationFactory);

		Implementee<Number> implementee = proxyImplementeeBeanBuilder
				.build(implementation_lotsImplementors_noValidityAndPriorityMethodPresents,
						implementorBeanFactory_lotsImplementors_noValidityAndPriorityMethodPresents);

		return invoke(implementee);
	}

	@SuppressWarnings({ "unchecked" })
	protected long lotsImplementors_withValidityAndPriorityMethodPresents(
			ImplementeeMethodInvocationFactory implementeeMethodInvocationFactory)
	{
		ProxyImplementeeBeanBuilder proxyImplementeeBeanBuilder = new ProxyImplementeeBeanBuilder();
		proxyImplementeeBeanBuilder.setImplementeeMethodInvocationFactory(
				implementeeMethodInvocationFactory);

		Implementee<Number> implementee = proxyImplementeeBeanBuilder
				.build(implementation_lotsImplementors_withValidityAndPriorityMethodPresents,
						implementorBeanFactory_lotsImplementors_withValidityAndPriorityMethodPresents);

		return invoke(implementee);
	}

	protected long invoke(Implementee<Number> implementee)
	{
		long time = System.currentTimeMillis();
		for (int i = 0; i < INVOCATION_COUNT; i++)
		{
			implementee.compare(i, i + 1);
		}
		time = System.currentTimeMillis() - time;

		return time;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Implementee<Number> createRawJdkProxy()
	{
		return (Implementee)java.lang.reflect.Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Implementee.class}, new java.lang.reflect.InvocationHandler()
		{
			private Implementor0<Number> implementor0 = new Implementor0<Number>();
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
			{
				return method.invoke(this.implementor0, args);
			}
		});
	}
	
	public static interface Implementee<T extends Number>
	{
		int compare(T a, T b);
	}

	@Implementor(Implementee.class)
	public static class Implementor0<T extends Number> implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor1 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor2 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor3 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor4 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor5 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor6 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor7 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor8 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor9 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor10<T extends Number>
			implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor11 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor12 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor13 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor14 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor15 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor16 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor17 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor18 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor19 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor20<T extends Number>
			implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor21 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor22 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor23 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor24 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor25 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor26 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor27 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor28 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor29 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor30<T extends Number>
			implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor31 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor32 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor33 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor34 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor35 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor36 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor37 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor38 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor39 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor40<T extends Number>
			implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor41 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor42 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor43 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor44 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor45 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor46 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor47 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor48 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor49 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor50<T extends Number>
			implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor51 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor52 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor53 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor54 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor55 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor56 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor57 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor58 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor59 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor60<T extends Number>
			implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor61 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor62 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor63 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor64 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor65 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor66 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor67 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor68 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor69 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor70<T extends Number>
			implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor71 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor72 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor73 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor74 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor75 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor76 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor77 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor78 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor79 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor80<T extends Number>
			implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor81 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor82 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor83 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor84 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor85 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor86 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor87 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor88 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor89 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	@Implementor(Implementee.class)
	public static class Implementor90<T extends Number> implements Implementee<T>
	{
		@Override
		public int compare(T a, T b)
		{
			return 0;
		}
	}

	public static class Implementor91 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor92 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor93 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor94 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor95 implements Implementee<Byte>
	{
		@Override
		public int compare(Byte a, Byte b)
		{
			return 0;
		}
	}

	public static class Implementor96 implements Implementee<Integer>
	{
		@Override
		public int compare(Integer a, Integer b)
		{
			return 0;
		}
	}

	public static class Implementor97 implements Implementee<Long>
	{
		@Override
		public int compare(Long a, Long b)
		{
			return 0;
		}
	}

	public static class Implementor98 implements Implementee<Float>
	{
		@Override
		public int compare(Float a, Float b)
		{
			return 0;
		}
	}

	public static class Implementor99 implements Implementee<Double>
	{
		@Override
		public int compare(Double a, Double b)
		{
			return 0;
		}
	}

	public static class Implementor100 implements Implementee<Number>
	{
		@Override
		@Validity("isValid")
		@Priority("getPriority")
		public int compare(Number a, Number b)
		{
			return 0;
		}

		public boolean isValid()
		{
			return true;
		}

		public int getPriority()
		{
			return 1;
		}
	}
}
