package com.nameless.social.api.service;

import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.repository.ClubRepository;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.QuestRepository;
import com.nameless.social.api.repository.UserGroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nameless.social.api.utils.StrUtils.parseTags;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
	private final UserGroupRepository userGroupRepository;
	private final QuestRepository questRepository;
	private final ClubRepository clubRepository;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;

	/**
	 * 사용자가 가입한 그룹과 모임을 조사하여 목록화하여 돌려주는 api입니다.
	 * @param email user email
	 * @return 그룹 목록을 List로 만든 뒤 GroupModel로 한 번 더 감싸서 전달.
	 * 그러므로 List<GroupModel>이 아니라 GroupModel로 전달해야하고 이 안에 group에 대한 List가 하위로 포함되어 있습니다.
	 */
	public GroupModel getGroupByUserEmail(final String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		List<String> clubNames = user.getUserClubs().stream()
				.map(userClub -> userClub.getClub().getName())
				.toList();

		return GroupModel.of(user.getUserGroups(), user.getEmail());
	}

	/**
	 * 그룹 정보 조회
	 * @param groupName
	 * @return
	 */
	public GroupInfoModel getGroupInfo(final String groupName) {
		Group group = groupRepository.findByName(groupName)
				.orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
		List<Club> clubs = clubRepository.findAllByGroupId(group.getId());

		// 그룹 가입한 사용자 수 조회
		long memberCount = userGroupRepository.countByIdGroupId(group.getId());

		// quest는 없을 수도 있기 때문에 오류를 발생시키면 안됨.
		List<Quest> quests = questRepository.findAll();

		return GroupInfoModel.of(
				group,
				clubs,
				memberCount,
				quests,
				parseTags(group.getTag())
		);
	}

	public List<GroupInfoModel> getGroupList() {
		List<GroupInfoModel> groupList = groupRepository.findAll().stream()
				.map(group -> {
					List<Club> club = clubRepository.findAllByGroupId(group.getId());

					// 그룹 가입한 사용자 수 조회
					long memberCount = userGroupRepository.countByIdGroupId(group.getId());

					// quest는 없을 수도 있기 때문에 오류를 발생시키면 안됨.
					List<Quest> quests = questRepository.findAll();

					return GroupInfoModel.of(
							group,
							club,
							memberCount,
							quests,
							parseTags(group.getTag())
					);
				})
				.toList();

		if (groupList.isEmpty()) {
			throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
		}

		return groupList;
	}
}
