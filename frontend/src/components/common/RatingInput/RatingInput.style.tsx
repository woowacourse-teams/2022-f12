import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
`;

export const EmptyButton = styled.button`
  background-color: transparent;
  border: none;
  width: 45px;
  cursor: pointer;

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      width: 35px;
    }
  `}
`;
