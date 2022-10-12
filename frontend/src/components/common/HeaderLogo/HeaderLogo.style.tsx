import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${({ theme }) => theme.colors.white};

  justify-content: flex-start;
  height: 4rem;

  &::after {
    position: absolute;
    top: 4rem;
    content: '';
    height: 0.3rem;
    width: 100%;
    background: linear-gradient(
      180deg,
      rgba(60, 60, 60, 0.1) 0%,
      rgba(60, 60, 60, 0) 100%
    );
  }

  ${({ theme: { device } }) => css`
    @media screen and ${device.desktop} {
      justify-content: center;
      height: max-content;

      position: relative;

      &::after {
        background: transparent;
        height: 0;
        width: 0;
      }
    }
  `};
`;

export const LinkWrapper = styled(Link)`
  padding: 1rem;
  width: 11rem;
`;
