package hyeong.lee.myboard.repository;

import hyeong.lee.myboard.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
