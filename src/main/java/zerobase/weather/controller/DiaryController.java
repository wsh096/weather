package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.Exception.InvalidDate;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @ApiOperation(value = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장")
    @PostMapping("/create/diary")

    void createDiary(
            @ApiParam(value = "일기를 작성할 날짜를 입력해 주세요.", example = "2023-03-01")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @ApiParam(value = "일기를 작성해 주세요.", example = "오늘은 내 인생 최고의 날이다.")
            @RequestBody String text) {
        diaryService.createDiary(date, text);
    }


    @ApiOperation(value = "선택한 날짜의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diary")
    List<Diary> readDiary(
            @ApiParam(value = "조회할 기간의 날짜입니다.", example = "2023-03-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
//
        return diaryService.readDiary(date);
    }

    @ApiOperation(value = "선택한 기간의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @ApiParam(value = "조회할 기간의 첫 번째 날입니다.", example = "2023-03-01")
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @ApiParam(value = "조회할 기간의 마지막 날입니다.", example = "2023-03-15")
            LocalDate endDate
    ) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @ApiOperation(value = "해당 날짜의 첫번째 일기 글을 새로 받아온 일기글로 수정합니다.")
    @PutMapping("/update/diary")
    void updateDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @ApiParam(value = "수정할 날짜입니다.", example = "2023-03-01")
            LocalDate date,
            @RequestBody
            @ApiParam(value = "수정할 내용을 작성해 주세요.", example = "오늘 내가 한 건...")
            String text) {
        diaryService.updateDiary(date, text);
    }
    @ApiOperation(value = "해당 날짜의 모든 일기를 삭제합니다.")
    @DeleteMapping("/delete/diary")
    void deleteDiary(
            @ApiParam(value = "입력한 날짜의 모든 일기를 삭제합니다.", example = "2023-03-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        diaryService.deleteDiary(date);
    }
}
