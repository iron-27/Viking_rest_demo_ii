package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class VikingLambdaService {

    private final VikingStorage vikingStorage;
    private final Random random = new Random();

    public VikingLambdaService(VikingStorage vikingStorage) {
        this.vikingStorage = vikingStorage;
    }


    public long countOlderThan(int age) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() > age)
                .count();
    }

    public long countYoungerThan(int age) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() < age)
                .count();
    }

    public long countInAgeRange(int from, int to) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() >= from && v.age() <= to)
                .count();
    }

    public long countOutsideAgeRange(int from, int to) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.age() < from || v.age() > to)
                .count();
    }

    public long countByBeardAndHair(BeardStyle beardStyle, HairColor hairColor) {
        return vikingStorage.findAll().stream()
                .filter(v -> v.beardStyle() == beardStyle && v.hairColor() == hairColor)
                .count();
    }

    public long countWithAxes() {
        return vikingStorage.findAll().stream()
                .filter(v -> {
                    long axeCount = v.equipment().stream()
                            .filter(e -> "Axe".equalsIgnoreCase(e.name()))
                            .count();
                    return axeCount == 1 || axeCount == 2;
                })
                .count();
    }

    public Viking getRandomTallViking() {
        List<Viking> tall = vikingStorage.findAll().stream()
                .filter(v -> v.heightCm() > 180)
                .toList();
        if (tall.isEmpty()) {
            return null;
        }
        return tall.get(random.nextInt(tall.size()));
    }

    public List<Viking> getLegendaryVikings() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(e -> "Legendary".equalsIgnoreCase(e.quality())))
                .toList();
    }

    public List<Viking> getSortedRedBeardedVikings() {
        return vikingStorage.findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Red
                        && v.beardStyle() != BeardStyle.CLEAN_SHAVEN)
                .sorted(Comparator.comparingInt(Viking::age))
                .toList();
    }


    public int findMaxId() {
        int[] ids = vikingStorage.findAll().stream()
                .mapToInt(Viking::id)
                .toArray();
        return Arrays.stream(ids).max().orElse(-1);
    }

    public int[] findEvenIds() {
        int[] ids = vikingStorage.findAll().stream()
                .mapToInt(Viking::id)
                .toArray();
        return Arrays.stream(ids)
                .filter(id -> id % 2 == 0)
                .toArray();
    }
}
