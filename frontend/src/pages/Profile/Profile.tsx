import { useContext, useState } from 'react';
import { useParams } from 'react-router-dom';

import * as S from '@/pages/Profile/Profile.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import DeskSetup from '@/components/DeskSetup/DeskSetup';
import InventoryProductList from '@/components/Profile/InventoryProductList/InventoryProductList';
import UserInfo from '@/components/Profile/UserInfo/UserInfo';
import ReviewListSection from '@/components/Review/ReviewListSection/ReviewListSection';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useGetOne from '@/hooks/api/useGetOne';
import useAuth from '@/hooks/useAuth';
import useInventory from '@/hooks/useInventory';

import { ENDPOINTS } from '@/constants/api';

function Profile() {
  const userData = useContext(UserDataContext);

  const { isLoggedIn } = useAuth();
  const { memberId } = useParams();

  const isOwnProfile = !memberId;

  const [activeTab, setActiveTab] = useState<number>(0);
  const tabTitle = ['리뷰를 작성한 제품', '작성한 리뷰 목록'];

  const {
    items,
    isReady: isInventoryProductsReady,
    isError: isInventoryProductError,
    refetch: refetchInventoryProducts,
    updateProfileProduct,
    reviews,
    isReviewLoading,
    isReviewReady,
    isReviewError,
    getNextPage,
  } = useInventory({ memberId });
  const {
    data: userInfo,
    isReady: isUserInfoReady,
    isError: isUserInfoError,
  } = useGetOne<Member>({
    url: isOwnProfile ? ENDPOINTS.ME : `${ENDPOINTS.MEMBERS}/${memberId}`,
    headers: isLoggedIn && { Authorization: `Bearer ${userData?.token}` },
  });

  const inventoryList = items?.reduce(
    (acc: Record<Category, InventoryProduct[]>, curr) => {
      const currCategory = curr.product.category;
      if (acc[currCategory] === undefined) {
        acc[currCategory] = [curr];
      } else {
        acc[currCategory].push(curr);
      }
      return acc;
    },
    {} as Record<Category, InventoryProduct[]>
  );

  return (
    <S.Container>
      <S.ProfileSection>
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isUserInfoReady}
          isError={isUserInfoError}
        >
          <UserInfo userData={userInfo} isOwnProfile={isOwnProfile} />
        </AsyncWrapper>
      </S.ProfileSection>
      <S.SectionHeaderWrapper>
        <SectionHeader title={'데스크 셋업'} />
      </S.SectionHeaderWrapper>
      <S.DeskSetupSection>
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isInventoryProductsReady}
          isError={isInventoryProductError}
        >
          <DeskSetup inventoryList={inventoryList} />
        </AsyncWrapper>
      </S.DeskSetupSection>
      <S.TabButtonWrapper>
        {tabTitle.map((title, index) => {
          return (
            <S.TabButton
              key={index}
              selected={title === tabTitle[activeTab]}
              onClick={() => {
                setActiveTab(index);
              }}
            >
              {title}
            </S.TabButton>
          );
        })}
      </S.TabButtonWrapper>
      <S.InventorySection>
        {activeTab === 0 ? (
          <AsyncWrapper
            fallback={<Loading />}
            isReady={isInventoryProductsReady}
            isError={isInventoryProductError}
          >
            <InventoryProductList
              inventoryList={inventoryList}
              editable={isOwnProfile}
              refetchInventoryProducts={refetchInventoryProducts}
              updateProfileProduct={updateProfileProduct}
            />
          </AsyncWrapper>
        ) : (
          <AsyncWrapper
            fallback={<Loading />}
            isReady={isReviewReady}
            isError={isReviewError}
          >
            <ReviewListSection
              columns={2}
              data={reviews}
              getNextPage={getNextPage}
              isLoading={isReviewLoading}
              isError={isReviewError}
              pageSize={4}
              productVisible={true}
              userNameVisible={false}
            />
          </AsyncWrapper>
        )}
      </S.InventorySection>
    </S.Container>
  );
}

export default Profile;
