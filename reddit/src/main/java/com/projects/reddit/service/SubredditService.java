package com.projects.reddit.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.reddit.exception.SpringRedditException;
import com.projects.reddit.mapper.SubredditMapper;
import com.projects.reddit.model.Subreddit;
import com.projects.reddit.model.SubredditDTO;
import com.projects.reddit.resources.SubredditRepository;

import antlr.collections.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SubredditService {

	@Autowired
	private SubredditRepository subredditRepository;
	
	@Autowired
	private SubredditMapper subredditMapper;

    @Transactional
    public SubredditDTO save(SubredditDTO subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }
    

//    @Transactional(readOnly = true)
//    public List<SubredditDTO> getAll() {
//        return subredditRepository.findAll()
//                .stream()
//                .map(subredditMapper::mapSubredditToDto)
//                .collect(toList());
//    }

//    public SubredditDTO getSubreddit(Long id) {
//        Subreddit subreddit = subredditRepository.findById(id)
//                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
//        return subredditMapper.mapSubredditToDto(subreddit);
//    }
}
