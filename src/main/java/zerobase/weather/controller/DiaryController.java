package zerobase.weather.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController//Client와 맞닿아 있는 곳. //Restful 한 응답을 줌
public class DiaryController { //client - controller - service - repository - db 순
    //@GetMapping 조회 목적 //PostMapping 저장 목적
    //@DateTimeFormat springboot에서 제공해주는 datetime임을 지정해 주는 format
    private final DiaryService diaryService;//controller에서 service로 주기 위함

    public DiaryController(DiaryService diaryService) {//생성자를 이용해서 가져오는 방식.
        this.diaryService = diaryService;
    }

    @PostMapping("/create/diary")
//path지정
        //(iso = DateTimeFormat.ISO.DATE) 받을 값 포맷을 지정해 줍니다. 이렇게 하겠다는 약속을 구하기
    void createDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text) {//요청을 넣어줄 때 들어갈 파라미터 /!@##$!$QW이런 거 말하는 것
        //body 안에 들어갈 텍스트 일기 내용.
        diaryService.createDiary(date, text);
    }

    //조회하는 역할! get!
    @GetMapping("/read/diary")
    List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        return diaryService.readDiary(date);
    }

    @GetMapping("/read/diaries")
    List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return diaryService.readDiaries(startDate, endDate);
    }

    //수정에는 put
    @PutMapping("/update/diary")
    void updateDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date, @RequestBody String text) {
        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")
    void deleteDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        diaryService.deleteDiary(date);//이것도 과제네. 전부 다 지워버리네...
    }
}
