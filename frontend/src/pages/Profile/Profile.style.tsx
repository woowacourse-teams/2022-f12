import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const ProfileSection = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-bottom: 2rem;
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
  &:hover {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }
`;

export const DeskSetupSection = styled.section`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 22rem;
  background-color: ${({ theme }) => theme.colors.secondary};
  margin-bottom: 2rem;
`;

export const TabButtonWrapper = styled.div`
  width: 100%;
  display: flex;
  align-items: start;
  gap: 0.4rem;
`;

export const TabButton = styled.button<{ selected: boolean }>`
  width: max-content;
  padding: 0.4rem 0.6rem;
  border-radius: 0.4rem;
  background-color: ${({ selected, theme }) =>
    selected ? theme.colors.primary : theme.colors.secondary};
  margin-bottom: 0.5rem;

  &:hover {
    background-color: ${({ theme }) => theme.colors.primary};
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      font-size: 0.8rem;
    }
    @media screen and ${device.tablet} {
      font-size: 1rem;
    }
    @media screen and ${device.desktop} {
      font-size: 1.2rem;
    }
  `}
`;

export const SectionHeaderWrapper = styled.div`
  margin-left: -1rem;
  align-self: flex-start;
`;
