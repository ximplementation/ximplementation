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

package org.ximplementation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <i>Implementor</i> annotation.
 * <p>
 * This annotation indicate that the class is an <i>implementor</i> of some
 * <i>implementee</i>s.
 * </p>
 * <p>
 * <b>An <i>implementor</i> is a class which implements one or more
 * <i>implementee</i>s through the {@code 'implements'} or {@code 'extends'}
 * keywords or through {@code @Implementor} annotation.</b>
 * </p>
 * <p>
 * <b>An <i>implementee</i> is a class which is implemented by one or more
 * <i>implementor</i>s.</b>
 * </p>
 * <p>
 * <b>Also, if {@code A} is an <i>implementor</i> of {@code B}, and {@code B} is
 * an <i>implementor</i> of {@code C}, then {@code A} is an <i>implementor</i>
 * of {@code C}.</b>
 * </p>
 * <p>
 * Examples:
 * </p>
 * <code>
 * <pre>
 * public interface Foo0
 * {
 * 	void foo0();
 * }
 * 
 * public abstract class Foo1
 * {
 * 	public abstract void foo1();
 * }
 * 
 * public class Bar0 extends Foo1 implements Foo0
 * {
 * 	&#64;Override
 * 	public void foo0(){}
 * 
 * 	&#64;Override
 * 	public void foo1(){}
 * }
 * 
 * &#64;Implementor(Foo0.class)
 * public class Bar1
 * {
 * 	&#64;Implement
 * 	public void foo0(){}
 * }
 * 
 * &#64;Implementor({Foo0.class, Foo1.class})
 * public class Bar2
 * {
 * 	&#64;Implement
 * 	public void foo1(){}
 * }
 * </pre>
 * </code>
 * <p>
 * Here, {@code Foo0} and {@code Foo1} are <i>implementee</i>s, {@code Bar0} is
 * an <i>implementor</i> of {@code Foo0} and {@code Foo1}, {@code Bar1} is an
 * <i>implementor</i> of {@code Foo0}, {@code Bar2} is an <i>implementor</i> of
 * {@code Foo0} and {@code Foo1} but only implements {@code Foo1.foo1} method.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Implementor
{
	/**
	 * The <i>implementee</i>s which this <i>implementor</i> is implemented.
	 * <p>
	 * It can be any classes not only this <i>implementor</i>'s ancestors.
	 * </p>
	 * 
	 * @return
	 */
	Class<?>[] value() default Object.class;
}
