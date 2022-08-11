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

  const keyboardItems = items?.filter((item) => item.product.category === 'keyboard');
  const monitorItems = items?.filter((item) => item.product.category === 'monitor');
  const standItems = items?.filter((item) => item.product.category === 'stand');
  const mouseItems = items?.filter((item) => item.product.category === 'mouse');
  const softwareItems = items?.filter((item) => item.product.category === 'software');

  const inventoryList = {
    keyboardItems,
    monitorItems,
    standItems,
    mouseItems,
  };

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
        <SectionHeader>
          <S.Title>보유한 장비 목록</S.Title>
          <S.Description>리뷰를 작성한 상품들이 표시됩니다.</S.Description>
        </SectionHeader>
        <AsyncWrapper
          fallback={<Loading />}
          isReady={isInventoryProductsReady}
          isError={isMyDataError}
        >
          <div>키보드</div>
          <InventoryProductList products={keyboardItems} />
          <div>마우스</div>
          <InventoryProductList products={mouseItems} />
          <div>모니터</div>
          <InventoryProductList products={monitorItems} />
          <div>스탠드</div>
          <InventoryProductList products={standItems} />
          <div>소프트웨어</div>
          <InventoryProductList products={softwareItems} />
        </AsyncWrapper>
      </S.InventorySection>
    </S.Container>
  );
}

export default Profile;
