package com.unravel.scout.service;

import com.unravel.scout.model.dto.v1.CountryDestinationDto;
import com.unravel.scout.model.entity.v1.Country;
import com.unravel.scout.model.entity.DestinationMapping;
import com.unravel.scout.model.entity.Destinations;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.enums.ItemType;
import com.unravel.scout.model.exceptions.ResourceNotFoundException;
import com.unravel.scout.repositories.CountriesRepository;
import com.unravel.scout.repositories.CountryMapping;
import com.unravel.scout.repositories.ItemDetailRepository;
import com.unravel.scout.repository.DestinationRepository;
import com.unravel.scout.utils.IdsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DestinationsService {

    private final DestinationRepository destinationRepository;
    private final CountriesRepository countriesRepository;
    private final CountryMapping countryMapping;
    private final ItemDetailRepository itemDetailRepository;

    @Value("${scout.destinations.document._id}")
    private String DESTINATION_ID;

    @Autowired
    public DestinationsService(final DestinationRepository destinationRepository,
                               CountriesRepository countriesRepository, CountryMapping countryMapping, ItemDetailRepository itemDetailRepository) {
        this.destinationRepository = destinationRepository;
        this.countriesRepository = countriesRepository;
        this.countryMapping = countryMapping;
        this.itemDetailRepository = itemDetailRepository;
    }

    public Destinations getDestinationDocument() {
        return destinationRepository
            .findById(DESTINATION_ID)
            .orElseThrow(() -> new ResourceNotFoundException("Destinations"));
    }

    public DestinationMapping getDestinationMapping(String city) {
        return getDestinationDocument().getItineraryMapping().get(city);
    }

    public void addDestinationMapping(String city, DestinationMapping mapping) {
        Destinations destinations = getDestinationDocument();
        destinations.getItineraryMapping().put(city, mapping);
        destinationRepository.save(destinations);
    }

    public List<Country> getCountriesDocument(Pageable pageable) {
        return countriesRepository.findAll(pageable).toList();
    }

    public Object fetchDestinationByItemType(String countryId) {
        List<ItemDetail> items = new ArrayList<>();
        Optional<ItemDetail> itemss = null;
        List<CountryDestinationDto> finalList = new ArrayList<>();
        //  List<List<CountryDestinationDto>> countries= new ArrayList<>();
        if (!countryId.isEmpty()) {
            itemss = itemDetailRepository.findById(IdsUtils.convertStringToUUID(countryId));
            return itemss.get().getCountryDestinations().stream().map(CountryDestinationDto::mapToCountryDestinationImageDto).collect(Collectors.toList());
        } else {
            List<CountryDestinationDto> dto = new ArrayList<>();
            items = itemDetailRepository.findByItemType(ItemType.COUNTRY.toString());

            for (int i = 0; i < items.size(); i++) {

                dto = items.get(i).getCountryDestinations().stream().map(CountryDestinationDto::mapToCountryDestinationImageDto).collect(Collectors.toList());
                finalList.addAll(dto);
            }
            return finalList;

        }
        //  return countries;

    }

}
