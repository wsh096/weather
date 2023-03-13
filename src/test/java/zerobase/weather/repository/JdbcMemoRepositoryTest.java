package zerobase.weather.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional //db정보변경 방지 어노테이션
public class JdbcMemoRepositoryTest {
    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    @DisplayName("insertTest")
    void insertMemoTest() {
        //given
        Memo newMemo = new Memo(1, "insert memo test");
        //when
        jdbcMemoRepository.save(newMemo);
        //then
        Optional<Memo> result = jdbcMemoRepository.findById(1);
        assertEquals(result.get().getText(), "insert memo test");

    }

    @Test
    @DisplayName("jdbcMemoRepository의 findAll 테스트")
    void findAllMemoTest() {
        //given
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        //when
        //then
        assertNotNull(memoList);
    }
}