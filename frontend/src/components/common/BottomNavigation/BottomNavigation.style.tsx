import { NavLink } from 'react-router-dom';
import styled, { css } from 'styled-components';

export const Container = styled.nav`
  position: fixed;
  bottom: 1.5rem;
  left: 5%;
  height: 4rem;
  width: 90%;
  background-color: ${({ theme }) => theme.colors.white};
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  border-radius: 1.5rem;

  ${({ theme: { device } }) => css`
    @media screen and ${device.tablet} {
      width: 50%;
      left: 25%;
    }
  `}
`;

export const NavList = styled.ul`
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 0.5rem;
  padding: 0.8rem 1rem;
`;

export const NavButton = styled(NavLink)`
  width: max-content;
  padding: 0.8rem 1.4rem;

  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;

  font-size: 0.9rem;
  font-weight: 500;

  border-radius: 1.1rem;

  transition: background-color 100ms, font-weight 100ms, box-shadow 100ms;

  &.active {
    font-weight: 700;
    background-color: ${({ theme }) => theme.colors.primary};
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.15);
  }
`;
