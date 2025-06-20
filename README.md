# XmlToJsonConverterLibrary
Xml to Json Library with Java

# AdapterXmlToJson

Generic Java library to convert XML data to JSON via POJO classes using the Adapter pattern.

## Features
- No external dependencies
- Supports primitive types and String fields
- Uses reflection for generic class mapping
- Lightweight and efficient parsing with regex

## Usage

```java
        String xml = """
                <User>
                    <name>Doğukan</name>
                    <age>25</age>
                    <active>true</active>
                </User>
                """;

        AdapterXmlToJson<User> adapter = new AdapterXmlToJson<>(User.class);
        String json = adapter.convertXmlToJson(xml);
        System.out.println(json);
