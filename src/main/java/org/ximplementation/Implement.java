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
 * Implement method annotation.
 * <p>
 * This annotation indicate that the method is an <i>implement method</i>.
 * </p>
 * <p>
 * An <i>implement method</i> is a method in an <i>implementor</i> which
 * overriding or {@code @Implement} methods in <i>implementee</i>.
 * </p>
 * <p>
 * An <i>implementee method</i> is a method in an <i>implementee</i> which is
 * implemented by one or more methods in <i>implementor</i>.
 * </p>
 * <p>
 * The <i>implement method</i> can be annotated with {@linkplain Priority} which
 * indicating its invocation priority and {@linkplain Validity} which indicating
 * its invocation validity.
 * </p>
 * <p>
 * This annotation should be annotated on methods of an <i>implementor</i>.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Implement
{
	/**
	 * The identification of the <i>implementee method</i> which this method is
	 * implemented.
	 * <p>
	 * It can be any of the following values :
	 * </p>
	 * <ul>
	 * <li>The name of the <i>implementee method</i>;</li>
	 * <li>The {@linkplain Refered#value() @Refered.value} of the <i>implementee
	 * method</i>;</li>
	 * <li>The signature part of the <i>implementee method</i> (eg.
	 * <code>"plus(java.lang.Integer, java.lang.Integer)"</code>).</li>
	 * </ul>
	 * <p>
	 * If not set, its value will be treated as this <i>implement method</i>
	 * name.
	 * </p>
	 * 
	 * @return
	 */
	String value() default "";
}
