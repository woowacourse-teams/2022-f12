import { DefaultTheme } from 'styled-components';

const colors = {
  PINK: '#F6BEBE',
  DARK_PINK: '#EA8686',
  LIGHT_GRAY: '#CFCFCF',
  DARK_GRAY: '#3C3C3C',
  BLACK: '#1A1C1D',
  WHITE: '#FAF9F9',
};

export const breakpoints = {
  mobile: 428,
  tablet: 1024,
};

const device = {
  mobile: `(max-width: ${breakpoints.mobile}px)`,
  tablet: `(min-width: ${breakpoints.mobile + 1}px) and (max-width: ${
    breakpoints.tablet
  }px)`,
  desktop: `(min-width: ${breakpoints.tablet + 1}px)`,
} as const;

const theme: DefaultTheme = {
  headerHeight: '3rem',

  colors: {
    primary: colors.PINK,
    primaryDark: colors.DARK_PINK,
    secondary: colors.LIGHT_GRAY,
    black: colors.BLACK,
    white: colors.WHITE,
    gray: colors.DARK_GRAY,
  },

  device,
} as const;

export default theme;
