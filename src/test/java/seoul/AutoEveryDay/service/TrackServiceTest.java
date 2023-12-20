package seoul.AutoEveryDay.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import seoul.AutoEveryDay.dto.track.ReserveTrackDto;
import seoul.AutoEveryDay.dto.track.TrackDto;
import seoul.AutoEveryDay.entity.ReserveTrack;
import seoul.AutoEveryDay.entity.Track;
import seoul.AutoEveryDay.entity.User;
import seoul.AutoEveryDay.repository.ReserveTrackRepository;
import seoul.AutoEveryDay.repository.TrackRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.given;
import static seoul.AutoEveryDay.service.LoginServiceTest.makeUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrackService 테스트")
public class TrackServiceTest {
    @InjectMocks
    private TrackService trackService;
    @Mock
    private TrackRepository trackRepository;
    @Mock
    private ReserveTrackRepository reserveTrackRepository;

    public static TrackDto makeTrackDto() {
        return TrackDto.builder()
                .id(1L)
                .name("test")
                .description("test")
                .build();
    }
    public static Track makeTrack() {
        return Track.builder()
                .id(1L)
                .name("test")
                .description("test")
                .build();
    }

    public static ReserveTrackDto makeDriveHistoryDto() {
        return ReserveTrackDto.builder()
                .id(1L)
                .userId(1L)
                .trackId(1L)
                .date(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("트랙 생성 성공")
    void createTestTrack() {
        // given
        TrackDto trackDto = makeTrackDto();
        trackDto.setId(null);  // 생성 요청 시 id는 null로 설정됨
        Track track = makeTrack();  // 만들어지는 트랙은 id가 1L로 설정됨

        given(trackRepository.existsByName(trackDto.getName())).willReturn(false);
        given(trackRepository.save(Mockito.any(Track.class))).will((invocation) -> {
            Track t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        // when
        TrackDto res = trackService.createTestTrack(trackDto);

        // then
        assertThat(res).isEqualTo(trackDto);
        assertThat(res.getId()).isEqualTo(track.getId());
        assertThat(res.getName()).isEqualTo(trackDto.getName());
        assertThat(res.getDescription()).isEqualTo(trackDto.getDescription());
    }

    @Test
    @DisplayName("트랙 생성 실패 - 이미 존재하는 이름")
    void createTestTrackFail() {
        // given
        TrackDto trackDto = makeTrackDto();
        trackDto.setId(null);

        given(trackRepository.existsByName(trackDto.getName())).willReturn(true);

        // when
        Throwable thrown = catchThrowable(() -> trackService.createTestTrack(trackDto));
        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST \"이미 존재하는 트랙 입니다.\"");
    }

    @Test
    @DisplayName("트랙 수정 성공")
    void editTestTrack() {
        // given
        TrackDto trackDto = makeTrackDto();
        Track track = makeTrack();

        given(trackRepository.findById(trackDto.getId())).willReturn(Optional.of(track));

        // when
        TrackDto res = trackService.editTestTrack(trackDto);

        // then
        assertThat(res).isEqualTo(trackDto);
        assertThat(res.getId()).isEqualTo(track.getId());
        assertThat(res.getName()).isEqualTo(trackDto.getName());
        assertThat(res.getDescription()).isEqualTo(trackDto.getDescription());
    }

    @Test
    @DisplayName("트랙 수정 실패 - 존재하지 않는 이름")
    void editTestTrackFail() {
        // given
        TrackDto trackDto = makeTrackDto();

        given(trackRepository.findById(trackDto.getId())).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> trackService.editTestTrack(trackDto));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND \"트랙을 찾을 수 없습니다.\"");
    }

    @Test
    @DisplayName("트랙 수정 실패 - 이미 존재하는 이름")
    void editTestTrackFail2() {
        // given
        TrackDto trackDto = makeTrackDto();
        Track myTrack = makeTrack();
        Track anotherTrack = makeTrack();
        anotherTrack.setId(2L);

        given(trackRepository.findById(trackDto.getId())).willReturn(Optional.of(myTrack));
        given(trackRepository.findByName(trackDto.getName())).willReturn(Optional.of(anotherTrack));

        // when
        Throwable thrown = catchThrowable(() -> trackService.editTestTrack(trackDto));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST \"이미 존재하는 트랙 입니다.\"");
    }

    @Test
    @DisplayName("트랙 삭제 성공")
    void deleteTestTrack() {
        // given
        TrackDto trackDto = makeTrackDto();
        Track track = makeTrack();

        given(trackRepository.findById(trackDto.getId())).willReturn(Optional.of(track));

        // when
        TrackDto res = trackService.deleteTestTrack(trackDto.getId());

        // then
        assertThat(res.getId()).isEqualTo(track.getId());
        assertThat(res.getName()).isEqualTo(trackDto.getName());
        assertThat(res.getDescription()).isEqualTo(trackDto.getDescription());
    }

