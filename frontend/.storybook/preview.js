import { BrowserRouter } from 'react-router-dom';
import ResetCss from '../src/style/ResetCss';
export const decorators = [
  (Story) => (
    <>
      <ResetCss />
      <BrowserRouter>
        <Story />
      </BrowserRouter>
    </>
  ),
];
