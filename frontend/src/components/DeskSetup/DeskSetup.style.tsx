import styled, { css } from 'styled-components';

export const Container = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: repeat(4, 1fr);

  overflow-x: scroll;
  -ms-overflow-style: none;
  scrollbar-width: none;
  &::-webkit-scrollbar {
    display: none;
  }

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      padding-left: 1rem;
      padding-right: 1rem;
      justify-items: start;
      grid-column-gap: 1rem;
    }
    @media screen and ${device.tablet} {
      padding-left: 0.3rem;
      padding-right: 0.3rem;
      justify-items: start;
      grid-column-gap: 1rem;
    }
    @media screen and ${device.desktop} {
      justify-items: center;
      grid-column-gap: 0rem;
    }
  `}
`;

export const NoContents = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;
  font-size: 1.5rem;
`;
