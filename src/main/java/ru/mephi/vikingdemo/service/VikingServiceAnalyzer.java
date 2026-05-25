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
    public long countWithOneOrTwoAxes() {
        return vikingStorage.findAll().stream()
                .filter(v -> {
                    long axes = v.equipment().stream()
                            .filter(e -> e.name().equalsIgnoreCase("axe"))
                            .count();
                    return axes == 1 || axes==2;
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
                .filter(v -> v.beardStyle() != BeardStyle.CLEAN_SHAVEN)
                .sorted(Comparator.comparingInt(Viking::age))
                .collect(Collectors.toList());
    }



    public Optional<Integer> getMaxId() {
        Integer[] ids = vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(Objects::nonNull)
                .toArray(Integer[]::new);
        if (ids.length == 0) {
            return Optional.empty();
        }
        return Optional.of(Arrays.stream(ids).max(Integer::compareTo).get());
    }

    public Integer[] getEvenIds() {
        Integer[] ids = vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(Objects::nonNull)
                .toArray(Integer[]::new);
        return Arrays.stream(ids)
                .filter(id -> id % 2 == 0)
                .toArray(Integer[]::new);
    }
}
