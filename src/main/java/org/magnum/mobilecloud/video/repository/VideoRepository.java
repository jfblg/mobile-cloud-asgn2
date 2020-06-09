package org.magnum.mobilecloud.video.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

    List<Video> findAll();

    List<Video> findByName(@Param("name") String name);
    List<Video> findByNameIgnoreCase(String name);

    List<Video> findByDuration(Long duration);
    List<Video> findByDurationLessThan(Long duration);
    List<Video> findByUrl(String url);
}
