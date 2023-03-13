package zerobase.weather.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Memo {
    private int id;
    private String text;
}