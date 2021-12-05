package cn.fantasticmao.mundo.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JsonUtil
 *
 * @author fantasticmao
 * @version 1.0
 * @since 2017/3/5
 */
public final class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Object -&gt; JSON
     *
     * @param obj object
     * @return JSON
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JSON -&gt; Class
     *
     * @param json  JSON
     * @param clazz class
     * @param <T>   Type
     * @return Object
     */
    public static <T> T toClass(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json.getBytes(), clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JSON Node -&gt; Class
     *
     * @param node  JSON Node
     * @param clazz class
     * @param <T>   Type
     * @return Object
     */
    public static <T> T toClass(JsonNode node, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JSON -&gt; JSON Node -&gt; Class
     *
     * @param json      JSON
     * @param fieldName NODE field
     * @param clazz     class
     * @param <T>       Type
     * @return Object
     */
    public static <T> T findNodeToClass(String json, String fieldName, Class<T> clazz) {
        JsonNode node = toJsonNode(json, fieldName);
        if (node != null && !node.isMissingNode()) {
            return toClass(node, clazz);
        } else {
            return null;
        }
    }

    /**
     * JSON -&gt; JSON Node
     *
     * @param json JSON
     * @return JSON Node
     */
    public static JsonNode toJsonNode(String json) {
        if (StringUtils.isNotEmpty(json)) {
            try {
                return OBJECT_MAPPER.readTree(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * JSON -&gt; JSON Node
     *
     * @param json      JSON
     * @param fieldName Node field
     * @return JSON Node
     */
    public static JsonNode toJsonNode(String json, String fieldName) {
        if (StringUtils.isNoneEmpty(json, fieldName)) {
            try {
                return OBJECT_MAPPER.readTree(json).findPath(fieldName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * JSON -&gt; LinkedHashMap
     *
     * @param json JSON
     * @return Map
     */
    public static Map toMap(String json) {
        return toClass(json, Map.class);
    }

    /**
     * JSON -&gt; ArrayList
     *
     * @param json JSON
     * @return List
     */
    public static List toList(String json) {
        return toClass(json, List.class);
    }

    /**
     * JSON -&gt; HashSet
     *
     * @param json JSON
     * @return Set
     */
    public static Set toSet(String json) {
        return toClass(json, Set.class);
    }
}
