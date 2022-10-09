import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  grid-row-gap: 1rem;

  overflow-x: scroll;
  -ms-overflow-style: none;
  scrollbar-width: none;
  &::-webkit-scrollbar {
    display: none;
  }

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      grid-column-gap: 0.8rem;
    }
    @media screen and ${device.tablet} {
      grid-column-gap: 1rem;
    }
    @media screen and ${device.desktop} {
      grid-column-gap: 0rem;
    }
  `}
`;

export const CategoryTitle = styled.h2`
  font-size: 1.2rem;
`;

export const EditDeskSetupButton = styled.button`
  padding: 0.4rem 1.4rem;
  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 0.4rem;
  box-shadow: 4px 4px 10px ${({ theme }) => theme.colors.secondary};
  width: max-content;

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

export const FlexWrapper = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const PseudoButton = styled.div`
  background-color: transparent;
  border: none;
  cursor: pointer;
`;

export const NoContents = styled.div`
  width: 100%;
  font-size: 1.2rem;
`;
