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
    final static Pattern childSelect = Pattern.compile("`*<(\\d)>");
    final static Pattern childrenSelect = Pattern.compile("`*<(\\d)\\+>");
    final static Pattern prioritySelect = Pattern.compile("\\((\\d)\\|([^)]+)\\)");
    final static String[] registers = {"eax", "ebx", "ecx", "edx", "esi", "edi", "ebp", "esp", "ax", "bx", "cx", "dx", "si", "di", "bp", "sp", "ah", "al", "bh", "bl", "ch", "cl", "dh", "dl"};
    public static void translate(Node entry, File outfile) {
        try {
            PrintWriter out = new PrintWriter(outfile);
            Queue<String>[] sections = new Queue[3];
            for(int i = 0; i < sections.length; i++) {
                sections[i] = new LinkedList<>();
            }
            recursiveParse(entry, sections);
            out.println("extern _printf");
            out.println("global _main");
            out.println("section .data");
            while (!sections[2].isEmpty()) {
                out.print(sections[2].poll());
            }
            out.println("\nsection .bss");
            while (!sections[1].isEmpty()) {
                out.print(sections[1].poll());
            }
            out.println("\nsection .text");
            while (!sections[0].isEmpty()) {
                out.print(sections[0].poll());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recursiveParse (Node entry, Queue<String>[] sections) { //0 is .text, 1 is .bss, 2 is .data
        if(entry.asm != null) {
            Matcher priority = prioritySelect.matcher(entry.asm);
            entry.asm = entry.asm.replaceAll("\\{\\$}", entry.getValue().toString()); //replace {} values with node data
            entry.outputregister = entry.outputregister.replaceAll("\\{\\$}", entry.getValue().toString());
            if (priority.lookingAt()) {
                for(int i = 1; i < Integer.parseInt(priority.group(1)); i++) {
                    recursiveParse(entry.getChildren().get(i-1), sections);
                }
                String tmp = entry.asm;
                entry.asm = priority.group(2);
                parse(entry, sections);
                entry.asm = tmp.substring(priority.end());
                recursiveParse(entry, sections);
                if(entry.asm != null) {
                    parse(entry, sections);
                }
                return;
            }
        }
        for(Node child : entry.getChildren()) {
            recursiveParse(child, sections);
        }
        if(entry.asm != null) {
            parse(entry, sections);
        }
    }
    
    private static void parse(Node entry, Queue[] sections) {
        int mode = 0;
        while (entry.asm.length() > 0) {
            Matcher switchMatch = sectionSwitcher.matcher(entry.asm);
            Matcher childMatch = childSelect.matcher(entry.asm);
            Matcher childrenMatch = childrenSelect.matcher(entry.asm);

            if (switchMatch.lookingAt()) {
                mode = Integer.parseInt(switchMatch.group(1));
                entry.asm = entry.asm.substring(switchMatch.end());
            } else if (childrenMatch.lookingAt()) {
                Node branch = new Node();
                for(int i = Integer.parseInt(childrenMatch.group(1)) - 1; i < entry.getChildren().size(); i++) {
                    branch.addChild(entry.getChildren().get(i));
                }
                recursiveParse(branch, sections);
                entry.asm = entry.asm.substring(childrenMatch.end());
            } else if (childMatch.lookingAt()) {
                if(entry.asm.charAt(0) == '`' && !isRegister(entry.getChildren().get(Integer.parseInt(childMatch.group(1)) - 1).outputregister)) {
                    sections[mode].offer("[" + entry.getChildren().get(Integer.parseInt(childMatch.group(1)) - 1).outputregister + "]");
                }
                else {
                    sections[mode].offer(entry.getChildren().get(Integer.parseInt(childMatch.group(1)) - 1).outputregister);
                }
                entry.asm = entry.asm.substring(childMatch.end());
            } else {
                sections[mode].offer(entry.asm.substring(0, 1));
                entry.asm = entry.asm.substring(1);
            }
        }
    }

    private static boolean isRegister(String outputregister) {
        for(String reg : registers) {
            if(outputregister.equals(reg)) return true;
        }
        return false;
    }
}