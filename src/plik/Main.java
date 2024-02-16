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

        String regex = "(?<!\\/\\/.*)(?<!\\/\\*.*)(String|int|boolean|byte|short|long|float|double|char)\\s+(\\w+)\\s*=\\s*(\"(?:\\\\\"|[^\"])*\"|'(?:\\\\'|[^'])*'|[^;]+?);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(file);

        while (matcher.find()) {
            String type = matcher.group(1);
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        countMap.entrySet().forEach(
                System.out::println
        );
    }
}
