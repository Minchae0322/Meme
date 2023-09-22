package minchae.meme;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import minchae.meme.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ScheduledTask {


    @Autowired
    private VerificationTokenRepository verificationTokenRepository; // 해당 레포지토리에는 Entity를 조작하는 메서드가 있어야 합니다.

    @Transactional
    @Scheduled(fixedRate = 5 * 60 * 1000) // 24시간마다 실행
    public void deleteOldData() {
        Date currentTime = new Date();
        Date fiveMinuitBefore = new Date(currentTime.getTime() - 5 * 1000);
        verificationTokenRepository.deleteByCreatedTimeBefore(fiveMinuitBefore);
    }
}