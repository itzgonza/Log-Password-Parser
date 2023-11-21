package io.github.itzgonza.impl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PasswordParser {

	public transient static PasswordParser instance;
	
    private static List<String> founds = new ArrayList<>();
    private static String path;
    private static String type;

    public PasswordParser() {
    	path = System.getProperty("user.home") + "/Desktop/logs";
    }

    public void initialize() {
        System.out.println("\ntype a site\n");
        
        type = new Scanner(System.in).nextLine();
        if (type.isEmpty())
        	return;

        try {
            parse(type);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        printResults();
    }

    private static void parse(String focus) throws Exception {
        System.out.printf("\nsearching %s accounts..\n\n", type);

        Files.walk(Paths.get(path)).filter(Files::isRegularFile)
                .filter(path -> path.toString().contains("password")).forEach(file -> {
                    try {
                        String read = new String(Files.readAllBytes(file), "utf-8");

                        String[] split = read.split("\n");
                        for (int i = 0; i < split.length; ++i) {
                            String replacer = split[i].trim();
                            if (replacer.length() >= 3) {
                                String total = replacer.substring(0, 3).toLowerCase() + replacer.substring(3).trim();
                                if (total.startsWith("url:") & total.contains(focus)) {
                                    String username = split[i + 1].substring(split[i + 1].indexOf(":") + 1).trim();
                                    String password = split[i + 2].substring(split[i + 2].indexOf(":") + 1).trim();
                                    if (username.length() > 1 & password.length() > 1) {
                                        if (username.contains("empty") | password.contains("empty"))
                                            continue;
                                        founds.add((username + ":" + password));
                                    }
                                }
                            }
                        }
                    } catch (Exception ignored) {}
                });
    }

    private static void printResults() {
        Runnable action = () -> {
            if (!founds.isEmpty()) {
                founds = founds.stream().distinct().collect(Collectors.toList());

                founds.forEach(System.out::println);
                
                System.out.print("\ntotal found (" + founds.size() + ")");
                return;
            }

            System.out.print("not found (founds == 0)");
        };

        Optional.ofNullable(action).ifPresent(Runnable::run);
    }

}