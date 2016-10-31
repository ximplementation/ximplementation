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

/**
 * Builder of <i>implementee</i> bean.
 * 
 * @author earthangry@gmail.com
 * @date 2015-12-3
 *
 */
public interface ImplementeeBeanBuilder
{
	/**
	 * Build an <i>implementee</i> instance.
	 * 
	 * @param implementation
	 *            The {@code Implementation} about the <i>implementee</i>.
	 * @param implementorBeanFactory
	 *            The {@code ImplementorBeanFactory} about the
	 *            <i>implementee</i>.
	 * @return A instance of the <i>implementee</i>.
	 */
	<T> T build(Implementation<T> implementation,
			ImplementorBeanFactory implementorBeanFactory);
}
