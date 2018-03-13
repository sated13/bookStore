package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.bookStore.entities.Cover;

public interface CoverRepository extends JpaRepository<Cover, Long> {

    Cover findByPictureOfBookCover(byte[] pictureOfBookCover);
}
