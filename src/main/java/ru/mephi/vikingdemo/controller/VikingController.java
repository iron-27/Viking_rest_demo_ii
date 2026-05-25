package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;

    private final VikingLambdaService lambdaService;

    public VikingController(
            VikingService vikingService,
            VikingLambdaService lambdaService,
            VikingListener vikingListener
    ) {

        this.vikingService = vikingService;
        this.lambdaService = lambdaService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Показать всех викингов из таблицы", operationId = "getAllVikings")
    @ApiResponse(responseCode = "200", description = "Записи таблицы возвращены")
    public List<Viking> getAllVikings() {
        return vikingService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Открыть карточку викинга по id", operationId = "getVikingById")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Карточка найдена"),
            @ApiResponse(responseCode = "404", description = "Записи с таким id нет")
    })
    public Viking getVikingById(@PathVariable int id) {
        return vikingService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить конкретного викинга", operationId = "createViking")
    @ApiResponse(responseCode = "201", description = "Викинг добавлен в таблицу")
    public Viking createViking(@RequestBody Viking viking) {
        Viking created = vikingService.createViking(viking);
        vikingListener.onVikingAdded(created);
        return created;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Перезаписать параметры конкретного викинга", operationId = "updateViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Параметры викинга полностью заменены"),
            @ApiResponse(responseCode = "404", description = "Не удалось найти запись для перезаписи")
    })
    public Viking updateViking(@PathVariable int id, @RequestBody Viking viking) {
        Viking updated = vikingService.updateById(id, viking);
        vikingListener.onVikingAdded(updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить викинга из таблицы", operationId = "deleteViking")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Запись удалена"),
            @ApiResponse(responseCode = "404", description = "Записи с таким id нет")
    })
    public void deleteViking(@PathVariable int id) {
        vikingService.deleteById(id);
        vikingListener.onVikingDeleted(id);
    }

    @GetMapping("/test")
    @Operation(summary = "Проверить, что контроллер отвечает", operationId = "test")
    public List<String> test() {
        return List.of("Ragnar", "Bjorn");
    }


    @PostMapping("/generate/{count}")
    @Operation(summary = "Массовая генерация случайных викингов")
    @ApiResponse(responseCode = "200", description = "Викинги успешно созданы")
    public List<Viking> generateVikings(@PathVariable int count) {

        List<Viking> generated = vikingService.createRandomVikings(count);

        generated.forEach(vikingListener::onVikingAdded);

        return generated;
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать случайного викинга", operationId = "createRandomViking")
    public Viking createRandomViking() {
        Viking created = vikingService.createRandomViking();
        vikingListener.onVikingAdded(created);
        return created;
    }

    @GetMapping("/lambda/older-than/{age}")
    public long countOlderThan(@PathVariable int age) {
        return lambdaService.countOlderThan(age);
    }

    @GetMapping("/lambda/younger-than/{age}")
    public long countYoungerThan(@PathVariable int age) {
        return lambdaService.countYoungerThan(age);
    }

    @GetMapping("/lambda/age-range")
    public long countInRange(
            @RequestParam int from,
            @RequestParam int to
    ) {
        return lambdaService.countInAgeRange(from, to);
    }

    @GetMapping("/lambda/outside-age-range")
    public long countOutsideRange(
            @RequestParam int from,
            @RequestParam int to
    ) {
        return lambdaService.countOutsideAgeRange(from, to);
    }

    @GetMapping("/lambda/beard-hair")
    public long countByBeardAndHair(
            @RequestParam BeardStyle beardStyle,
            @RequestParam HairColor hairColor
    ) {
        return lambdaService.countByBeardAndHair(beardStyle, hairColor);
    }

    @GetMapping("/lambda/axes")
    public long countWithAxes() {
        return lambdaService.countWithAxes();
    }

    @GetMapping("/lambda/random-tall")
    public Viking randomTallViking() {
        return lambdaService.getRandomTallViking();
    }

    @GetMapping("/lambda/legendary")
    public List<Viking> legendaryVikings() {
        return lambdaService.getLegendaryVikings();
    }

    @GetMapping("/lambda/red-bearded")
    public List<Viking> redBeardedVikings() {
        return lambdaService.getSortedRedBeardedVikings();
    }

    @GetMapping("/lambda/max-id")
    public int maxId() {
        return lambdaService.findMaxId();
    }

    @GetMapping("/lambda/even-ids")
    public int[] evenIds() {
        return lambdaService.findEvenIds();
    }


}