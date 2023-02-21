package numble.bankingapi.social.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class FriendServiceTest {

	private static final long 나의_ID = 100L;
	private static final long 상대방의_ID = 3L;
	@Autowired
	private FriendService friendService;

	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private AskedFriendHistoryRepository askedFriendHistoryRepository;

	@Test
	@DisplayName("친구 정보를 저장한다.")
	void saveFriend() {
		Friend friend = new Friend(나의_ID, 상대방의_ID);
		assertDoesNotThrow(
			() -> friendService.saveFriend(friend)
		);
	}

	@Test
	@DisplayName("친구 목록을 조회한다.")
	void findFriends() {
		long times = 10;

		for (long i = 1; i <= times; i++) {
			friendRepository.save(new Friend(나의_ID, i));
		}

		List<Friend> friends = assertDoesNotThrow(
			() -> friendService.findFriends(나의_ID)
		);

		assertThat(friends).hasSize((int)times);
	}

	@Test
	@DisplayName("친구 요청 정보를 저장한다.")
	void saveAskedFriend() {
		AskedFriendHistory askedFriendHistory = new AskedFriendHistory(나의_ID, 상대방의_ID, ApprovalStatus.WAITING);

		assertDoesNotThrow(
			() -> friendService.saveAskedFriendHistory(askedFriendHistory)
		);
	}

	@ParameterizedTest
	@EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"REJECTED"})
	@DisplayName("저장할 때, 상대방에게 보낸 이력이 거절(REJECTED) 상태가 아닌 경우 예외가 발생한다.")
	void saveAskedFriend_notExitsRequest(ApprovalStatus notRejected) {
		AskedFriendHistory askedFriendHistory = new AskedFriendHistory(나의_ID, 상대방의_ID, notRejected);
		askedFriendHistoryRepository.save(askedFriendHistory);

		assertThatThrownBy(
			() -> friendService.saveAskedFriendHistory(askedFriendHistory)
		).isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"REJECTED"})
	@DisplayName("저장할 때, 상대방에게 받은 이력이 거절(REJECTED) 상태가 아닌 경우 예외가 발생한다.")
	void saveAskedFriend_notExitsRequest_otherCase(ApprovalStatus notRejected) {
		AskedFriendHistory askedFriendHistory = new AskedFriendHistory(상대방의_ID, 나의_ID, notRejected);
		askedFriendHistoryRepository.save(askedFriendHistory);

		assertThatThrownBy(
			() -> friendService.saveAskedFriendHistory(askedFriendHistory)
		).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("대기 중인 친구 요청 목록을 조회한다.")
	void findAskedFriendsIsStatusWaiting() {
		long count = 23;
		for (long i = 1; i <= count; i++) {
			AskedFriendHistory 대기중인_요청자 = new AskedFriendHistory(i, 나의_ID, ApprovalStatus.WAITING);
			askedFriendHistoryRepository.save(대기중인_요청자);
		}

		List<AskedFriendHistory> histories = assertDoesNotThrow(
			() -> friendService.findWaitingAskedFriendHistories(나의_ID)
		);
		assertThat(histories).hasSize((int)count);
	}

	@Test
	@DisplayName("친구 요청 정보를 승낙한다.")
	void approveAskedFriend() {
		AskedFriendHistory 대기중인_요청 = askedFriendHistoryRepository.save(
			new AskedFriendHistory(상대방의_ID, 나의_ID, ApprovalStatus.WAITING));
		assertDoesNotThrow(
			() -> friendService.approvedAskedFriend(대기중인_요청.getId())
		);
	}

	@Test
	@DisplayName("친구 요청 정보가 존재하지 않으면 예외가 발생한다.")
	void approveAskedFriend_notEmpty() {
		long 존재하지않는_요청 = 999L;
		assertThatThrownBy(
			() -> friendService.approvedAskedFriend(존재하지않는_요청)
		).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("친구 요청 정보를 절한다.")
	void rejectAskedFriend() {
		AskedFriendHistory 대기중인_요청 = askedFriendHistoryRepository.save(
			new AskedFriendHistory(상대방의_ID, 나의_ID, ApprovalStatus.WAITING));
		assertDoesNotThrow(
			() -> friendService.rejectAskedFriend(대기중인_요청.getId())
		);
	}

	@Test
	@DisplayName("친구 요청 정보가 존재하지 않으면 예외가 발생한다.")
	void rejectAskedFriend_notEmpty() {
		long 존재하지않는_요청 = 999L;
		assertThatThrownBy(
			() -> friendService.rejectAskedFriend(존재하지않는_요청)
		).isInstanceOf(IllegalArgumentException.class);
	}
}
