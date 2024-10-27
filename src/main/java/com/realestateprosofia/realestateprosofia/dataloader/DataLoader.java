package com.realestateprosofia.realestateprosofia.dataloader;

import com.realestateprosofia.realestateprosofia.model.*;
import com.realestateprosofia.realestateprosofia.model.enums.ApartmentType;
import com.realestateprosofia.realestateprosofia.model.enums.ConstructionType;
import com.realestateprosofia.realestateprosofia.model.enums.HouseType;
import com.realestateprosofia.realestateprosofia.model.enums.ParcelType;
import com.realestateprosofia.realestateprosofia.repository.*;
import com.realestateprosofia.realestateprosofia.utils.BudgetRange;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AgentRepository agentRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;
    private final PropertyRepository propertyRepository;
    private final AgencyRepository agencyRepository;
    private final ViewingRepository viewingRepository;

    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args){

        final Logger logger = LoggerFactory.getLogger(getClass());
        if (agencyRepository.count() > 0) {
            logger.info("Database already initialized");
            return;
        }

        final Agency agency = new Agency();
        agency.setName("Sofia Real Estate Agency");
        agency.setAddress("Sofia, Bulgaria");
        agency.setPhone("+359 2 123 4567");
        agency.setBudget(BigDecimal.ZERO);

        agencyRepository.save(agency);
        logger.info("Agency created: {}", agency.getName());

        final List<Agent> agents = Arrays.asList(
                createAgent("Ivan Ivanov", "+359 88 123 4567", agency),
                createAgent("Petar Petrov", "+359 88 234 5678", agency),
                createAgent("Maria Georgieva", "+359 88 345 6789", agency),
                createAgent("Georgi Dimitrov", "+359 88 456 7890", agency),
                createAgent("Elena Nikolova", "+359 88 567 8901", agency)
        );

        agentRepository.saveAll(agents);
        logger.info("Created {} agents", agents.size());

        final List<Seller> sellers = Arrays.asList(
                createSellerWithProperty("Seller One", "+359 88 111 1111", agents.get(0),
                        createApartment("Spacious two-bedroom apartment in center", "Sofia Center",
                                new BigDecimal("120000"), 80, ApartmentType.TWO_BEDROOM,
                                ConstructionType.BRICK)),
                createSellerWithProperty("Seller Two", "+359 88 222 2222", agents.get(1),
                        createHouse("Entire house with garden", "Sofia Suburbs",
                                new BigDecimal("75000"), 120, HouseType.WHOLE_HOUSE,
                                ConstructionType.EPK, 2, 500)),
                createSellerWithProperty("Seller Three", "+359 88 333 3333", agents.get(2),
                        createParcel("Forest parcel in outskirts", "Sofia Outskirts",
                                new BigDecimal("50000"), 1000, ParcelType.FOREST,
                                true)),
                createSellerWithProperty("Seller Four", "+359 88 444 4444", agents.get(3),
                        createApartment("Studio apartment near university", "University Area",
                                new BigDecimal("80000"), 40, ApartmentType.STUDIO,
                                ConstructionType.PANEL)),
                createSellerWithProperty("Seller Five", "+359 88 555 5555", agents.get(4),
                        createHouse("First floor of a beautiful house", "Green Park",
                                new BigDecimal("65000"), 90, HouseType.FLOOR,
                                ConstructionType.ADOBE, 1, 300))
        );

        sellerRepository.saveAll(sellers);
        logger.info("Created {} sellers with properties", sellers.size());

        List<Property> allProperties = propertyRepository.findAll();
        logger.info("Added {} properties to agency catalog", allProperties.size());

        for (int i = 1; i <= 10; i++) {
            final Buyer buyer = new Buyer();
            buyer.setName("Buyer " + i);
            buyer.setPhone("+359 88 666 " + String.format("%04d", i));
            final BigDecimal minBudget = new BigDecimal(30000 + random.nextInt(60001));
            final BigDecimal maxBudget = minBudget.add(new BigDecimal(random.nextInt(60001)));
            buyer.setBudgetRange(new BudgetRange(minBudget, maxBudget));

            final Agent assignedAgent = agents.get(random.nextInt(agents.size()));
            buyer.setAgent(assignedAgent);
            assignedAgent.getBuyers().add(buyer);

            buyerRepository.save(buyer);
            logger.info("Created Buyer: {} with budget range {} - {}", buyer.getName(), minBudget, maxBudget);
        }
        createInitialViewings(logger, allProperties);
        logger.info("Data loading complete.");
    }

    private Agent createAgent(final String name,
                              final String phone,
                              final Agency agency) {
        final Agent agent = new Agent();
        agent.setName(name);
        agent.setPhone(phone);
        agent.setAgency(agency);
        return agent;
    }

    private Seller createSellerWithProperty(final String name,
                                            final String phone,
                                            final Agent agent,
                                            final Property property) {
        final Seller seller = new Seller();
        seller.setName(name);
        seller.setPhone(phone);
        seller.setAgent(agent);
        seller.setProperty(property);
        property.setSeller(seller);
        property.setAgent(agent);
        agent.getSellers().add(seller);
        agent.getProperties().add(property);
        return seller;
    }

    private Apartment createApartment(final String description,
                                      final String address,
                                      final BigDecimal price,
                                      final int area,
                                      final ApartmentType apartmentType,
                                      final ConstructionType constructionType) {
        final Apartment apartment = new Apartment();
        apartment.setDescription(description);
        apartment.setAddress(address);
        apartment.setPrice(price);
        apartment.setArea(area);
        apartment.setApartmentType(apartmentType);
        apartment.setConstructionType(constructionType);
        return apartment;
    }

    private House createHouse(final String description,
                              final String address,
                              final BigDecimal price,
                              final int area,
                              final HouseType houseType,
                              final ConstructionType constructionType,
                              final int parkingSpaces,
                              final double yardArea) {
        final House house = new House();
        house.setDescription(description);
        house.setAddress(address);
        house.setPrice(price);
        house.setArea(area);
        house.setHouseType(houseType);
        house.setConstructionType(constructionType);
        house.setParkingSpaces(parkingSpaces);
        house.setYardArea(yardArea);
        return house;
    }

    private Parcel createParcel(final String description,
                                final String address,
                                final BigDecimal price,
                                final int area,
                                final ParcelType parcelType,
                                final boolean regulated) {
        final Parcel parcel = new Parcel();
        parcel.setDescription(description);
        parcel.setAddress(address);
        parcel.setPrice(price);
        parcel.setArea(area);
        parcel.setParcelType(parcelType);
        parcel.setRegulated(regulated);
        return parcel;
    }

    private void createInitialViewings(final Logger logger,
                                       final List<Property> properties) {
        final List<Buyer> buyers = buyerRepository.findAll();
        for (Buyer buyer : buyers) {
            final List<Property> affordableProperties = properties.stream()
                    .filter(p -> p.getPrice().compareTo(buyer.getBudgetRange().getMax()) <= 0)
                    .toList();

            if (affordableProperties.isEmpty()) {
                logger.warn("No affordable properties for buyer {}", buyer.getName());
                continue;
            }

            int viewingsCreated = 0;
            for (Property property : affordableProperties) {
                if (viewingsCreated >= 3) break;

                final Agent agent = property.getAgent();
                if (agent == null) {
                    logger.warn("Property {} has no agent assigned", property.getAddress());
                    continue;
                }

                final Viewing viewing = new Viewing();
                viewing.setViewingDate(LocalDateTime.now().plusDays(random.nextInt(30)));
                viewing.setProperty(property);
                viewing.setAgent(agent);
                viewing.setBuyer(buyer);

                viewingRepository.save(viewing);

                buyer.getViewings().add(viewing);
                agent.getViewings().add(viewing);

                logger.info("Created viewing for Buyer {} on Property {}", buyer.getName(), property.getAddress());
                viewingsCreated++;
            }

            if (viewingsCreated < 3) {
                logger.warn("Only {} viewings created for Buyer {}", viewingsCreated, buyer.getName());
            }
        }
    }
}