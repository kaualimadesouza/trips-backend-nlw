package com.me.travel_planner.link;

import com.me.travel_planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public LinkResponseDTO registerLink(Trip trip, LinkResquestDTO details) {
        Link link = new Link(details.title(), details.url(), trip);
        linkRepository.save(link);
        return new LinkResponseDTO(link.getId(), details.title(), details.url());
    }

    public List<LinkResponseDTO> getlinksByTrip(UUID tripId) {
        return linkRepository.findAll().stream().map(link -> new LinkResponseDTO(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
