package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.ChargeDto;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.ChargeSpot;
import seoul.AutoEveryDay.entity.GasStationHistory;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.ChargeSpotRepository;
import seoul.AutoEveryDay.repository.GasStationHistoryRepository;

@Service
@Slf4j(topic = "GasStationService")
@Transactional
@RequiredArgsConstructor
public class GasStationService {
    private final GasStationHistoryRepository gasStationHistoryRepository;
    private final ChargeSpotRepository chargeSpotRepository;

    public ChargeDto addChargeHistory(ChargeDto chargeDto, User user, Car car) {
        ChargeSpot chargeSpot = chargeSpotRepository.findByName(chargeDto.getChargeSpotName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 충전소 이름입니다.")
        );
        GasStationHistory gasStationHistory = GasStationHistory.builder()
                .user(user)
                .car(car)
                .chargeSpot(chargeSpot)
                .amount(chargeDto.getAmount())
                .build();
        return chargeDto;
    }
}
