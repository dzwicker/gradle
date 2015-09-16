/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.model.internal.manage.schema.extract;

import org.gradle.model.Managed;
import org.gradle.model.internal.core.NodeInitializer;
import org.gradle.model.internal.core.NodeInitializerRegistry;
import org.gradle.model.internal.manage.schema.ModelManagedImplStructSchema;
import org.gradle.model.internal.manage.schema.ModelSchema;
import org.gradle.model.internal.type.ModelType;

public abstract class ManagedImplStructNodeInitializerExtractionSupport implements NodeInitializerExtractionStrategy {
    private final Class<?> implementedInterface;

    protected ManagedImplStructNodeInitializerExtractionSupport(Class<?> implementedInterface) {
        this.implementedInterface = implementedInterface;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    protected boolean isTarget(ModelType<?> type) {
        if (!type.getRawClass().isAnnotationPresent(Managed.class)) {
            return false;
        }
        return implementedInterface == null
            || (!type.getRawClass().equals(implementedInterface)
            && implementedInterface.isAssignableFrom(type.getRawClass()));
    }

    @Override
    public <T> NodeInitializer extractNodeInitializer(ModelSchema<T> schema, NodeInitializerRegistry nodeInitializerRegistry) {
        if (!(schema instanceof ModelManagedImplStructSchema)) {
            return null;
        }
        if (!isTarget(schema.getType())) {
            return null;
        }
        return extractNodeInitializer((ModelManagedImplStructSchema<T>) schema, nodeInitializerRegistry);
    }

    protected abstract <T> NodeInitializer extractNodeInitializer(ModelManagedImplStructSchema<T> schema, NodeInitializerRegistry nodeInitializerRegistry);
}