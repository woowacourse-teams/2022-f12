import ProductBar from '@/components/common/ProductBar/ProductBar';
import UserInfo from '@/components/common/UserInfo/UserInfo';
import * as S from '@/pages/Profile/Profile.style';
import sampleProfile from '@/mocks/sample_profile.jpg';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import { products } from '@/mocks/data';
import useAuth from '@/hooks/useAuth';
import { Navigate } from 'react-router-dom';
import ROUTES from '@/constants/routes';

const mockKeyboards = products.slice(0, 6);

const mockInventoryItem = mockKeyboards.map((product, index) => ({
  ...product,
  inventoryId: index,
  isSelected: index === 0,
}));

const selectedProduct = mockInventoryItem.find(({ isSelected }) => isSelected);

function Profile() {
  const { isLoggedIn } = useAuth();

  return isLoggedIn ? (
    <S.Container>
      <S.ProfileSection>
        <UserInfo profileImageUrl={sampleProfile} username="@dev1" />
        <ProductSelect
          options={mockInventoryItem}
          initialValue={selectedProduct}
          // onSelectSubmit={handleInventoryRefetch}
        />
      </S.ProfileSection>
      <S.InventorySection>
        <SectionHeader>
          <S.Title>보유한 장비 목록</S.Title>
        </SectionHeader>
        <S.InventoryProductList>
          {mockInventoryItem.map(({ inventoryId, name, isSelected }) => (
            <ProductBar
              key={inventoryId}
              name={name}
              barType={isSelected ? 'selected' : 'default'}
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
