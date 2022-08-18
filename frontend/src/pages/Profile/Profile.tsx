import { useContext } from 'react';
import { useParams } from 'react-router-dom';

import * as S from '@/pages/Profile/Profile.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';

import DeskSetup from '@/components/DeskSetup/DeskSetup';
import InventoryProductList from '@/components/Profile/InventoryProductList/InventoryProductList';
import UserInfo from '@/components/Profile/UserInfo/UserInfo';

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

  const {
    items,
    isReady: isInventoryProductsReady,
    isError: isInventoryProductError,
    refetch: refetchInventoryProducts,
    updateProfileProduct,
  } = useInventory({ memberId });
  const {
    data: userInfo,
    isReady: isUserInfoReady,
    isError: isUserInfoError,
  } = useGetOne<Member>({
    url: isOwnProfile ? ENDPOINTS.ME : `${ENDPOINTS.MEMBERS}/${memberId}`,
    headers: isLoggedIn && { Authorization: `Bearer ${userData?.token}` },
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
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isUserInfoReady}
          isError={isUserInfoError}
        >
          <UserInfo userData={userInfo} isOwnProfile={isOwnProfile} />
        </AsyncWrapper>
      </S.ProfileSection>
      <S.DeskSetupSection>
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isInventoryProductsReady}
          isError={isInventoryProductError}
        >
          <DeskSetup inventoryList={inventoryList} />
        </AsyncWrapper>
      </S.DeskSetupSection>
      <S.InventorySection>
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isInventoryProductsReady}
          isError={isInventoryProductError}
        >
          <InventoryProductList
            inventoryList={inventoryList}
            editable={isOwnProfile}
            submitHandler={refetchInventoryProducts}
            updateProfileProduct={updateProfileProduct}
          />
        </AsyncWrapper>
      </S.InventorySection>
    </S.Container>
  );
}

export default Profile;
