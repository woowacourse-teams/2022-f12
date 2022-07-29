import UserInfo from '@/components/common/UserInfo/UserInfo';
import * as S from '@/pages/Profile/Profile.style';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import useInventory from '@/hooks/useInventory';
import { ENDPOINTS } from '@/constants/api';
import { useContext } from 'react';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';
import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import InventoryProductList from '@/components/InventoryProductList/InventoryProductList';

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
    keyboards,
    isReady: isInventoryProductsReady,
    refetchInventoryProducts,
    selectedProduct,
    setSelectedProduct,
    otherProducts,
    updateProfileProduct,
  } = useInventory();
  const [myData, , isMyDataReady] = useGetOne<Member>({
    url: ENDPOINTS.ME,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  return (
    <S.Container>
      <S.ProfileSection>
        <AsyncWrapper fallback={<div>로딩 중</div>} isReady={isMyDataReady}>
          <UserInfo userData={myData} />
        </AsyncWrapper>
        <ProductSelect
          submitHandler={refetchInventoryProducts}
          selectedProduct={selectedProduct}
          setSelectedProduct={setSelectedProduct}
          otherProducts={otherProducts}
          updateProfileProduct={updateProfileProduct}
        />
      </S.ProfileSection>
      <S.InventorySection>
        <SectionHeader>
          <S.Title>보유한 장비 목록</S.Title>
        </SectionHeader>
        <AsyncWrapper
          fallback={<div>로딩 중</div>}
          isReady={isInventoryProductsReady}
        >
          <InventoryProductList products={keyboards} />
        </AsyncWrapper>
      </S.InventorySection>
    </S.Container>
  );
}

export default Profile;
