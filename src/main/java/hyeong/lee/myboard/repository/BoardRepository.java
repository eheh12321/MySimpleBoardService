package hyeong.lee.myboard.repository;

import hyeong.lee.myboard.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
