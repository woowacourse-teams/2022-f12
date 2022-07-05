import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import ResetCss from './style/ResetCss';
import App from './App';

/* eslint-disable */

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./mocks/browser');
  worker.start({
    onUnhandledRequest(req) {
      if (!req.url.pathname.startsWith('http://localhost:3000/')) {
        console.warn(
          'Found an unhandled %s request to %s',
          req.method,
          req.url.href
        );
      }
    },
  });
}

/* eslint-enable */

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <>
    <ResetCss />
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </>
);
