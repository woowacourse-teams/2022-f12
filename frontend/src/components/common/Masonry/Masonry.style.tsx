import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  gap: 1.5rem;
  overflow-y: hidden;

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      overflow-x: scroll;
    }
    @media screen and ${device.desktop} {
      overflow-x: hidden;
    }
  `}
`;

export const Column = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;
