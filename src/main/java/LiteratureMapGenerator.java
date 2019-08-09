import org.jbibtex.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteratureMapGenerator {

    static class LiteratureMapEntry implements Comparable<LiteratureMapEntry> {
        String literatureName;
        String fileName;

        String generateMarkdownLink(String documentRootDirectory) {
            if (fileName == null) {
                return literatureName;
            }

            Pattern pattern = Pattern.compile(":(.*?):");
            Matcher matcher = pattern.matcher(fileName);

            if (!matcher.find()) {
                return literatureName;
            }

            var encodedFileName = encodeURIComponent(matcher.group(1));
            var fullFilePath = documentRootDirectory + encodedFileName;
            return "[" + literatureName + "](" + fullFilePath + ")";
        }

        LiteratureMapEntry(String literatureName, String fileName) {
            assert literatureName != null;
            this.literatureName = literatureName;
            this.fileName = fileName;
        }

        @Override
        public int compareTo(LiteratureMapEntry o) {
            return this.literatureName.compareTo(o.literatureName);
        }
    }

    public static void main(String[] args) throws ParseException, IOException {
        if (args.length != 3) {
            System.err.println("Call program as following:");
            System.err.println("program.jar inputFilePath groupingKey literatureRootPath");
            System.exit(-1);
        }
        var literatureRootFolder = args[2];
        var pathToBib = args[0];
        var groupingKeyString = args[1];
        var groupingKey = new Key(groupingKeyString);
        var fileKey = new Key("file");
        var naKeyString = "N/A";
        var fr = new FileReader(pathToBib);

        var bibtexParser = new BibTeXParser();
        var database = bibtexParser.parse(fr);
        fr.close();

        var entries = database.getEntries();

        var literatureMap = new TreeMap<String, SortedSet<LiteratureMapEntry>>();

        for (BibTeXEntry bibTeXEntry : entries.values()) {
            Value belongingGroup = bibTeXEntry.getField(groupingKey);
            String belongingGroupString = naKeyString;

            Value file = bibTeXEntry.getField(fileKey);
            Value title = bibTeXEntry.getField(BibTeXEntry.KEY_TITLE);

            if (title == null) {
                System.err.println("No title found");
                continue;
            }

            if (belongingGroup == null) {
                System.err.println("No belonging group key found for literature: " + title.toUserString());
            } else {
                belongingGroupString = belongingGroup.toUserString();
            }

            if (!literatureMap.containsKey(belongingGroupString)) {
                literatureMap.put(belongingGroupString, new TreeSet<>());
            }

            String fileName = file != null ? file.toUserString() : null;

            literatureMap.get(belongingGroupString).add(new LiteratureMapEntry(title.toUserString(), fileName));
        }

        StringBuilder markdownTableSB = new StringBuilder();
        markdownTableSB.append("| Literature Scope | Literatue |\n");
        markdownTableSB.append("| --- | --- |\n");

        for (String literatureMapKey : literatureMap.keySet()) {
            if (literatureMapKey.equals(naKeyString)) {
                continue;
            }
            appendMarkdownTableRowToLiteratureMapTable(literatureMapKey, literatureMap.get(literatureMapKey), markdownTableSB, literatureRootFolder);
        }

        if (literatureMap.containsKey(naKeyString)) {
            appendMarkdownTableRowToLiteratureMapTable(naKeyString, literatureMap.get(naKeyString), markdownTableSB, literatureRootFolder);
        }

        System.out.println(markdownTableSB.toString());
    }

    private static void appendMarkdownTableRowToLiteratureMapTable(String literatureMapKey, SortedSet<LiteratureMapEntry> literatureMapEntries, StringBuilder markdownTableSB, String literatureRootFolder) {
        markdownTableSB.append("| **").append(literatureMapKey).append("** |   |\n");
        for (LiteratureMapEntry literatureMapEntry : literatureMapEntries) {
            markdownTableSB.append("|   | ").append(literatureMapEntry.generateMarkdownLink(literatureRootFolder)).append(" |\n");
        }
    }

    private static String encodeURIComponent(String s) {
        String result;

        result = URLEncoder.encode(s, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");

        return result;
    }
}
