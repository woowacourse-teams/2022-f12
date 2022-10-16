import styled, { css } from 'styled-components';

export const Container = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const Title = styled.h1`
  ${({ theme: { device } }) => css`
    font-size: 1.5rem;
    @media screen and ${device.mobile} {
      font-size: 1.2rem;
    }
  `}
`;
