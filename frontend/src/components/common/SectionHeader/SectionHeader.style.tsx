import styled, { css } from 'styled-components';

export const Container = styled.header`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-left: 1rem;
  padding: 1rem 0;
`;

export const Title = styled.h1`
  ${({ theme: { device } }) => css`
    font-size: 1.5rem;
    @media screen and ${device.mobile} {
      font-size: 1.2rem;
    }
  `}
`;
