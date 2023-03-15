package zerobase.weather.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class JpaMemoRepositoryTest {
@Autowired
    JpaMemoRepository jpaMemoRepository;
@Test
@DisplayName("Jpa를 활용한 db 접근 insert 테스트")

void insertMemoTest(){
    //given
    Memo newMemo =new Memo(10,"this is jpa memo");
    //when
    jpaMemoRepository.save(newMemo);
    //then
    List<Memo> memoList = jpaMemoRepository.findAll();
    assertTrue(memoList.size() > 0);

}

@Test
@DisplayName("JpaMemoRepository인터페이스를 활용한 findById TEST")

void findByIdTest(){
    //given
    Memo newMemo = new Memo(11,"jpa");
    //when
    Memo memo = jpaMemoRepository.save(newMemo);
    //then
    Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
    assertEquals(result.get().getText(),"jpa");

}
}