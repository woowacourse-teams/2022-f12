package com.woowacourse.f12.application.profile;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.FollowingRepository;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.profile.Profiles;
import com.woowacourse.f12.dto.request.profile.ProfileSearchRequest;
import com.woowacourse.f12.dto.response.profile.PagedProfilesResponse;
import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfileService {

    private final MemberRepository memberRepository;
    private final InventoryProductRepository inventoryProductRepository;
    private final FollowingRepository followingRepository;


    public ProfileService(final MemberRepository memberRepository,
                          final InventoryProductRepository inventoryProductRepository,
                          final FollowingRepository followingRepository) {
        this.memberRepository = memberRepository;
        this.inventoryProductRepository = inventoryProductRepository;
        this.followingRepository = followingRepository;
    }

    public PagedProfilesResponse findBySearchConditions(@Nullable final Long loggedInId,
                                                        final ProfileSearchRequest profileSearchRequest,
                                                        final Pageable pageable) {
        final Slice<Member> slice = findBySearchConditions(profileSearchRequest, pageable);
        if (slice.isEmpty()) {
            return PagedProfilesResponse.empty();
        }
        final Profiles profiles = createProfiles(loggedInId, slice.getContent());
        return PagedProfilesResponse.of(slice.hasNext(), profiles);
    }

    private Slice<Member> findBySearchConditions(final ProfileSearchRequest profileSearchRequest,
                                                 final Pageable pageable) {
        final CareerLevel careerLevel = parseCareerLevel(profileSearchRequest);
        final JobType jobType = parseJobType(profileSearchRequest);
        if (profileSearchRequest.getQuery() == null && careerLevel == null && jobType == null) {
            return memberRepository.findWithOutSearchConditions(pageable);
        }
        return memberRepository.findWithSearchConditions(profileSearchRequest.getQuery(), careerLevel,
                jobType, pageable);
    }

    private static List<Long> extractIds(final List<Member> members) {
        return members.stream()
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    private JobType parseJobType(final ProfileSearchRequest profileSearchRequest) {
        final JobTypeConstant jobTypeConstant = profileSearchRequest.getJobType();
        if (jobTypeConstant == null) {
            return null;
        }
        return jobTypeConstant.toJobType();
    }

    private CareerLevel parseCareerLevel(final ProfileSearchRequest profileSearchRequest) {
        final CareerLevelConstant careerLevelConstant = profileSearchRequest.getCareerLevel();
        if (careerLevelConstant == null) {
            return null;
        }
        return careerLevelConstant.toCareerLevel();
    }

    private Profiles createProfiles(final Long loggedInId, final List<Member> members) {
        final List<Long> memberIds = extractIds(members);
        final List<InventoryProduct> mixedInventoryProducts =
                inventoryProductRepository.findWithProductByMemberIds(memberIds);
        final List<Following> followingRelations
                = followingRepository.findByFollowerIdAndFollowingIdIn(loggedInId, memberIds);
        return Profiles.of(members, mixedInventoryProducts, followingRelations);
    }

    public PagedProfilesResponse findFollowingsByConditions(final Long loggedInId,
                                                            final ProfileSearchRequest profileSearchRequest,
                                                            final Pageable pageable) {
        final Slice<Member> slice = findFollowingsBySearchConditions(loggedInId, profileSearchRequest, pageable);
        if (slice.isEmpty()) {
            return PagedProfilesResponse.empty();
        }
        final Profiles followingProfiles = createFollowingProfiles(slice);
        return PagedProfilesResponse.of(slice.hasNext(), followingProfiles);
    }

    private Profiles createFollowingProfiles(final Slice<Member> slice) {
        final List<Long> memberIds = extractIds(slice.getContent());
        final List<InventoryProduct> mixedInventoryProducts =
                inventoryProductRepository.findWithProductByMemberIds(memberIds);
        return Profiles.ofFollowings(slice.getContent(), mixedInventoryProducts);
    }

    private Slice<Member> findFollowingsBySearchConditions(final Long loggedInId,
                                                           final ProfileSearchRequest profileSearchRequest,
                                                           final Pageable pageable) {
        final CareerLevel careerLevel = parseCareerLevel(profileSearchRequest);
        final JobType jobType = parseJobType(profileSearchRequest);
        if (profileSearchRequest.getQuery() == null && careerLevel == null && jobType == null) {
            return memberRepository.findFollowingsWithOutSearchConditions(loggedInId, pageable);
        }
        return memberRepository.findFollowingsWithSearchConditions(loggedInId, profileSearchRequest.getQuery(),
                careerLevel, jobType, pageable);
    }
}
