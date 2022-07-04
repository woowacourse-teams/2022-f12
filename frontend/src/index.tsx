import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import ResetCss from './style/ResetCss';
import App from './App';

/* eslint-disable */

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./mocks/browser');
  worker.start();
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
