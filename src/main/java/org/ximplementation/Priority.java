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
 * Priority annotation.
 * <p>
 * This annotation indicate that the <i>implement method</i> has an invocation
 * priority, either a fixed value or the result of a priority method invocation.
 * </p>
 * <p>
 * Priority method should return a value of {@code int} type, and its parameters
 * can be annotated with {@linkplain Index @Index} , for setting the parameter
 * value to specified index parameter value of the <i>implementee method</i>
 * when invoking.
 * </p>
 * <p>
 * This annotation should be annotated on <i>implement method</i>.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 * @see Implement
 * @see Index
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Priority
{
	/** The default priority value */
	int DEFAULT = 0;

	/**
	 * The fixed priority value.
	 * <p>
	 * Its default value is {@code 0}。
	 * </p>
	 * 
	 * @return
	 */
	int value() default DEFAULT;

	/**
	 * The identification of the priority method.
	 * <p>
	 * It can be any of the following values :
	 * </p>
	 * <ul>
	 * <li>The name of the priority method if no duplication;</li>
	 * <li>The {@linkplain Refered#value() @Refered.value} of the priority
	 * method;</li>
	 * <li>The signature part of the priority method (eg.
	 * <code>"getPriority(java.lang.String)"</code>).</li>
	 * </ul>
	 * 
	 * @return
	 */
	String method() default "";
}
