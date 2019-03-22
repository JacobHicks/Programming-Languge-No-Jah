package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
    final static Pattern sectionSwitcher = Pattern.compile("\\[(\\d)]");
    final static Pattern childSelect = Pattern.compile("<(\\d)>");
    public static void translate(Node entry, File outfile) {
        try {
            PrintWriter out = new PrintWriter(outfile);
            Queue<String>[] sections = new Queue[3];
            for(int i = 0; i < sections.length; i++) {
                sections[i] = new LinkedList<>();
            }
            recursiveParse(entry, sections);

            out.print("section .text");
            while (!sections[0].isEmpty()) {
                out.print(sections[0].poll());
            }
            out.println("\nsection .bsd");
            while (!sections[1].isEmpty()) {
                out.print(sections[1].poll());
            }
            out.println("\nsection .data");
            while (!sections[2].isEmpty()) {
                out.print(sections[2].poll());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void recursiveParse (Node entry, Queue<String>[] sections) { //0 is .text, 1 is .bsd, 2 is .data
        for(Node child : entry.getChildren()) {
            recursiveParse(child, sections);
        }
        entry.asm = entry.asm.replaceAll("\\{\\$}", entry.getValue().toString()); //replace {} values with node data
        entry.outputregister = entry.outputregister.replaceAll("\\{\\$}", entry.getValue().toString());

        int mode = 0;

        while (entry.asm.length() > 0) {
            Matcher switchMatch = sectionSwitcher.matcher(entry.asm);
            Matcher childMatch = childSelect.matcher(entry.asm);

            if (switchMatch.lookingAt()) {
                mode = Integer.parseInt(switchMatch.group(1));
                entry.asm = entry.asm.substring(switchMatch.end());
            } else if (childMatch.lookingAt()) {
                sections[mode].offer(entry.getChildren().get(Integer.parseInt(childMatch.group(1)) - 1).outputregister);
                entry.asm = entry.asm.substring(childMatch.end());
            } else {
                int spaceindex = entry.asm.indexOf(' ') + 1;
                int newlineindex = entry.asm.indexOf('\n') + 1;
                if (spaceindex == 0) spaceindex = entry.asm.length();
                if (newlineindex == 0) newlineindex = entry.asm.length();
                int newdex = Math.min(spaceindex, newlineindex);
                sections[mode].offer(entry.asm.substring(0, newdex));
                entry.asm = entry.asm.substring(newdex);
            }
        }
    }
}
