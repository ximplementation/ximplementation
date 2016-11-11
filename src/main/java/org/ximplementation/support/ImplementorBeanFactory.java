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

import java.util.Collection;

/**
 * Factory of <i>implementor</i> beans.
 * <p>
 * It is used for getting <i>implementor</i> beans when invoking <i>implement
 * method</i>s.
 * </p>
 * 
 * @author earthangry@gmail.com
 * @date 2016-8-15
 * @see ImplementeeBeanBuilder
 * @see ImplementeeMethodInvocationFactory
 */
public interface ImplementorBeanFactory
{
	/**
	 * Get beans of the given <i>implementor</i>.
	 * 
	 * @param implementor
	 *            An <i>implementor</i>.
	 * @return The beans of the <i>implementor</i>, {@code null} or an empty
	 *         {@code Collection} if none.
	 */
	<T> Collection<T> getImplementorBeans(Class<T> implementor);
}
