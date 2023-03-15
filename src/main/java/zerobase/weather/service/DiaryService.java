package zerobase.weather.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DateWeatherRepository;
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
@Transactional
public class DiaryService {
    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;


    public DiaryService(DiaryRepository diaryRepository,
                        DateWeatherRepository dateWeatherRepository) {
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }


    @Transactional
    @Scheduled(cron = "0 0 1 * * *") //cron 규칙
    public void saveWeatherDate() {//날씨 저장해줄 함수
        dateWeatherRepository.save(getWeatherFromApi());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {//public으로 선언 필요
        /**3가지 기능에 대한 정리
         * open weather map에서 날씨 데이터 가져오기
         * 받아온 날씨 json 파싱하기
         * 파싱된 데이터 + 일기 값 우리 db에 넣기
         */
        //날씨 데이터 가져오기(API or DB)
        DateWeather dateWeather = getDateWeather(date);

        //파싱된 데이터 + 일기 값 우리 db에 넣기
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }


    private DateWeather getWeatherFromApi() {
        //open weather map에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();
        //받아온 날씨 json 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parsedWeather.get("temp"));
        return dateWeather;
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherListFromDB.size() == 0) {
            //새로 api에서 날씨 정보를 가져와야 한다.
            //정책 상, 현재 날씨를 가져오도록 하거나, 날씨 없이 일기 쓰게 하기.
            return getWeatherFromApi();
        } else {
            return dateWeatherListFromDB.get(0);
        }
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
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
