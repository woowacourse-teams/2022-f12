import 'styled-components';

type Breakpoints = {
  mobile: number;
  tablet: number;
  desktop: number;
};

type Device = {
  [Prop in keyof Breakpoints]: `(min-width: ${Breakpoints[Prop]}px)`;
};

declare module 'styled-components' {
  export interface DefaultTheme {
    headerHeight: string;

    colors: {
      primary: string;
      primaryDark: string;
      secondary: string;
      black: string;
      white: string;
      gray: string;
    };

    device: Device;
  }
}
