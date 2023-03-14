package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    //조회를 위한 repository
    List<Diary> findAllByDate(LocalDate date);//db에서 값을 알아서 가져와 줌.

    //기간 조회를 위한 repository
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    //수정을 위한 repository
    Diary getFirstByDate(LocalDate date);
    //getFirstByDate은 Limit 1 개의 데이터를 가져오는 것

    @Transactional //이거 안 붙이면 신뢰할 수 없다고 나오는 에러가 정상임
    void deleteAllByDate(LocalDate date); //위의 옵션 조합으로 조정이 가능
}
