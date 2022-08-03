import UserInfo from '@/components/common/UserInfo/UserInfo';
import * as S from '@/pages/Profile/Profile.style';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import useOtherInventory from '@/hooks/useOtherInventory';
import { ENDPOINTS } from '@/constants/api';
import useGetOne from '@/hooks/api/useGetOne';
import AsyncWrapper from '@/components/common/AsyncWrapper/AsyncWrapper';
import InventoryProductList from '@/components/InventoryProductList/InventoryProductList';
import Loading from '@/components/common/Loading/Loading';
import { useParams } from 'react-router-dom';

type Member = {
  id: string;
  gitHubId: string;
  name: string;
  imageUrl: string;
  careerLevel: string;
  jobType: string;
};

function OtherProfile() {
  const { memberId } = useParams();

  const { items, isReady: isInventoryProductsReady } = useOtherInventory({
    memberId,
  });

  const {
    data: myData,
    isReady: isMyDataReady,
    isError: isMyDataError,
  } = useGetOne<Member>({
    url: `${ENDPOINTS.MEMBERS}/${memberId}`,
  });

  const keyboardItems = items?.filter(
    (item) => item.product.category === 'keyboard'
  );
  const monitorItems = items?.filter(
    (item) => item.product.category === 'monitor'
  );
  const standItems = items?.filter((item) => item.product.category === 'stand');
  const mouseItems = items?.filter((item) => item.product.category === 'mouse');
  const softwareItems = items?.filter(
    (item) => item.product.category === 'software'
  );

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
          <ProductSelect inventoryList={inventoryList} editable={false} />
        </AsyncWrapper>
      </S.ProfileSection>
      <S.InventorySection>
        <SectionHeader>
          <S.Title>보유한 장비 목록</S.Title>
        </SectionHeader>
        <p>현재까지 리뷰를 작성한 상품들</p>
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

export default OtherProfile;
