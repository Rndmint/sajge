package io.github.sajge.desktop.tables.demo;

import io.github.sajge.desktop.tables.*;
import io.github.sajge.desktop.tables.chain.*;
import io.github.sajge.desktop.tables.patterns.TableHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TableManagerDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Table Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            List<Person> people = Arrays.asList(
                    new Person("dude", 30),
                    new Person("yo", 25),
                    new Person("Carol", 28),
                    new Person("some guy", 27)
            );

            JTable table = new JTable();
            JScrollPane scroll = new JScrollPane(table);
            frame.add(scroll, BorderLayout.CENTER);

            TableHandlerContext<Person> ctx = new TableHandlerContext<>(table, people, null);
            TableHandler<Person> chain = new TableChainBuilder<Person>()
                    .add(new ModelHandler<>(
                            new String[]{"Name", "Age", "Nothing"},
                            p -> new Object[]{
                                    p.getName(),
                                    p.getAge(),
                                    null
                            }))
                    .add(new ColumnWidthHandler<>(200, 100))
                    .add(new RendererHandler<>(1, table.getDefaultRenderer(Integer.class)))
                    .add(new SelectionHandler<>(ListSelectionModel.SINGLE_SELECTION))
                    .add(new RowClickHandler<>(row -> System.out.println("Clicked: " + people.get(row))))
                    .build();
            chain.handle(ctx);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return name + " (" + age + ")";
        }
    }
}
