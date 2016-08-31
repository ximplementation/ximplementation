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
 * Reference annotation.
 * <p>
 * This annotation indicate that the method has been identified by a name, and
 * can be used by {@linkplain Implement @Implement},
 * {@linkplain Priority @Priority}, {@linkplain Validity @Validity} annotations
 * for method reference.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Refered
{
	/**
	 * The reference name of this method.
	 * 
	 * @return
	 */
	String value();
}
