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
 * An <i>implementor</i> is a class which implements one or more
 * <i>implementee</i>s by the {@code 'implements' or 'extends'} syntax or by
 * this {@code Implementor} annotation.
 * </p>
 * <p>
 * An <i>implementee</i> is a class which is implemented by one or more
 * <i>implementor</i>s.
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
	 * It can be any classes not just this <i>implementor</i>'s ancestors.
	 * </p>
	 * 
	 * @return
	 */
	Class<?>[] value() default Object.class;
}
