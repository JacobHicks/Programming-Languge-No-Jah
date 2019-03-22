package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
    final static Pattern sectionSwitcher = Pattern.compile("\\[(\\d)].*");
    final static Pattern childSelect = Pattern.compile("<(\\d)>.*");
    public static void translate(Node entry, File outfile) {
        try {
            PrintWriter out = new PrintWriter(outfile);
            Queue<String>[] sections = new Queue[3];
            for(int i = 0; i < sections.length; i++) {
                sections[i] = new LinkedList<>();
            }
            recursiveParse(entry, sections);

            out.println("section .text");
            while (!sections[0].isEmpty()) {
                out.println(sections[0].poll());
            }
            out.flush();
            out.println("section .bsd");
            while (!sections[1].isEmpty()) {
                out.println(sections[1].poll());
            }
            out.flush();
            out.println("section .data");
            while (!sections[2].isEmpty()) {
                out.println(sections[2].poll());
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
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

            } else if (childMatch.lookingAt()) {
                sections[mode].offer(entry.getChildren().get(Integer.parseInt(childMatch.group(1))).outputregister);
                entry.asm = entry.asm.substring(childMatch.end());
            } else {
                sections[mode].offer(entry.asm.substring(0, Math.min(entry.asm.indexOf(' '), entry.asm.indexOf('\n'))));
                entry.asm = entry.asm.substring(0, Math.min(entry.asm.indexOf(' '), entry.asm.indexOf('\n')));
            }
        }
    }
}
