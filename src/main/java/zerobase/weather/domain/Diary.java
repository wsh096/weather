package zerobase.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity //jpa 방식을 사용할 것이기 때문에 항상 Entitiy를 붙여 줄 것!
@Getter
@Setter
@NoArgsConstructor
public class Diary {

    @Id //
    @GeneratedValue(strategy = GenerationType.IDENTITY)//자동 생성하게 하는 방식!
    private int id;
    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date;
}
