package plik;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        String file = Files.readString(Path.of("test.java"));

        Map<String, Integer> countMap = new HashMap<>();

        String file2 = file.replaceAll("((?:\\/\\*(?:.|[\\n\\r])*?\\*\\/)|(\\/\\*[\\s\\S]*$))", "");
        String[] lines = file2.split("\n");

        String regex =
                "(?<!\"{1})(?<!\".*\".*\".*)(?<!\".*\".*\".*\".*\".*)(?<!\".*\".*\".*\".*\".*\".*\".*)" +
                        "(?<!\\/\\/.*)(String|int|boolean|byte|short|long|float|double|char)\\s+(\\w+)\\s*=\\s*(\"(?:\\\\\"|[^\"])*\"|'(?:\\\\'|[^'])*'|[^;]+?)(;|\s*\\+)";
        Pattern pattern = Pattern.compile(regex);
        for (String s: lines) {

            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                String match = matcher.group();
                String[] strings = match.split("\s");
                countMap.put(strings[0], countMap.getOrDefault(strings[0], 0) + 1);
            }
        }

        countMap.entrySet().forEach(
                System.out::println
        );
    }
}
