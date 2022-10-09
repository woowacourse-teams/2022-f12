import styled, { css } from 'styled-components';

export const Container = styled.div`
  max-width: 80%;
  display: grid;

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      justify-content: center;
      grid-template-columns: repeat(1, 1fr);
      gap: 2rem;
    }
    @media screen and ${device.tablet} {
      justify-items: center;
      grid-template-columns: repeat(1, 1fr);
      gap: 2rem 7rem;
    }
    @media screen and ${device.desktop} {
      justify-items: center;
      grid-template-columns: repeat(2, 1fr);
      gap: 2rem 10rem;
    }
  `}

  margin: 0 auto;
`;

export const CardWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;

export const NoDataContainer = styled.div`
  display: flex;
  justify-content: center;
`;
