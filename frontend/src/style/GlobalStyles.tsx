import { NavLink } from 'react-router-dom';
import styled, { createGlobalStyle, css } from 'styled-components';

const GlobalStyles = createGlobalStyle`
* {
  font-family: "Pretendard", -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
  color: ${({ theme }) => theme.colors.black}
}

body {
  background-color: ${({ theme }) => theme.colors.white};
  width: 100vw;
  overflow-x: hidden;
}

button {
  cursor: pointer;
}
`;

export const CustomNavLink = styled(NavLink)`
  height: 100%;
  display: flex;
  align-items: center;
  position: relative;
  text-align: center;

  padding: 0 0.5rem;

  overflow: hidden;

  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    width: 100%;
    ${({ theme }) => css`
      border-bottom: 2px solid ${theme.colors.primary};
    `}

    left: -100%;
    margin-left: 200%;
    transition: left 300ms linear, margin-left 300ms ease-out;
  }

  &.active::after {
    left: 0;
    margin-left: 0;
    transition: left 300ms ease-out;
  }
`;

export const SROnly = styled.div`
  overflow: hidden;
  white-space: no-wrap;
  clip: rect(1px, 1px, 1px, 1px);
  clip-path: inset(50%);
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  border: 0;
  top: 0;
  left: 0;
`;

export default GlobalStyles;
