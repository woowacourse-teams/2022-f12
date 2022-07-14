import { BrowserRouter } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';
import ResetCss from '@/style/ResetCss';
import GlobalStyles from '@/style/GlobalStyles';
import theme from '@/style/theme';

export const decorators = [
  (Story) => (
    <>
      <ResetCss />
      <ThemeProvider theme={theme}>
        <GlobalStyles />
        <BrowserRouter>
          <Story />
        </BrowserRouter>
      </ThemeProvider>
    </>
  ),
];
