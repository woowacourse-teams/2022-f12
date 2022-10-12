import { Link } from 'react-router-dom';
import styled from 'styled-components';

export const Container = styled.nav`
  position: fixed;
  bottom: 0;
  left: 0;
  height: 4.5rem;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.white};

  &::after {
    position: absolute;
    bottom: 4.5rem;
    left: 0;
    content: '';
    height: 0.3rem;
    width: 100%;
    background: linear-gradient(
      180deg,
      rgba(60, 60, 60, 0) 0%,
      rgba(60, 60, 60, 0.1) 100%
    );
  }

  display: flex;
  justify-content: space-around;
  padding: 0 0.8rem;
`;

export const NavButton = styled(Link)`
  width: 100%;
  padding: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;

  font-size: 0.7rem;

  svg {
    width: 70%;
  }
`;

export const LoginLink = styled.a`
  width: 100%;
  padding: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;

  font-size: 0.7rem;

  svg {
    width: 70%;
  }
`;
