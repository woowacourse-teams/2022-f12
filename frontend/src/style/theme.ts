import { DefaultTheme } from 'styled-components';

const colors = {
  PINK: '#F6BEBE',
  DARK_PINK: '#EA8686',
  LIGHT_GRAY: '#CFCFCF',
  DARK_GRAY: '#3C3C3C',
  BLACK: '#1A1C1D',
  WHITE: '#FAF9F9',
};

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
} as const;

export default theme;
