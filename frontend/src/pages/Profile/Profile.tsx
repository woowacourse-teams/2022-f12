import { useContext } from 'react';
import { useParams } from 'react-router-dom';

import * as S from '@/pages/Profile/Profile.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';

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
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isUserInfoReady}
          isError={isUserInfoError}
        >
          <UserInfo userData={userInfo} />
        </AsyncWrapper>
        <AsyncWrapper
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
        </AsyncWrapper>
      </S.ProfileSection>
      <S.InventorySection>
        <SectionHeader title={'보유한 장비 목록'}>
          <S.Description>리뷰를 작성한 상품들이 표시됩니다.</S.Description>
        </SectionHeader>
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isInventoryProductsReady}
          isError={isUserInfoError}
        >
          <InventoryProductList inventoryList={inventoryList} />
        </AsyncWrapper>
      </S.InventorySection>
    </S.Container>
  );
}

export default Profile;
