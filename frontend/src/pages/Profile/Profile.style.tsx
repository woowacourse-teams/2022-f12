import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3rem;
`;

export const ProfileSection = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  gap: 1rem;
`;

export const InventorySection = styled.section`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

export const InventoryItemHeaderWrapper = styled.div`
  display: flex;
  justify-content: space-between;
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

export const EditDeskSetupButton = styled.button`
  padding: 0.4rem 1.4rem;
  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 0.4rem;
  box-shadow: 4px 4px 10px ${({ theme }) => theme.colors.secondary};
`;

export const DeskSetupSection = styled.section`
  width: 100%;
  height: 25rem;
  background-color: ${({ theme }) => theme.colors.secondary};
`;

export const TabButtonWrapper = styled.div`
  width: 100%;
  display: flex;
  align-items: start;
  gap: 0.2rem;
`;

export const TabButton = styled.button<{ selected: boolean }>`
  width: max-content;
  padding: 0.4rem 0.6rem;
  font-size: 1.2rem;
  border-radius: 0.4rem;
  background-color: ${({ selected, theme }) =>
    selected ? theme.colors.primary : theme.colors.secondary};
  transition: 0.5s;

  &:hover {
    background-color: ${({ theme }) => theme.colors.primary};
    transition: 0.5s;
  }
`;
