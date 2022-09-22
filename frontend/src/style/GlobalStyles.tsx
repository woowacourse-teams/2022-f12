import { NavLink } from 'react-router-dom';
import styled, { createGlobalStyle, css } from 'styled-components';

const GlobalStyles = createGlobalStyle`
* {
  font-family: "Pretendard", -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
  color: ${({ theme }) => theme.colors.black}
}

body {
  background-color: ${({ theme }) => theme.colors.white}
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

export default GlobalStyles;
