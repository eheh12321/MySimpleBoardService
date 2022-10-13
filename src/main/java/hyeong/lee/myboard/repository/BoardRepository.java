package hyeong.lee.myboard.repository;

import hyeong.lee.myboard.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByEditorContainingIgnoreCase(String searchValue, Pageable pageable);

    Page<Board> findAllByContentContainingIgnoreCase(String searchValue, Pageable pageable);

    Page<Board> findAllByTitleContainingIgnoreCase(String searchValue, Pageable pageable);

}
