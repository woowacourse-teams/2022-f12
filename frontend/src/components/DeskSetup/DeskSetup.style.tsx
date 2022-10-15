import styled, { css } from 'styled-components';

export const GridContainer = styled.div`
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
      width: 90%;
      justify-items: start;
      grid-column-gap: 0.8rem;
    }
    @media screen and ${device.tablet} {
      width: 100%;
      padding-left: 0.3rem;
      padding-right: 0.3rem;
      justify-items: start;
      grid-column-gap: 1rem;
    }
    @media screen and ${device.desktop} {
      width: 100%;
      justify-items: center;
      grid-column-gap: 0rem;
    }
  `}
`;

export const NoContents = styled.div`
  display: flex;
  justify-content: center;
  width: 80%;
  font-size: 1.3rem;
`;
