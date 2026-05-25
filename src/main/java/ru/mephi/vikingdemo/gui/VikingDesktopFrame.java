package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;
import ru.mephi.vikingdemo.service.VikingServiceAnalyzer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingServiceAnalyzer analyzer;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService, VikingServiceAnalyzer analyzer) {
        this.vikingService = vikingService;
        this.analyzer = analyzer;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 480));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);


        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(e -> onCreateViking());


        JButton massCreateButton = new JButton("Create many vikings (100)");
        massCreateButton.addActionListener(e -> {
            List<Viking> army = vikingService.generateManyRandomVikings(100);
            army.forEach(tableModel::addViking);
            JOptionPane.showMessageDialog(this, "Создано " + army.size() + " викингов", "Генерация", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton analyticsButton = new JButton("Analytics");
        analyticsButton.addActionListener(e -> showAnalyticsDialog());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(massCreateButton);
        bottomPanel.add(analyticsButton);
        add(bottomPanel, BorderLayout.SOUTH);

        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        if (!all.isEmpty()) {
            all.forEach(tableModel::addViking);
        }
    }

    private void showAnalyticsDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();

        JRadioButton opt1 = new JRadioButton("1) Викинги старше 20 лет");
        JRadioButton opt2 = new JRadioButton("2) Викинги младше 20 лет");
        JRadioButton opt3 = new JRadioButton("3) Викинги в возрасте от 20 до 40 лет");
        JRadioButton opt4 = new JRadioButton("4) Викинги вне возраста 20-40 лет");
        JRadioButton opt5 = new JRadioButton("5) Светлые с длинной бородой");
        JRadioButton opt6 = new JRadioButton("6) Имеют 1 или 2 топора");
        JRadioButton opt8 = new JRadioButton("8) Случайный великан (рост > 180 см)");
        JRadioButton opt9 = new JRadioButton("9) Викинги с легендарным снаряжением");
        JRadioButton opt10 = new JRadioButton("10) Рыжая борода, сортировка по возрасту");
        JRadioButton opt11 = new JRadioButton("11) Максимальный ID");
        JRadioButton opt12 = new JRadioButton("12) Все чётные ID");

        group.add(opt1);
        group.add(opt2);
        group.add(opt3);
        group.add(opt4);
        group.add(opt5);
        group.add(opt6);
        group.add(opt8);
        group.add(opt9);
        group.add(opt10);
        group.add(opt11);
        group.add(opt12);

        panel.add(new JLabel("Выберите тип анализа:"));
        panel.add(opt1);
        panel.add(opt2);
        panel.add(opt3);
        panel.add(opt4);
        panel.add(opt5);
        panel.add(opt6);
        panel.add(opt8);
        panel.add(opt9);
        panel.add(opt10);
        panel.add(opt11);
        panel.add(opt12);

        int result = JOptionPane.showConfirmDialog(this, panel, "Аналитика", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String output = "";

            if (opt1.isSelected()) {
                long count = analyzer.countByAgeGreaterThan(31);
                output = "Викингов старше 20 лет: " + count;
            } else if (opt2.isSelected()) {
                long count = analyzer.countByAgeLessThan(31);
                output = "Викингов младше 20 лет: " + count;
            } else if (opt3.isSelected()) {
                long count = analyzer.countByAgeBetween(20, 40);
                output = "Викингов в возрасте 20-40 лет: " + count;
            } else if (opt4.isSelected()) {
                long count = analyzer.countByAgeOutside(20, 40);
                output = "Викингов вне возраста 20-40 лет: " + count;
            } else if (opt5.isSelected()) {
                long count = analyzer.countByBeardAndHair(BeardStyle.SHORT, HairColor.Blond);
                output = "Светлых с короткой бородой: " + count;
            } else if (opt6.isSelected()) {
                output = "Викингов с 1 л 2 топорами: " + analyzer.countWithOneOrTwoAxes();

            } else if (opt8.isSelected()) {
                output = analyzer.getRandomVikingTallerThan(180)
                        .map(v -> "Случайный великан: " + v.name() + " (рост " + v.heightCm() + " см)")
                        .orElse("Викингов ростом выше 180 см не найдено");
            } else if (opt9.isSelected()) {
                List<Viking> legendary = analyzer.getVikingsWithLegendaryEquipment();
                output = "Викинги с легендарным снаряжением: " + legendary.stream()
                        .map(Viking::name)
                        .collect(Collectors.joining(", "));
            } else if (opt10.isSelected()) {
                List<Viking> red = analyzer.getRedHairedVikingsSortedByAge();
                output = "Рыжие викинги (по возрасту):\n" + red.stream()
                        .map(v -> v.name() + " (" + v.age() + " лет)")
                        .collect(Collectors.joining("\n"));
            } else if (opt11.isSelected()) {
                output = "Максимальный ID: " + analyzer.getMaxId();
            } else if (opt12.isSelected()) {
                output = "Чётные ID: " + analyzer.getEvenIds();
            }

            JOptionPane.showMessageDialog(this, output, "Результат анализа", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
