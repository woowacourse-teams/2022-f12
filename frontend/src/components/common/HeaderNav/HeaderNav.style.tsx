import { Link } from 'react-router-dom';
import styled from 'styled-components';

export const Wrapper = styled.div`
  max-width: 1320px;
  width: 100%;
  margin: 0 auto;
  display: flex;
`;

export const Nav = styled.nav`
  width: 100%;
  height: 3rem;
  display: flex;

  position: sticky;
  top: 0;
  left: 0;
  background-color: ${({ theme }) => theme.colors.white};
  z-index: 1;

  &::after {
    position: absolute;
    top: 3rem;
    content: '';
    height: 0.3rem;
    width: 100%;
    background: linear-gradient(
      180deg,
      rgba(60, 60, 60, 0.1) 0%,
      rgba(60, 60, 60, 0) 100%
    );
  }
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

export const ProfileLink = styled(Link)``;
