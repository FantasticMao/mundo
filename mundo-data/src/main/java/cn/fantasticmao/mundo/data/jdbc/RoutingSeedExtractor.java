package cn.fantasticmao.mundo.data.jdbc;

import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.data.util.ReflectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RoutingSeedExtractor
 *
 * @author fantasticmao
 * @version 1.0.6
 * @since 2022-08-18
 */
final class RoutingSeedExtractor {
    private static final ConcurrentHashMap<Method, MergedAnnotation<RoutingSeed>> METHOD_ANNOTATION_CACHE
        = new ConcurrentHashMap<>(32);
    private static final ConcurrentHashMap<Class<?>, MergedAnnotation<RoutingSeed>> CLASS_ANNOTATION_CACHE
        = new ConcurrentHashMap<>(32);
    private static final ConcurrentHashMap<Class<?>, Optional<Field>> DOMAIN_FIELD_CACHE
        = new ConcurrentHashMap<>(32);
    private static final ConcurrentHashMap<Class<?>, Method> DOMAIN_GETTER_CACHE
        = new ConcurrentHashMap<>(32);

    @Nullable
    public static Object fromDomainFields(Object[] arguments, Class<?> domainType)
        throws InvocationTargetException, IllegalAccessException {
        Optional<Field> seedFieldOpt = getDomainField(domainType);
        if (seedFieldOpt.isEmpty()) {
            return null;
        }

        int x = -1;
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            if (domainType.isInstance(argument)) {
                x = i;
                break;
            }
        }
        return x >= 0 ? getDomainGetter(seedFieldOpt.get(), domainType).invoke(arguments[x]) : null;
    }

    @Nullable
    public static Object fromMethodArguments(Object[] arguments, Annotation[][] annotations) {
        if (arguments.length != annotations.length) {
            return null;
        }

        int x = -1;
        point:
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof RoutingSeed) {
                    x = i;
                    break point;
                }
            }
        }
        return x >= 0 ? arguments[x] : null;
    }

    @Nullable
    public static RoutingSeed fromMethodDeclaration(Method method) {
        MergedAnnotation<RoutingSeed> mergedAnnotation = METHOD_ANNOTATION_CACHE
            .computeIfAbsent(method, _method ->
                MergedAnnotations.from(_method).get(RoutingSeed.class)
            );
        return mergedAnnotation.isPresent() ? mergedAnnotation.synthesize() : null;
    }

    @Nullable
    public static RoutingSeed fromClassDeclaration(Class<?> clazz) {
        MergedAnnotation<RoutingSeed> mergedAnnotation = CLASS_ANNOTATION_CACHE
            .computeIfAbsent(clazz, _clazz ->
                MergedAnnotations.from(_clazz).get(RoutingSeed.class)
            );
        return mergedAnnotation.isPresent() ? mergedAnnotation.synthesize() : null;
    }

    private static Optional<Field> getDomainField(Class<?> domainType) {
        return DOMAIN_FIELD_CACHE.computeIfAbsent(domainType, clazz -> {
            Field seedField = ReflectionUtils.findField(clazz,
                new ReflectionUtils.DescribedFieldFilter() {
                    @Nonnull
                    @Override
                    public String getDescription() {
                        return "@RoutingSeed must be unique";
                    }

                    @Override
                    public boolean matches(@Nonnull Field field) {
                        return field.isAnnotationPresent(RoutingSeed.class);
                    }
                }, true);
            return Optional.ofNullable(seedField);
        });
    }

    private static Method getDomainGetter(@Nonnull Field seedField, Class<?> domainType) {
        return DOMAIN_GETTER_CACHE.computeIfAbsent(domainType, clazz -> {
            try {
                return new PropertyDescriptor(seedField.getName(), clazz).getReadMethod();
            } catch (IntrospectionException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        });
    }
}
