package org.magnum.mobilecloud.video;

import javassist.tools.web.BadHttpRequest;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class MyController {

//    private List<Video> videoList;
//    @PostConstruct
//    public void init() {
//        videoList = new ArrayList<>();
//    }

    // you must use Autowired to inject the dependency
    @Autowired
    private VideoRepository videoRepository;

    // GET /video
    @RequestMapping(value = "/video", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<Video> getVideo() {
        return videoRepository.findAll();
    }

    // TODO: not working
    // https://www.netsurfingzone.com/jpa/how-to-write-custom-method-in-repository-in-spring-data-jpa/
    // GET /video/search/findByName?title={title}
    @RequestMapping(value = "/video/search/findByName", method = RequestMethod.GET)
    @ResponseBody
    public List<Video> getVideoSearchFindByName(@RequestParam("title") String title) {
        System.out.println("search by name: " + title);
        return videoRepository.findByName(title);
    }

    // GET /video/search/findByDurationLessThan?duration={duration}
    // DONE
    @RequestMapping(value = "/video/search/findByDurationLessThan", method = RequestMethod.GET)
    @ResponseBody
    public List<Video> getVideoSearchFindByDurationLessThan(@RequestParam("duration") Long duration) {
        return videoRepository.findByDurationLessThan(duration);
    }

    // GET /video/search/findByUrl?url={url}
    @RequestMapping(value = "/video/search/findByUrl", method = RequestMethod.GET)
    @ResponseBody
    public List<Video> getVideoSearchFindByUrl(@RequestParam("url") String url) {
        return videoRepository.findByUrl(url);
    }

    // POST /video
    @RequestMapping(value = "/video", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Video postVideo(@RequestBody Video video) {
        return videoRepository.save(video);
    }

    // GET /video/{id}
    // DONE
    @RequestMapping(value = "/video/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Video getVideoId(@PathVariable Long id) {
        if (videoRepository.exists(id))
            return videoRepository.findOne(id);
        throw new ResourceNotFoundException();
    }

    // POST /video/{id}/like
    @RequestMapping(value = "/video/{id}/like", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity postVideoIdLike(@PathVariable Long id, Principal principal) {
        if (! videoRepository.exists(id))
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        String userName = principal.getName();
        Video v = videoRepository.findOne(id);
        Set<String> likedBy = v.getLikedBy();

        if (likedBy.contains(userName))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        likedBy.add(userName);
        v.setLikedBy(likedBy);
        v.setLikes(likedBy.size());
        videoRepository.save(v);
        return new ResponseEntity(HttpStatus.OK);
    }

    // POST /video/{id}/unlike
    @RequestMapping(value = "/video/{id}/unlike", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity postVideoIdUnlike(@PathVariable Long id, Principal principal) {
        if (! videoRepository.exists(id))
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        String userName = principal.getName();
        Video v = videoRepository.findOne(id);
        Set<String> likedBy = v.getLikedBy();
        if (! likedBy.contains(userName))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        likedBy.remove(userName);
        v.setLikedBy(likedBy);
        v.setLikes(likedBy.size());

        videoRepository.save(v);
        return new ResponseEntity(HttpStatus.OK);
    }


}
