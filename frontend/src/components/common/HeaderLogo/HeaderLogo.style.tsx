import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${({ theme }) => theme.colors.white};

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      justify-content: flex-start;
      height: 4rem;

      position: sticky;
      top: 0;
      left: 0;
      z-index: 1;

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
    }
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
  ${({ theme: { device } }) => css`
    padding: 1rem;
    @media screen and ${device.mobile} {
      width: 11rem;
    }
    @media screen and ${device.tablet} {
      width: 10rem;
    }
  `}
`;
