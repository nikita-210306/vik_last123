package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VikingServiceAnalyzer {

    private final VikingStorage vikingStorage;
    private final Random random = new Random();

    public VikingServiceAnalyzer(VikingStorage vikingStorage) {
        this.vikingStorage = vikingStorage;
    }



    public long countByAgeGreaterThan(int age) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() > age)
                .count();
    }

    public long countByAgeLessThan(int age) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() < age)
                .count();
    }

    public long countByAgeBetween(int minAge, int maxAge) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() >= minAge && v.age() <= maxAge)
                .count();
    }

    public long countByAgeOutside(int minAge, int maxAge) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() < minAge || v.age() > maxAge)
                .count();
    }

    public long countByBeardAndHair(BeardStyle beard, HairColor hair) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.beardStyle() == beard && v.hairColor() == hair)
                .count();
    }

    /**
     * Подсчёт викингов с определённым количеством топоров в снаряжении.
     * Топор определяется по названию, содержащему "axe" (без учёта регистра).
     */
    public long countByAxeQuantity(int expectedAxesCount) {
        return vikingStorage.findAll().stream()
                .filter(v -> {
                    long axes = v.equipment().stream()
                            .filter(e -> e.name().toLowerCase().contains("axe"))
                            .count();
                    return axes == expectedAxesCount;
                })
                .count();
    }



    public Optional<Viking> getRandomVikingTallerThan(int heightCm) {
        List<Viking> tallVikings = vikingStorage.findAll().stream()
                .filter(v -> v.heightCm() > heightCm)
                .collect(Collectors.toList());
        if (tallVikings.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tallVikings.get(random.nextInt(tallVikings.size())));
    }

    public List<Viking> getVikingsWithLegendaryEquipment() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(e -> "Legendary".equalsIgnoreCase(e.quality())))
                .collect(Collectors.toList());
    }

    public List<Viking> getRedHairedVikingsSortedByAge() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Red)
                .sorted(Comparator.comparingInt(Viking::age))
                .collect(Collectors.toList());
    }



    public Optional<Integer> getMaxId() {
        return vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(Objects::nonNull)
                .max(Integer::compareTo);
    }

    public List<Integer> getEvenIds() {
        return vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(id -> id != null && id % 2 == 0)
                .collect(Collectors.toList());
    }
}
