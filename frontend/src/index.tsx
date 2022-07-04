import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import ResetCss from './style/ResetCss';
import App from './App';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <>
    <ResetCss />
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </>
);