    @Test
    @DisplayName("트랙 삭제 실패 - 존재하지 않는 이름")
    void deleteTestTrackFail() {
        // given
        TrackDto trackDto = makeTrackDto();

        given(trackRepository.findById(trackDto.getId())).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> trackService.deleteTestTrack(trackDto.getId()));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND \"트랙을 찾을 수 없습니다.\"");
    }

    @Test
    @DisplayName("트랙 삭제 실패 - 예약된 트랙")
    void deleteTestTrackFail2() {
        // given
        TrackDto trackDto = makeTrackDto();
        Track track = makeTrack();

        given(trackRepository.findById(trackDto.getId())).willReturn(Optional.of(track));
        given(reserveTrackRepository.existsByTrack_Id(track.getId())).willReturn(true);

        // when
        Throwable thrown = catchThrowable(() -> trackService.deleteTestTrack(trackDto.getId()));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST \"예약된 트랙은 삭제할 수 없습니다.\"");
    }

    @Test
    @DisplayName("모든 트랙 조회 성공")
    void getAllTestTrack() {
        // given
        Track track = makeTrack();

        given(trackRepository.findAll()).willReturn(List.of(track));

        // when
        List<TrackDto> res = trackService.getAllTestTrack();

        // then
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.get(0).getId()).isEqualTo(track.getId());
        assertThat(res.get(0).getName()).isEqualTo(track.getName());
        assertThat(res.get(0).getDescription()).isEqualTo(track.getDescription());
    }

    @Test
    @DisplayName("예약 생성 성공")
    void createTestHistory() {
        // given
        Track track = makeTrack();
        TrackDto trackDto = makeTrackDto();
        ReserveTrackDto reserveTrackDto = makeDriveHistoryDto();
        User user = makeUser();

        given(reserveTrackRepository.findByTrack_IdAndDate(track.getId(), reserveTrackDto.getDate())).willReturn(Optional.empty());
        given(trackRepository.findById(track.getId())).willReturn(Optional.of(track));
        given(reserveTrackRepository.save(Mockito.any(ReserveTrack.class))).will((invocation) -> {
            ReserveTrack d = invocation.getArgument(0);
            d.setId(1L);
            return d;
        });

        // when
        ReserveTrackDto res = trackService.createReserveHistory(reserveTrackDto, user);

        // then
        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getUserId()).isEqualTo(user.getId());
        assertThat(res.getTrackId()).isEqualTo(trackDto.getId());
        assertThat(res.getDate()).isEqualTo(reserveTrackDto.getDate());
    }

    @Test
    @DisplayName("예약 생성 실패 - 오늘 이전 날짜")
    void createTestHistoryFail() {
        // given
        Track track = makeTrack();
        ReserveTrackDto reserveTrackDto = makeDriveHistoryDto();
        User user = makeUser();
        reserveTrackDto.setDate(LocalDate.now().minusDays(1));

        // when
        Throwable thrown = catchThrowable(() -> trackService.createReserveHistory(reserveTrackDto, user));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST \"예약은 오늘 이후로만 가능합니다.\"");
    }

    @Test
    @DisplayName("예약 생성 실패 - 이미 예약된 날짜")
    void createTestHistoryFail2() {
        // given
        Track track = makeTrack();
        ReserveTrackDto reserveTrackDto = makeDriveHistoryDto();
        User user = makeUser();

        given(reserveTrackRepository.findByTrack_IdAndDate(track.getId(), reserveTrackDto.getDate())).willReturn(Optional.of(ReserveTrack.builder().build()));

        // when
        Throwable thrown = catchThrowable(() -> trackService.createReserveHistory(reserveTrackDto, user));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 400 BAD_REQUEST \"이미 예약된 날짜입니다.\"");
    }

    @Test
    @DisplayName("예약 생성 실패 - 존재하지 않는 트랙")
    void createTestHistoryFail3() {
        // given
        Track track = makeTrack();
        ReserveTrackDto reserveTrackDto = makeDriveHistoryDto();
        User user = makeUser();

        given(reserveTrackRepository.findByTrack_IdAndDate(track.getId(), reserveTrackDto.getDate())).willReturn(Optional.empty());
        given(trackRepository.findById(track.getId())).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> trackService.createReserveHistory(reserveTrackDto, user));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND \"트랙을 찾을 수 없습니다.\"");
    }

    @Test
    @DisplayName("예약 삭제 성공")
    void deleteTestHistory() {
        // given
        Track track = makeTrack();
        ReserveTrackDto reserveTrackDto = makeDriveHistoryDto();
        User user = makeUser();

        given(trackRepository.findById(reserveTrackDto.getId())).willReturn(Optional.of(track));
        given(reserveTrackRepository.findByUser_IdAndTrack_IdAndDate(user.getId(), track.getId(), reserveTrackDto.getDate())).willReturn(Optional.of(ReserveTrack.builder().build()));

        // when
        ReserveTrackDto res = trackService.deleteReserveHistory(reserveTrackDto, user);

        // then
        assertThat(res.getId()).isEqualTo(reserveTrackDto.getId());
        assertThat(res.getUserId()).isEqualTo(reserveTrackDto.getUserId());
        assertThat(res.getTrackId()).isEqualTo(reserveTrackDto.getTrackId());
        assertThat(res.getDate()).isEqualTo(reserveTrackDto.getDate());
    }

    @Test
    @DisplayName("예약 삭제 실패 - 찾을 수 없는 트랙")
    void deleteTestHistoryFail() {
        // given
        ReserveTrackDto reserveTrackDto = makeDriveHistoryDto();
        User user = makeUser();

        given(trackRepository.findById(reserveTrackDto.getId())).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> trackService.deleteReserveHistory(reserveTrackDto, user));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND \"트랙을 찾을 수 없습니다.\"");
    }

    @Test
    @DisplayName("예약 삭제 실패 - 존재하지 않는 예약")
    void deleteTestHistoryFail2() {
        // given
        Track track = makeTrack();
        ReserveTrackDto reserveTrackDto = makeDriveHistoryDto();
        User user = makeUser();

        given(trackRepository.findById(reserveTrackDto.getId())).willReturn(Optional.of(track));
        given(reserveTrackRepository.findByUser_IdAndTrack_IdAndDate(user.getId(), track.getId(), reserveTrackDto.getDate())).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> trackService.deleteReserveHistory(reserveTrackDto, user));

        // then
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        assertThat(thrown).hasToString("org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND \"예약을 찾을 수 없습니다.\"");
    }
}
