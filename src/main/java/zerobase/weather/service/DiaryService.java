package zerobase.weather.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiaryService {
    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public void createDiary(LocalDate date, String text) {//public으로 선언 필요
        /**3가지 기능에 대한 정리
         * open weather map에서 날씨 데이터 가져오기
         * 받아온 날씨 json 파싱하기
         * 파싱된 데이터 + 일기 값 우리 db에 넣기
         */
        //open weather map에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();

        //받아온 날씨 json 파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherData);

        //파싱된 데이터 + 일기 값 우리 db에 넣기
        Diary nowDiary = new Diary();
        nowDiary.setWeather(parseWeather.get("main").toString());
        nowDiary.setIcon(parseWeather.get("icon").toString());
        nowDiary.setTemperature((Double) parseWeather.get("temp"));
        nowDiary.setText(text);
        nowDiary.setDate(date);
        diaryRepository.save(nowDiary);
    }

    public List<Diary> readDiary(LocalDate date){
        return diaryRepository.findAllByDate(date);
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate,endDate);
    }

    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);//이러면 첫 번째거만 수정되긴 함. 이 부분이겠네. 과제
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);//id가 있는 상태로 하면 update기능이 됨!
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }


    private String getWeatherString() {
        String apiUrl =
                "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid="
                        + apiKey;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//요청을 보낼 수 있는 connection을 열고
            connection.setRequestMethod("GET");//get요청을 보내고
            int responseCode = connection.getResponseCode();//응답 코드 수신
            BufferedReader br;
            if (responseCode == 200) {//응답 또는 에러를 가지고 옴
                br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);//response라는 stringbuilder에 담아 줌
            }
            br.close();

            return response.toString();//응답값을 답은 stringbuiler를 반환
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherDate = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherDate.get("main"));
        resultMap.put("icon", weatherDate.get("icon"));
        return resultMap;
    }



}
