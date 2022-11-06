import { BrowserRouter } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';

import LoginContextProvider from '@/contexts/LoginContextProvider';
import ModalContextProvider from '@/contexts/ModalContextProvider';

import GlobalStyles from '@/style/GlobalStyles';
import ResetCss from '@/style/ResetCss';
import theme from '@/style/theme';

export const decorators = [
  (Story) => (
    <>
      <ResetCss />
      <ThemeProvider theme={theme}>
        <ModalContextProvider>
          <LoginContextProvider>
            <GlobalStyles />
            <BrowserRouter>
              <Story />
            </BrowserRouter>
          </LoginContextProvider>
        </ModalContextProvider>
      </ThemeProvider>
    </>
  ),
];
