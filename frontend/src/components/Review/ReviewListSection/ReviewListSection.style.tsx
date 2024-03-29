import styled, { css } from 'styled-components';

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;

  min-height: 10rem;
`;

export const Wrapper = styled.div<{ columns: number }>`
  display: grid;
  gap: 2rem 1rem;
  justify-items: center;

  ${({ theme: { device }, columns }) => css`
    @media screen and ${device.mobile} {
      grid-template-columns: repeat(1, 1fr);
    }
    @media screen and (min-width: 768px) {
      grid-template-columns: ${`repeat(${columns}, 1fr)`};
    }
  `}
`;
