import { DefaultTheme } from 'styled-components';

const colors = {
  PINK: '#F6BEBE',
  DARK_PINK: '#EA8686',
  LIGHT_GRAY: '#CFCFCF',
  DARK_GRAY: '#3C3C3C',
  BLACK: '#1A1C1D',
  WHITE: '#FAF9F9',
};

const breakpoints = {
  mobile: 320,
  tablet: 768,
  desktop: 1440,
};

const device = {
  mobile: `(min-width: ${breakpoints.mobile}px)`,
  tablet: `(min-width: ${breakpoints.tablet}px)`,
  desktop: `(min-width: ${breakpoints.desktop}px)`,
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
