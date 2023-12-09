package seoul.AutoEveryDay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.ChargeHistoryDto;
import seoul.AutoEveryDay.dto.ChargeSpotDto;
import seoul.AutoEveryDay.entity.Car;
import seoul.AutoEveryDay.entity.ChargeSpot;
import seoul.AutoEveryDay.entity.ChargeHistory;
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

    private ChargeHistory findChargeHistory(Long id) {
        return gasStationHistoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주유소 이용 정보입니다.")
        );
    }
    private ChargeSpot findChargeSpot(Long id) {
        return chargeSpotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 충전소입니다.")
        );
    }

    public ChargeHistoryDto getChargeHistory(Long id) {
        ChargeHistory chargeHistory = findChargeHistory(id);
        return ChargeHistoryDto.builder()
                .id(chargeHistory.getId())
                .carId(chargeHistory.getCar().getId())
                .chargeSpotId(chargeHistory.getChargeSpot().getId())
                .amount(chargeHistory.getAmount())
                .build();
    }

    public ChargeHistoryDto addChargeHistory(ChargeHistoryDto chargeHistoryDto, User user, Car car) {
        ChargeSpot chargeSpot = findChargeSpot(chargeHistoryDto.getChargeSpotId());
        ChargeHistory chargeHistory = ChargeHistory.builder()
                .user(user)
                .car(car)
                .chargeSpot(chargeSpot)
                .amount(chargeHistoryDto.getAmount())
                .build();
        try {
            gasStationHistoryRepository.save(chargeHistory);
        } catch (Exception e) {
            log.error("주유소 이용 정보 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "주유소 이용 정보 저장 실패");
        }
        return chargeHistoryDto;
    }

    public ChargeHistory deleteChargeHistory(Long id) {
        ChargeHistory chargeHistory = findChargeHistory(id);
        try {
            gasStationHistoryRepository.delete(chargeHistory);
        } catch (Exception e) {
            log.error("주유소 이용 정보 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "주유소 이용 정보 삭제 실패");
        }
        return chargeHistory;
    }

    public ChargeSpotDto addChargeSpot(ChargeSpotDto chargeSpotDto) {
        if (chargeSpotRepository.existsByName(chargeSpotDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 주유구 입니다.");
        }
        ChargeSpot chargeSpot = ChargeSpot.builder()
                .name(chargeSpotDto.getName())
                .build();
        try {
            chargeSpotRepository.save(chargeSpot);
        } catch (Exception e) {
            log.error("충전소 저장 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "주유구 저장 실패");
        }
        return chargeSpotDto;
    }

    public ChargeSpotDto deleteChargeSpot(Long id) {
        ChargeSpot chargeSpot = findChargeSpot(id);
        try {
            chargeSpotRepository.delete(chargeSpot);
        } catch (Exception e) {
            log.error("충전소 삭제 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "충전소 삭제 실패");
        }
        return ChargeSpotDto.builder()
                .name(chargeSpot.getName())
                .build();
    }
}
