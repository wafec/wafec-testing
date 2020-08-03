package wafec.testing.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CharsUtils {
    public static List<Character> toList(char[] charArray) {
        if (charArray == null)
            return null;
        List<Character> result = new ArrayList<>();
        for (char c : charArray) {
            result.add(c);
        }
        return result;
    }

    public static List<String> listString(List<Character> charList) {
        if (charList == null)
            return null;
        return charList.stream().map(Object::toString).collect(Collectors.toList());
    }
}
