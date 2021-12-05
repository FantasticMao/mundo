package cn.fantasticmao.mundo.core.support;

import cn.fantasticmao.mundo.core.util.JsonUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.Builder;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * MapBuilder
 *
 * @author fantasticmao
 * @version 1.0
 * @since 2017/3/5
 */
@NotThreadSafe
public class MapBuilder<K, V> implements Builder<Map<K, V>> {
    private Map<K, V> map;

    private MapBuilder() {
        throw new AssertionError("No MapBuilder instances for you!");
    }

    private MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public static <K, V> MapBuilder<K, V> create() {
        return create(HashMap::new);
    }

    public static <K, V> MapBuilder<K, V> create(int capacity) {
        return create(() -> new HashMap<K, V>(capacity));
    }

    public static <K, V> MapBuilder<K, V> create(Supplier<Map<K, V>> supplier) {
        return new MapBuilder<>(supplier.get());
    }

    public MapBuilder<K, V> put(K k, V v) {
        map.put(k, v);
        return this;
    }

    public MapBuilder<K, V> putIfAbsent(K k, V v) {
        return putIf(k, v, (key, val) -> !map.keySet().contains(key) && !map.values().contains(val));
    }

    public MapBuilder<K, V> putIfKeyAbsent(K k, V v) {
        return putIf(k, v, (key, val) -> !map.keySet().contains(key));
    }

    public MapBuilder<K, V> putIfValAbsent(K k, V v) {
        return putIf(k, v, (key, val) -> !map.values().contains(val));
    }

    public MapBuilder<K, V> putIfNonNull(K k, V v) {
        return putIf(k, v, ObjectUtils::allNotNull);
    }

    public MapBuilder<K, V> putIfKeyNonNull(K k, V v) {
        return putIf(k, v, (key, val) -> Objects.nonNull(key));
    }

    public MapBuilder<K, V> putIfValNonNull(K k, V v) {
        return putIf(k, v, (key, val) -> Objects.nonNull(val));
    }

    public MapBuilder<K, V> putIf(K k, V v, BiPredicate<K, V> biPredicate) {
        if (biPredicate.test(k, v)) {
            map.put(k, v);
        }
        return this;
    }

    @Override
    public Map<K, V> build() {
        return map;
    }

    public String toJson() {
        return JsonUtil.toJson(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
