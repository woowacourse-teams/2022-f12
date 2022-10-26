import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';

import App from '@/App';

import CacheContextProvider from '@/contexts/CacheContextProvider';
import DeviceContextProvider from '@/contexts/DeviceContextProvider';
import LoginContextProvider from '@/contexts/LoginContextProvider';
import ModalContextProvider from '@/contexts/ModalContextProvider';

import GlobalStyles from '@/style/GlobalStyles';
import ResetCss from '@/style/ResetCss';
import theme from '@/style/theme';

/* eslint-disable */

// if (process.env.NODE_ENV === 'development' && !window.Cypress) {
//   const { worker } = require('@/mocks/browser');
//   worker.start({
//     onUnhandledRequest(req) {
//       const urlPath = req.url.pathname;

//       if (!urlPath.startsWith('http://localhost:8080')) return;
//       console.warn('Found an unhandled %s request to %s', req.method, req.url.href);
//     },
//   });
// }

/* eslint-enable */

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <>
    <ThemeProvider theme={theme}>
      <ResetCss />
      <GlobalStyles />
      <DeviceContextProvider>
        <BrowserRouter>
          <CacheContextProvider>
            <LoginContextProvider>
              <ModalContextProvider>
                <App />
              </ModalContextProvider>
            </LoginContextProvider>
          </CacheContextProvider>
        </BrowserRouter>
      </DeviceContextProvider>
    </ThemeProvider>
  </>
);
