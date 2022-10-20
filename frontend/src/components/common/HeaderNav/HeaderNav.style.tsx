import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

export const Wrapper = styled.div`
  max-width: 1320px;
  width: 93%;
  margin: 0 auto;
  display: flex;
`;

export const Nav = styled.nav`
  position: sticky;
  top: 0;
  left: 0;
  background-color: ${({ theme }) => theme.colors.white};
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

  display: flex;
  align-items: center;
  justify-content: space-between;

  height: 4rem;
  ${({ theme: { device } }) => css`
    @media screen and ${device.desktop} {
      width: 100%;
      height: 3rem;
      &::after {
        top: 3rem;
      }
    }
  `}
`;

export const FlexLeftWrapper = styled.ul`
  width: 90%;
  height: 3rem;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 2rem;
`;

export const FlexRightWrapper = styled(FlexLeftWrapper)`
  justify-content: flex-end;
`;

export const LoginLink = styled.a``;

export const TransparentButton = styled.button`
  background-color: transparent;
  border: none;
  font-size: 1rem;
`;

export const LogoLink = styled(Link)`
  padding: 1rem;
  width: 11rem;
`;

export const CustomLink = styled(Link)`
  padding: 1rem;
  width: 5rem;

  text-align: center;

  svg {
    width: 2em;
  }
`;
