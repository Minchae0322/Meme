package minchae.meme.repository;

import minchae.meme.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    void deleteByCreatedTimeBefore(Date date);

    Optional<VerificationToken> findVerificationTokenByVerificationSubjectAndVerificationCode(String subject, String vCode);
}
