import ProductBar from '@/components/common/ProductBar/ProductBar';
import UserInfo from '@/components/common/UserInfo/UserInfo';
import * as S from '@/pages/Profile/Profile.style';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import useAuth from '@/hooks/useAuth';
import { Navigate } from 'react-router-dom';
import ROUTES from '@/constants/routes';
import useInventory from '@/hooks/useInventory';
import { ENDPOINTS } from '@/constants/api';
import { useContext } from 'react';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';

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
  const { isLoggedIn } = useAuth();
  const { keyboards, refetchInventoryProducts } = useInventory();
  const [myData] = useGetOne<Member>({
    url: ENDPOINTS.ME,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  return isLoggedIn ? (
    <S.Container>
      <S.ProfileSection>
        <UserInfo
          profileImageUrl={myData?.imageUrl}
          username={`@${myData?.gitHubId}`}
        />
        <ProductSelect submitHandler={refetchInventoryProducts} />
      </S.ProfileSection>
      <S.InventorySection>
        <SectionHeader>
          <S.Title>보유한 장비 목록</S.Title>
        </SectionHeader>
        <S.InventoryProductList>
          {keyboards.map(({ id: inventoryId, selected, product: { name } }) => (
            <ProductBar
              key={inventoryId}
              name={name}
              barType={selected ? 'selected' : 'default'}
            />
          ))}
        </S.InventoryProductList>
      </S.InventorySection>
    </S.Container>
  ) : (
    <Navigate to={ROUTES.HOME} />
  );
}

export default Profile;
