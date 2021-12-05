package cn.fantasticmao.mundo.core.support;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MapBuilderTest
 *
 * @author fantasticmao
 * @version 1.0
 * @since 2018/1/8
 */
public class MapBuilderTest {

    @Test
    public void put() {
        Map<String, Object> map = MapBuilder.<String, Object>create()
            .put("one", 1)
            .put("two", 2)
            .put("three", 3)
            .put("four", 4)
            .build();
        System.out.println(map);
    }

    @Test
    public void putIfKeyAbsent() {
        Map<String, Object> map = MapBuilder.<String, Object>create(4)
            .put("one", 1)
            .putIfAbsent("aaa", 1)
            .putIfKeyAbsent("one", 2)
            .putIfValAbsent("two", 2)
            .build();
        System.out.println(map);
    }

    @Test
    public void putIfNonNull() {
        Map<String, Object> map = MapBuilder.<String, Object>create(5)
            .put("one", 1)
            .putIfNonNull("two", null)
            .putIfKeyNonNull(null, 3)
            .putIfValNonNull("two", 2)
            .build();
        System.out.println(map);
    }

    @Test
    public void ifPut() {
        Map<String, Object> map = MapBuilder.<String, Object>create(LinkedHashMap::new)
            .put("one", 1)
            .putIf("two", 2, (k, v) -> false)
            .putIf("three", 2, (k, v) -> true)
            .put("four", 4)
            .build();
        System.out.println(map);
    }

}