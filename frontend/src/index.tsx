import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import ResetCss from '@/style/ResetCss';
import App from '@/App';
import { ThemeProvider } from 'styled-components';
import theme from '@/style/theme';
import GlobalStyles from '@/style/GlobalStyles';
import LoginContextProvider from '@/contexts/LoginContextProvider';

/* eslint-disable */

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('@/mocks/browser');
  worker.start({
    onUnhandledRequest(req) {
      const urlPath = req.url.pathname;

      if (!urlPath.startsWith('http://localhost:8080')) return;
      console.warn(
        'Found an unhandled %s request to %s',
        req.method,
        req.url.href
      );
    },
  });
}

/* eslint-enable */

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <>
    <ThemeProvider theme={theme}>
      <ResetCss />
      <GlobalStyles />
      <BrowserRouter>
        <LoginContextProvider>
          <App />
        </LoginContextProvider>
      </BrowserRouter>
    </ThemeProvider>
  </>
);
