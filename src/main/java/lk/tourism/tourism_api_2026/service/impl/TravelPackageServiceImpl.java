package lk.tourism.tourism_api_2026.service.impl;

import lk.tourism.tourism_api_2026.controller.request.CreateTravelPackageRequest;
import lk.tourism.tourism_api_2026.controller.request.TravelPackageDetailItem;
import lk.tourism.tourism_api_2026.exception.TravelPackageNotCreatedException;
import lk.tourism.tourism_api_2026.model.Session;
import lk.tourism.tourism_api_2026.model.TravelPackage;
import lk.tourism.tourism_api_2026.model.TravelPackageDetail;
import lk.tourism.tourism_api_2026.repository.SessionRepository;
import lk.tourism.tourism_api_2026.repository.TravelPackageDetailRepository;
import lk.tourism.tourism_api_2026.repository.TravelPackageRepository;
import lk.tourism.tourism_api_2026.repository.UserRepository;
import lk.tourism.tourism_api_2026.service.TravelPackageService;
import lk.tourism.tourism_api_2026.utilities.GeneralUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class TravelPackageServiceImpl implements TravelPackageService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final TravelPackageRepository travelPackageRepository;
    private final TravelPackageDetailRepository travelPackageDetailRepository;

    @Override
    public Boolean sessionExistBySessionCodeSessionStateUserTypeAndCredentialsState(String sessionCode, String sessionState, String userType, String credentialsState) {

        Session session = sessionRepository.findBySessionCodeAndSessionState(sessionCode, GeneralUtilities.getSessionState(sessionState));

        Boolean userExists = userRepository.existsByUserTypeAndCredentialsStateAndId(GeneralUtilities.getUserType(userType), GeneralUtilities.getCredentialsState(credentialsState), session.getUserId());

        return userExists == true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void create(String tourGuideSessionCode, CreateTravelPackageRequest rq) {

        if(tourGuideSessionCode.trim().isEmpty()) {
            log.debug("tour guide session code cannot be empty");
            throw new TravelPackageNotCreatedException("tour guide session cannot be empty");
        }

        Boolean isValidSessionAndLogin = sessionExistBySessionCodeSessionStateUserTypeAndCredentialsState(tourGuideSessionCode, "ACTIVE", "TOUR_GUIDE", "ACTIVE");

        if(!isValidSessionAndLogin){
            log.debug("provided session code is invalid");
            throw new TravelPackageNotCreatedException("provided session code is invalid");
        }

        if(rq.getPeopleCount() <= 0) {
            log.debug("people count must be greater than 0");
            throw new TravelPackageNotCreatedException("people count must be greater than 0");
        }

        if(rq.getTotalPrice() <= 0) {
            log.debug("total price must be greater than 0");
            throw new TravelPackageNotCreatedException("total price must be greater than 0");
        }

        if(rq.getAdmissionPercentage() <= 0) {
            log.debug("admission percentage must be greater than 0");
            throw new TravelPackageNotCreatedException("admission percentage must be greater than 0");
        }

        TravelPackage travelPackage = new TravelPackage(
                rq.getName(),
                rq.getPeopleCount(),
                rq.getDuration(),
                BigDecimal.valueOf(rq.getTotalPrice()).setScale(2, RoundingMode.HALF_UP),
                rq.getAdmissionPercentage()
        );

        TravelPackage savedTravelPackage;

        try {
            savedTravelPackage = travelPackageRepository.save(travelPackage);
        } catch (RuntimeException e) {
            throw new TravelPackageNotCreatedException(e.getMessage());
        }

        for(TravelPackageDetailItem item : rq.getVisitingLocations()) {

            TravelPackageDetail travelPackageDetail = new TravelPackageDetail(
                    item.getTitle(),
                    item.getDescription(),
                    item.getUrl(),
                    savedTravelPackage
            );

            try {
                travelPackageDetailRepository.save(travelPackageDetail);
            } catch (RuntimeException e) {
                throw new TravelPackageNotCreatedException(e.getMessage());
            }

        }

    }

}
