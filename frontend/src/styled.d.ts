import 'styled-components';

declare module 'styled-components' {
  export interface DefaultTheme {
    headerHeight: string;

    colors: {
      primary: string;
      secondary: string;
      black: string;
      white: string;
      gray: string;
    };
  }
}
