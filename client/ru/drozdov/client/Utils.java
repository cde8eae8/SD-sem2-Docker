package ru.drozdov.client;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

class Loop {
    private final Map<String, Command> commands;

    Loop(Map<String, Command> commands) {
        this.commands = new HashMap<>(commands);
        this.commands.putIfAbsent("help", new Command(this::help, "prints this text"));
    }

    void loop() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print(">> ");
            String line = sc.nextLine();
            var args = new ArrayList<>(Arrays.asList(line.split(" +")));
            var command = args.get(0);
            if (Objects.equals(command, "quit")) {
                break;
            }
            if (commands.containsKey(command)) {
                args.remove(0);
                commands.get(command).runner.accept(args);
            } else {
                System.out.printf("Command %s not found\n", command);
                if (command.contains("help")) {
                    commands.get("help").runner.accept(List.of());
                }
            }
        }
    }

    private void help(List<String> args) {
        for (var command : commands.entrySet()) {
            System.out.printf("%s: %s\n", command.getKey(), command.getValue().description);
        }
    }
}

class Command {
    Command(Consumer<List<String>> runner, String description) {
        this.runner = runner;
        this.description = description;
    }


    final public Consumer<List<String>> runner;
    final public String description;
}
