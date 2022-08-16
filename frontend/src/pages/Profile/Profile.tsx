import { useContext } from 'react';
import { useParams } from 'react-router-dom';

import * as S from '@/pages/Profile/Profile.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

import DeskSetup from '@/components/DeskSetup/DeskSetup';
import DeskSetupCard from '@/components/DeskSetupCard/DeskSetupCard';
import InventoryProductList from '@/components/Profile/InventoryProductList/InventoryProductList';
import ProductSelect from '@/components/Profile/ProductSelect/ProductSelect';
import UserInfo from '@/components/Profile/UserInfo/UserInfo';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useGetOne from '@/hooks/api/useGetOne';
import useInventory from '@/hooks/useInventory';

import { ENDPOINTS } from '@/constants/api';

function Profile() {
  const userData = useContext(UserDataContext);
  const { memberId } = useParams();

  const isOwnProfile = !memberId;

  const {
    items,
    isReady: isInventoryProductsReady,
    refetch: refetchInventoryProducts,
    updateProfileProduct,
  } = useInventory({ memberId });
  const {
    data: userInfo,
    isReady: isUserInfoReady,
    isError: isUserInfoError,
  } = useGetOne<Member>({
    url: isOwnProfile ? ENDPOINTS.ME : `${ENDPOINTS.MEMBERS}/${memberId}`,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  const inventoryList = items?.reduce((acc: Record<string, InventoryProduct[]>, curr) => {
    const currCategory = curr.product.category;
    if (acc[currCategory] === undefined) {
      acc[currCategory] = [curr];
    } else {
      acc[currCategory].push(curr);
    }
    return acc;
  }, {});

  return (
    <S.Container>
      <S.ProfileSection>
        <S.EmptySection />
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isUserInfoReady}
          isError={isUserInfoError}
        >
          <UserInfo userData={userInfo} />
        </AsyncWrapper>
        <S.FollowButton>팔로우</S.FollowButton>
      </S.ProfileSection>
      <S.DeskSetupSection>
        <DeskSetup />
      </S.DeskSetupSection>
      <S.InventorySection>
        <S.InventoryItemHeaderWrapper>
          <SectionHeader title={'리뷰를 작성한 제품'} />
          <S.EditDeskSetupButton>데스크 셋업 변경하기</S.EditDeskSetupButton>
        </S.InventoryItemHeaderWrapper>
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isInventoryProductsReady}
          isError={isUserInfoError}
        >
          <InventoryProductList inventoryList={inventoryList} />
        </AsyncWrapper>
      </S.InventorySection>

      {/* <AsyncWrapper
          fallback={<Loading />}
          isReady={isInventoryProductsReady}
          isError={isUserInfoError}
        >
          <ProductSelect
            submitHandler={refetchInventoryProducts}
            updateProfileProduct={updateProfileProduct}
            inventoryList={inventoryList}
            editable={isOwnProfile}
          />
        </AsyncWrapper> */}
    </S.Container>
  );
}

export default Profile;
