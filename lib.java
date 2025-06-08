package adapter;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;

public class AdapterXmlToJson<T> {

    private final Class<T> targetClass;

    public AdapterXmlToJson(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public String convertXmlToJson(String xmlData) throws Exception {
        T obj = parseXmlToObject(xmlData, targetClass);
        return convertObjectToJson(obj);
    }

    private T parseXmlToObject(String xml, Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            String tag = field.getName();
            Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(xml);

            if (matcher.find()) {
                String value = matcher.group(1).trim();
                Object castedValue = castValue(field.getType(), value);
                field.set(instance, castedValue);
            }
        }

        return instance;
    }

    private Object castValue(Class<?> type, String value) {
        if (type == int.class || type == Integer.class)
            return Integer.parseInt(value);
        if (type == long.class || type == Long.class)
            return Long.parseLong(value);
        if (type == double.class || type == Double.class)
            return Double.parseDouble(value);
        if (type == boolean.class || type == Boolean.class)
            return Boolean.parseBoolean(value);
        if (type == String.class)
            return value;
        return null; // Gelişmiş cast için recursive yapı gerekebilir
    }

    private String convertObjectToJson(Object obj) throws Exception {
        StringBuilder json = new StringBuilder();
        json.append("{");

        Field[] fields = obj.getClass().getDeclaredFields();
        List<String> jsonFields = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            Object val = field.get(obj);
            String jsonKey = "\"" + field.getName() + "\"";
            String jsonValue = toJsonValue(val);
            jsonFields.add(jsonKey + " : " + jsonValue);
        }

        json.append(String.join(", ", jsonFields));
        json.append("}");

        return json.toString();
    }

    private String toJsonValue(Object val) {
        if (val == null) return "null";
        if (val instanceof String) return "\"" + escapeJson(val.toString()) + "\"";
        if (val instanceof Number || val instanceof Boolean) return val.toString();
        // Nested object desteklenebilir
        return "\"" + escapeJson(val.toString()) + "\"";
    }

    private String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
