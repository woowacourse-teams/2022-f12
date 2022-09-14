import { render, RenderOptions } from '@testing-library/react';
import React, { FC, ReactElement } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';

import CacheContextProvider from '@/contexts/CacheContextProvider';
import LoginContextProvider from '@/contexts/LoginContextProvider';
import ModalContextProvider from '@/contexts/ModalContextProvider';

import theme from '@/style/theme';

const AllTheProviders: FC<{ children: React.ReactNode }> = ({ children }) => {
  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
        <CacheContextProvider>
          <LoginContextProvider>
            <ModalContextProvider>{children}</ModalContextProvider>
          </LoginContextProvider>
        </CacheContextProvider>
      </BrowserRouter>
    </ThemeProvider>
  );
};

const customRender = (ui: ReactElement, options?: Omit<RenderOptions, 'wrapper'>) =>
  render(ui, { wrapper: AllTheProviders, ...options });

export * from '@testing-library/react';
export { customRender as render };
