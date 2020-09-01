/*
 * Copyright 2002-2014 the original author or authors.
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

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ObjectUtils;
import com.vick.test.util.jdbc.convert.ConversionService;
import com.vick.test.util.jdbc.convert.TypeDescriptor;
import com.vick.test.util.jdbc.convert.converter.ConditionalGenericConverter;
import com.vick.test.util.jdbc.convert.converter.GenericConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * Converts an array to a comma-delimited String. First adapts the source array
 * to a List, then delegates to {@link CollectionToStringConverter} to perform
 * the target String conversion.
 *
 * @author Keith Donald
 * @since 3.0
 */
final class ArrayToStringConverter implements ConditionalGenericConverter {

	private final CollectionToStringConverter helperConverter;


	public ArrayToStringConverter(ConversionService conversionService) {
		this.helperConverter = new CollectionToStringConverter(conversionService);
	}


	@Override
	public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new GenericConverter.ConvertiblePair(Object[].class, String.class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return this.helperConverter.matches(sourceType, targetType);
	}

	@Override
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return this.helperConverter.convert(Arrays.asList(ObjectUtils.toObjectArray(source)), sourceType, targetType);
	}

}
