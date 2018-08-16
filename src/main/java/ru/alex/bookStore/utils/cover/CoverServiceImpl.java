package ru.alex.bookStore.utils.cover;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.bookStore.entities.Cover;
import ru.alex.bookStore.repository.CoverRepository;

@Service
@Transactional
@Slf4j
public class CoverServiceImpl implements CoverService {

    @Autowired
    CoverRepository coverRepository;

    @Override
    public boolean save(Cover cover) {
        try {
            coverRepository.save(cover);
            return true;
        } catch (Exception e) {
            log.error("Error during saving cover {}: {}", cover, e);
            return false;
        }
    }

    @Override
    public boolean delete(Cover cover) {
        try {
            coverRepository.delete(cover);
            return true;
        } catch (Exception e) {
            log.error("Error during deleting cover {}: {}", cover, e);
            return false;
        }
    }

    public Cover createEmptyCover() {
        return new Cover();
    }

    @Override
    public void setBookId(Cover cover, Long bookId) {
        cover.setFilename(bookId);
    }
}
