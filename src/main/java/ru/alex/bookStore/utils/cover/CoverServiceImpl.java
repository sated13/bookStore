package ru.alex.bookStore.utils.cover;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.bookStore.entities.Cover;
import ru.alex.bookStore.repository.CoverRepository;

@Service
@Transactional
public class CoverServiceImpl implements CoverService {

    @Autowired
    CoverRepository coverRepository;

    @Override
    public boolean save(byte[] coverArray) {
        try {
            Cover cover = new Cover(coverArray);
            coverRepository.save(cover);
            return true;
        }
        catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace(System.out);
            return false;
        }
    }

    @Override
    public boolean save(Cover cover) {
        try {
            coverRepository.save(cover);
            return true;
        }
        catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace(System.out);
            return false;
        }
    }

    @Override
    public boolean delete(byte[] cover) {
        return false;
    }
}
