import styled, { css } from 'styled-components';

export const Container = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
`;

export const Title = styled.h1`
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      font-size: 1.2rem;
    }
    @media screen and ${device.tablet} {
      font-size: 1.5rem;
    }
  `}
`;
