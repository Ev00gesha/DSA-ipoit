package by.it.group410972.rak.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class SourceScannerC {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        List<Path> javaFiles = new ArrayList<>();
        collectJavaFiles(Paths.get(src), javaFiles);

        Map<Path, String> processedFiles = new TreeMap<>();
        for (Path file : javaFiles) {
            String content = readFile(file);
            if (content == null) continue;
            if (content.contains("@Test") || content.contains("org.junit.Test")) continue;
            String cleaned = cleanJavaContent(content);
            processedFiles.put(file, cleaned);
        }

        Map<Path, List<Path>> duplicates = findDuplicates(processedFiles);

        for (Path original : duplicates.keySet()) {
            System.out.println(original);
            for (Path copy : duplicates.get(original)) {
                System.out.println(copy);
            }
        }
    }

    private static void collectJavaFiles(Path dir, List<Path> files) throws IOException {
        if (!Files.exists(dir)) return;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    collectJavaFiles(entry, files);
                } else if (entry.toString().endsWith(".java")) {
                    files.add(entry);
                }
            }
        }
    }

    private static String readFile(Path file) {
        try {
            byte[] bytes = Files.readAllBytes(file);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            try {
                byte[] bytes = Files.readAllBytes(file);
                return new String(bytes, Charset.forName("ISO-8859-1"));
            } catch (IOException ex) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static String cleanJavaContent(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        boolean inBlockComment = false;
        boolean inLineComment = false;
        int len = text.length();

        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);

            if (inBlockComment) {
                if (c == '*' && i + 1 < len && text.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }

            if (inLineComment) {
                if (c == '\n') inLineComment = false;
                else continue;
            }

            if (c == '/' && i + 1 < len) {
                char next = text.charAt(i + 1);
                if (next == '/') {
                    inLineComment = true;
                    i++;
                    continue;
                } else if (next == '*') {
                    inBlockComment = true;
                    i++;
                    continue;
                }
            }

            sb.append(c);
        }

        String result = sb.toString();
        result = result.replaceAll("(?m)^\\s*package\\s+.*?;", "");
        result = result.replaceAll("(?m)^\\s*import\\s+.*?;", "");
        result = result.replaceAll("[\\x00-\\x20]+", " ");
        return result.trim();
    }

    private static Map<Path, List<Path>> findDuplicates(Map<Path, String> files) {
        Map<Path, List<Path>> result = new LinkedHashMap<>();
        List<Path> keys = new ArrayList<>(files.keySet());
        int n = keys.size();

        for (int i = 0; i < n; i++) {
            Path a = keys.get(i);
            String contentA = files.get(a);
            List<Path> copies = new ArrayList<>();
            for (int j = i + 1; j < n; j++) {
                Path b = keys.get(j);
                String contentB = files.get(b);
                if (levenshteinDistance(contentA, contentB) < 10) {
                    copies.add(b);
                }
            }
            if (!copies.isEmpty()) result.put(a, copies);
        }
        return result;
    }

    private static int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        for (int j = 0; j <= len2; j++) prev[j] = j;

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            for (int j = 1; j <= len2; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[len2];
    }
}
