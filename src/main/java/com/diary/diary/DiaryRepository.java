package com.diary.diary;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface DiaryRepository extends CrudRepository<Diary, Long> {
    List<Diary> findAllByDateBefore(LocalDate date);
    List<Diary> findAllByOrderByDateDesc();
}

