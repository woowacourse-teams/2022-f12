import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
`;

export const ProfileSection = styled.section`
  width: 50%;

  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
`;

export const InventorySection = styled.section`
  width: 50%;

  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

export const Title = styled.h1`
  font-size: 1.5rem;
`;

export const InventoryProductList = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
`;
