import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  overflow-y: hidden;

  overflow-x: scroll;
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
    }
    @media screen and ${device.desktop} {
      -ms-overflow-style: none;
      scrollbar-width: none;
      &::-webkit-scrollbar {
        display: none;
      }
    }
  `}
`;

export const Column = styled.div`
  display: flex;
  flex-direction: column;
`;
