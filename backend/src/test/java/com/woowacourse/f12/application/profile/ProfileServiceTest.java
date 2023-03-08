package com.woowacourse.f12.application.profile;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.FRONTEND_CONSTANT;
import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.FollowingRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.profile.Profile;
import com.woowacourse.f12.dto.request.profile.ProfileSearchRequest;
import com.woowacourse.f12.dto.response.profile.PagedProfilesResponse;
import com.woowacourse.f12.dto.response.profile.ProfileResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private InventoryProductRepository inventoryProductRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FollowingRepository followingRepository;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void 비회원이_검색_조건_없이_프로필을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L));
        final List<InventoryProduct> inventoryProducts = List.of(inventoryProduct);
        Member member = CORINNE.생성(1L);
        final Profile profile = new Profile(member, new InventoryProducts(inventoryProducts), false);

        given(memberRepository.findWithOutSearchConditions(pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMemberIds(List.of(member.getId())))
                .willReturn(inventoryProducts);

        // when
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest(null, null, null);
        PagedProfilesResponse pagedProfilesResponse = profileService.findBySearchConditions(null, profileSearchRequest,
                pageable);

        // then
        final ProfileResponse expect = ProfileResponse.from(profile);
        assertAll(
                () -> verify(memberRepository).findWithOutSearchConditions(pageable),
                () -> verify(inventoryProductRepository).findWithProductByMemberIds(List.of(member.getId())),
                () -> assertThat(pagedProfilesResponse.isHasNext()).isFalse(),
                () -> assertThat(pagedProfilesResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(expect)
        );
    }

    @Test
    void 비회원이_옵션으로만_프로필을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L));
        final List<InventoryProduct> inventoryProducts = List.of(inventoryProduct);
        Member member = CORINNE.생성(1L);
        final Profile profile = new Profile(member, new InventoryProducts(inventoryProducts), false);

        given(memberRepository.findWithSearchConditions(null, SENIOR, BACKEND, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMemberIds(List.of(member.getId())))
                .willReturn(inventoryProducts);

        // when
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest(null, SENIOR_CONSTANT, BACKEND_CONSTANT);
        PagedProfilesResponse memberPageResponse = profileService.findBySearchConditions(null, profileSearchRequest,
                pageable);

        // then
        final ProfileResponse expect = ProfileResponse.from(profile);
        assertAll(
                () -> verify(memberRepository).findWithSearchConditions(null, SENIOR, BACKEND, pageable),
                () -> verify(inventoryProductRepository).findWithProductByMemberIds(List.of(member.getId())),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(expect));
    }

    @Test
    void 비회원이_키워드와_옵션으로_프로필을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L));
        final List<InventoryProduct> inventoryProducts = List.of(inventoryProduct);
        Member member = CORINNE.생성(1L);
        final Profile profile = new Profile(member, new InventoryProducts(inventoryProducts), false);

        given(memberRepository.findWithSearchConditions("cheese", SENIOR, BACKEND, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMemberIds(List.of(member.getId())))
                .willReturn(inventoryProducts);

        // when
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest("cheese", SENIOR_CONSTANT,
                BACKEND_CONSTANT);
        PagedProfilesResponse pagedProfilesResponse = profileService.findBySearchConditions(null, profileSearchRequest,
                pageable);

        // then
        final ProfileResponse expect = ProfileResponse.from(profile);
        assertAll(
                () -> verify(memberRepository).findWithSearchConditions("cheese", SENIOR, BACKEND, pageable),
                () -> verify(inventoryProductRepository).findWithProductByMemberIds(List.of(member.getId())),
                () -> assertThat(pagedProfilesResponse.isHasNext()).isFalse(),
                () -> assertThat(pagedProfilesResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(expect)
        );
    }

    @Test
    void 회원이_키워드와_옵션으로_프로필을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L));
        final List<InventoryProduct> inventoryProducts = List.of(inventoryProduct);
        Member member = CORINNE.생성(1L);
        final Profile profile = new Profile(member, new InventoryProducts(inventoryProducts), true);

        Long loggedInId = 2L;
        Following following = Following.builder()
                .followerId(loggedInId)
                .followingId(member.getId())
                .build();

        given(memberRepository.findWithSearchConditions("cheese", SENIOR, BACKEND, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMemberIds(List.of(member.getId())))
                .willReturn(inventoryProducts);
        given(followingRepository.findByFollowerIdAndFollowingIdIn(loggedInId, List.of(member.getId())))
                .willReturn(List.of(following));

        // when
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest("cheese", SENIOR_CONSTANT,
                BACKEND_CONSTANT);
        PagedProfilesResponse pagedProfilesResponse = profileService.findBySearchConditions(loggedInId,
                profileSearchRequest,
                pageable);

        // then
        final ProfileResponse expect = ProfileResponse.from(profile);
        assertAll(
                () -> verify(memberRepository).findWithSearchConditions("cheese", SENIOR, BACKEND, pageable),
                () -> verify(inventoryProductRepository).findWithProductByMemberIds(List.of(member.getId())),
                () -> verify(followingRepository).findByFollowerIdAndFollowingIdIn(loggedInId, List.of(member.getId())),
                () -> assertThat(pagedProfilesResponse.isHasNext()).isFalse(),
                () -> assertThat(pagedProfilesResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(expect)
        );
    }

    @Test
    void 회원목록을_검색하여_조회할때_해당_결과가_없으면_다음_로직이_실행되지_않는다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Long loggedInId = 2L;

        given(memberRepository.findWithSearchConditions("invalid", JUNIOR, FRONTEND, pageable))
                .willReturn(new SliceImpl<>(Collections.emptyList(), pageable, false));

        // when
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest("invalid", JUNIOR_CONSTANT,
                FRONTEND_CONSTANT);
        PagedProfilesResponse pagedProfilesResponse = profileService.findBySearchConditions(loggedInId,
                profileSearchRequest,
                pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findWithSearchConditions("invalid", JUNIOR, FRONTEND, pageable),
                () -> verify(inventoryProductRepository, times(0)).findWithProductByMemberIds(any()),
                () -> verify(followingRepository, times(0)).findByFollowerIdAndFollowingIdIn(anyLong(), any()),
                () -> assertThat(pagedProfilesResponse.isHasNext()).isFalse(),
                () -> assertThat(pagedProfilesResponse.getItems()).isEmpty()
        );
    }

    @Test
    void 팔로잉하는_회원을_키워드와_옵션_없이_조회한다() {
        // given
        Long loggedInId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest(null, null, null);
        Member member = CORINNE.생성(2L);
        final Profile profile = new Profile(member, true);

        given(memberRepository.findFollowingsWithoutSearchConditions(loggedInId, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMemberIds(List.of(member.getId())))
                .willReturn(Collections.emptyList());

        // when
        PagedProfilesResponse pagedProfilesResponse = profileService.findFollowingsByConditions(loggedInId,
                profileSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findFollowingsWithoutSearchConditions(loggedInId, pageable),
                () -> verify(inventoryProductRepository).findWithProductByMemberIds(List.of(member.getId())),
                () -> assertThat(pagedProfilesResponse.isHasNext()).isFalse(),
                () -> assertThat(pagedProfilesResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ProfileResponse.from(profile))
        );
    }

    @Test
    void 팔로잉하는_회원을_옵션으로만_조회한다() {
        // given
        Long loggedInId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest(null, SENIOR_CONSTANT, BACKEND_CONSTANT);
        Member member = CORINNE.생성(2L);
        final Profile profile = new Profile(member, true);

        given(memberRepository.findFollowingsWithSearchConditions(loggedInId, null, SENIOR, BACKEND, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMemberIds(List.of(member.getId())))
                .willReturn(Collections.emptyList());

        // when
        PagedProfilesResponse pagedProfilesResponse = profileService.findFollowingsByConditions(loggedInId,
                profileSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findFollowingsWithSearchConditions(loggedInId, null, SENIOR, BACKEND,
                        pageable),
                () -> verify(inventoryProductRepository).findWithProductByMemberIds(List.of(member.getId())),
                () -> assertThat(pagedProfilesResponse.isHasNext()).isFalse(),
                () -> assertThat(pagedProfilesResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ProfileResponse.from(profile))
        );
    }

    @Test
    void 팔로잉하는_회원을_키워드와_옵션으로_조회한다() {
        // given
        Long loggedInId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Member member = CORINNE.생성(2L);
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest("ham", SENIOR_CONSTANT, null);
        final Profile profile = new Profile(member, true);

        given(memberRepository.findFollowingsWithSearchConditions(loggedInId, "ham", SENIOR, null, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMemberIds(List.of(member.getId())))
                .willReturn(Collections.emptyList());

        // when
        PagedProfilesResponse pagedProfilesResponse = profileService.findFollowingsByConditions(loggedInId,
                profileSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findFollowingsWithSearchConditions(loggedInId, "ham", SENIOR, null,
                        pageable),
                () -> verify(inventoryProductRepository).findWithProductByMemberIds(List.of(member.getId())),
                () -> assertThat(pagedProfilesResponse.isHasNext()).isFalse(),
                () -> assertThat(pagedProfilesResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(ProfileResponse.from(profile))
        );
    }

    @Test
    void 팔로잉하는_회원목록을_검색할때_결과가_없으면_다음_로직이_실행되지_않는다() {
        // given
        Long loggedInId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        ProfileSearchRequest profileSearchRequest = new ProfileSearchRequest("invalid", JUNIOR_CONSTANT,
                FRONTEND_CONSTANT);

        given(memberRepository.findFollowingsWithSearchConditions(loggedInId, "invalid", JUNIOR, FRONTEND, pageable))
                .willReturn(new SliceImpl<>(Collections.emptyList(), pageable, false));

        // when
        PagedProfilesResponse pagedProfilesResponse = profileService.findFollowingsByConditions(loggedInId,
                profileSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findFollowingsWithSearchConditions(loggedInId, "invalid", JUNIOR,
                        FRONTEND, pageable),
                () -> verify(inventoryProductRepository, times(0)).findWithProductByMemberIds(any()),
                () -> assertThat(pagedProfilesResponse.isHasNext()).isFalse(),
                () -> assertThat(pagedProfilesResponse.getItems()).isEmpty()
        );
    }
}
