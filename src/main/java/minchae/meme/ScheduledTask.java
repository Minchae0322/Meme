package minchae.meme;
import lombok.RequiredArgsConstructor;
import minchae.meme.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    @Autowired
    private MailRepository mailRepository; // 해당 레포지토리에는 Entity를 조작하는 메서드가 있어야 합니다.

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 24시간마다 실행
    public void deleteOldData() {
        Date currentTime = new Date();
        mailRepository.deleteByCreatedTimeBefore(currentTime);
    }
}