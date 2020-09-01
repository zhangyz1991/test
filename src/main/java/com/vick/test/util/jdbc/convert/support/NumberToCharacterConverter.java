/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vick.test.util.jdbc.convert.support;

import com.vick.test.util.jdbc.convert.converter.Converter;

/**
 * Converts from any JDK-standard Number implementation to a Character.
 *
 * @author Keith Donald
 * @since 3.0
 * @see Character
 * @see Short
 * @see Integer
 * @see Long
 * @see java.math.BigInteger
 * @see Float
 * @see Double
 * @see java.math.BigDecimal
 */
final class NumberToCharacterConverter implements Converter<Number, Character> {

	@Override
	public Character convert(Number source) {
		return (char) source.shortValue();
	}

}
