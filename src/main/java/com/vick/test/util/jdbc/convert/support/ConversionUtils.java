/*
 * Copyright 2002-2019 the original author or authors.
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

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.ClassUtils;
import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.convert.ConversionFailedException;
import com.vick.test.util.jdbc.convert.ConversionService;
import com.vick.test.util.jdbc.convert.TypeDescriptor;
import com.vick.test.util.jdbc.convert.converter.GenericConverter;

/**
 * Internal utilities for the conversion package.
 *
 * @author Keith Donald
 * @author Stephane Nicoll
 * @since 3.0
 */
abstract class ConversionUtils {

	@Nullable
	public static Object invokeConverter(GenericConverter converter, @Nullable Object source,
										 TypeDescriptor sourceType, TypeDescriptor targetType) {

		try {
			return converter.convert(source, sourceType, targetType);
		}
		catch (ConversionFailedException ex) {
			throw ex;
		}
		catch (Throwable ex) {
			throw new ConversionFailedException(sourceType, targetType, source, ex);
		}
	}

	public static boolean canConvertElements(@Nullable TypeDescriptor sourceElementType,
			@Nullable TypeDescriptor targetElementType, ConversionService conversionService) {

		if (targetElementType == null) {
			// yes
			return true;
		}
		if (sourceElementType == null) {
			// maybe
			return true;
		}
		if (conversionService.canConvert(sourceElementType, targetElementType)) {
			// yes
			return true;
		}
		if (ClassUtils.isAssignable(sourceElementType.getType(), targetElementType.getType())) {
			// maybe
			return true;
		}
		// no
		return false;
	}

	public static Class<?> getEnumType(Class<?> targetType) {
		Class<?> enumType = targetType;
		while (enumType != null && !enumType.isEnum()) {
			enumType = enumType.getSuperclass();
		}
		Assert.notNull(enumType, () -> "The target type " + targetType.getName() + " does not refer to an enum");
		return enumType;
	}

}
