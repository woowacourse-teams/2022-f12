import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  justify-content: space-around;
`;

export const ProfileSection = styled.section`
  width: 45%;

  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
`;

export const InventorySection = styled.section`
  width: 45%;

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

export const EditButton = styled.button`
  align-self: flex-end;
  width: max-content;
  padding: 0.5rem 1rem;
  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 0.3rem;
  border: none;

  filter: drop-shadow(1px 1px 2px rgba(0, 0, 0, 0.25));

  &:hover {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }
`;
