package ru.mephi.vikingboard.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.vikingboard.desktop.DesktopSync;
import ru.mephi.vikingboard.model.Viking;
import ru.mephi.vikingboard.service.VikingRosterService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Viking board", description = "Методы для работы с таблицей викингов через REST и Swagger")
public class VikingBoardController {

    private final VikingRosterService rosterService;
    private final DesktopSync desktopSync;

    public VikingBoardController(VikingRosterService rosterService, DesktopSync desktopSync) {
        this.rosterService = rosterService;
        this.desktopSync = desktopSync;
    }

    @GetMapping
    @Operation(summary = "Показать всех викингов из таблицы", operationId = "listVikingsFromBoard")
    @ApiResponse(responseCode = "200", description = "Записи таблицы возвращены")
    public List<Viking> listVikings() {
        return rosterService.loadAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Открыть карточку викинга по id", operationId = "readVikingCard")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Карточка найдена"),
            @ApiResponse(responseCode = "404", description = "Записи с таким id нет")
    })
    public Viking readViking(@PathVariable int id) {
        return rosterService.loadOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить конкретного викинга", operationId = "createExactViking")
    @ApiResponse(responseCode = "201", description = "Викинг добавлен в таблицу")
    public Viking createExactViking(@RequestBody Viking viking) {
        Viking created = rosterService.createExact(viking);
        desktopSync.upsert(created);
        return created;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Перезаписать параметры конкретного викинга", operationId = "overwriteVikingCard")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Параметры викинга полностью заменены"),
            @ApiResponse(responseCode = "404", description = "Не удалось найти запись для перезаписи")
    })
    public Viking overwriteViking(@PathVariable int id, @RequestBody Viking viking) {
        Viking updated = rosterService.overwrite(id, viking);
        desktopSync.upsert(updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить викинга из таблицы", operationId = "eraseVikingFromBoard")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Запись удалена"),
            @ApiResponse(responseCode = "404", description = "Записи с таким id нет")
    })
    public void eraseViking(@PathVariable int id) {
        rosterService.erase(id);
        desktopSync.remove(id);
    }

    @GetMapping("/test")
    @Operation(summary = "Проверить, что контроллер отвечает", operationId = "showDemoNames")
    public List<String> showDemoNames() {
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать случайного викинга", operationId = "createRandomVikingForBoard")
    public Viking createRandomViking() {
        Viking created = rosterService.createRandom();
        desktopSync.upsert(created);
        return created;
    }
}
