import { useContext } from 'react';

import * as S from '@/pages/Profile/Profile.style';

import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import Loading from '@/components/common/Loading/Loading';
import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import UserInfo from '@/components/common/UserInfo/UserInfo';

import InventoryProductList from '@/components/InventoryProductList/InventoryProductList';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useGetOne from '@/hooks/api/useGetOne';
import useInventory from '@/hooks/useInventory';

import { ENDPOINTS } from '@/constants/api';

type Member = {
  id: string;
  gitHubId: string;
  name: string;
  imageUrl: string;
  careerLevel: string;
  jobType: string;
};

function Profile() {
  const userData = useContext(UserDataContext);
  const {
    items,
    isReady: isInventoryProductsReady,
    refetch: refetchInventoryProducts,
    updateProfileProduct,
  } = useInventory();
  const {
    data: myData,
    isReady: isMyDataReady,
    isError: isMyDataError,
  } = useGetOne<Member>({
    url: ENDPOINTS.ME,
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
          isReady={isMyDataReady}
          isError={isMyDataError}
        >
          <UserInfo userData={myData} />
        </AsyncWrapper>
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isInventoryProductsReady}
          isError={isMyDataError}
        >
          <ProductSelect
            submitHandler={refetchInventoryProducts}
            updateProfileProduct={updateProfileProduct}
            inventoryList={inventoryList}
            editable={true}
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
          isError={isMyDataError}
        >
          <InventoryProductList inventoryList={inventoryList} />
        </AsyncWrapper>
      </S.InventorySection>
    </S.Container>
  );
}

export default Profile;
