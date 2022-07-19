import ProductBar from '@/components/common/ProductBar/ProductBar';
import UserInfo from '@/components/common/UserInfo/UserInfo';
import * as S from '@/pages/Profile/Profile.style';
import sampleProfile from '@/mocks/sample_profile.jpg';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import useAuth from '@/hooks/useAuth';
import { Navigate } from 'react-router-dom';
import ROUTES from '@/constants/routes';
import useInventory from '@/hooks/useInventory';

function Profile() {
  const { isLoggedIn } = useAuth();
  const [keyboards] = useInventory();

  const selectedProduct = keyboards.find(({ selected }) => selected);
  return isLoggedIn ? (
    <S.Container>
      <S.ProfileSection>
        <UserInfo profileImageUrl={sampleProfile} username="@dev1" />
        {keyboards.length !== 0 && (
          <ProductSelect
            options={keyboards}
            initialValue={selectedProduct}
            // onSelectSubmit={handleInventoryRefetch}
          />
        )}
      </S.ProfileSection>
      <S.InventorySection>
        <SectionHeader>
          <S.Title>보유한 장비 목록</S.Title>
        </SectionHeader>
        <S.InventoryProductList>
          {keyboards.map(
            ({ id: inventoryId, selected: isSelected, product: { name } }) => (
              <ProductBar
                key={inventoryId}
                name={name}
                barType={isSelected ? 'selected' : 'default'}
              />
            )
          )}
        </S.InventoryProductList>
      </S.InventorySection>
    </S.Container>
  ) : (
    <Navigate to={ROUTES.HOME} />
  );
}

export default Profile;
