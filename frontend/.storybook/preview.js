import ResetCss from '../src/style/ResetCss';
export const decorators = [
  (Story) => (
    <>
      <ResetCss />
      <Story />
    </>
  ),
];
