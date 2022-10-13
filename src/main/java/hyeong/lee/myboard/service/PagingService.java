package hyeong.lee.myboard.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PagingService {

    private final int DEFAULT_BAR_LENGTH = 5;

    public List<Integer> getPaginationNumbers(Pageable pageable) {
        int startNumber = Math.max(pageable.getPageNumber() - (DEFAULT_BAR_LENGTH / 2), 0); // 항상 0보다 크도록 세팅
        int endNumber = Math.min(startNumber + DEFAULT_BAR_LENGTH, pageable.getPageSize()); // 최댓값보다 클 수 없음

        return IntStream.range(startNumber, endNumber).boxed().collect(Collectors.toList());
    }
}
