import ProductBar from '@/components/common/ProductBar/ProductBar';
import UserInfo from '@/components/common/UserInfo/UserInfo';
import * as S from '@/pages/Profile/Profile.style';
import sampleProfile from '@/mocks/sample_profile.jpg';
import SectionHeader from '@/components/common/SectionHeader/SectionHeader';
import { useReducer, useState } from 'react';
import ProductSelect from '@/components/common/ProductSelect/ProductSelect';
import { products } from '@/mocks/data';

const mockKeyboards = products.slice(0, 6);

function Profile() {
  const mockInventoryItem = mockKeyboards.map((product, index) => ({
    ...product,
    inventoryId: index,
    isSelected: index === 0,
  }));

  const selectedProduct = mockInventoryItem.find(
    ({ isSelected }) => isSelected
  );

  const [isEditMode, setEditMode] = useReducer(
    (isEditMode: boolean) => !isEditMode,
    false
  );

  const [profileProductInput, setProfileProductInput] =
    useState(selectedProduct);

  const handleSelectedProductChange = (value: InventoryProduct) => {
    const target = mockInventoryItem.find(
      ({ inventoryId }) => inventoryId === value.inventoryId
    );

    profileProductInput.isSelected = false;
    target.isSelected = true;

    setProfileProductInput(value);
  };

  return (
    <S.Container>
      <S.ProfileSection>
        <S.EditButton onClick={setEditMode}>
          {isEditMode ? '수정 완료' : '수정하기'}
        </S.EditButton>
        <UserInfo
          profileImageUrl={sampleProfile}
          username="@dev1"
          jobType="프론트엔드"
          career="0-2년차"
        />
        {isEditMode ? (
          <ProductSelect
            setValue={handleSelectedProductChange}
            options={mockInventoryItem}
            value={profileProductInput}
          />
        ) : (
          <ProductBar name={profileProductInput.name} barType={'selected'} />
        )}
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
  );
}

export default Profile;
