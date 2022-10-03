import styled, { css } from 'styled-components';

export const Main = styled.main`
  display: flex;
  flex-direction: column;
  max-width: 1296px;
  width: 100%;

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      gap: 1rem;
      padding: 1.2rem;
    }
    @media screen and ${device.desktop} {
      margin: 50px auto;
      gap: 3rem;
    }
  `}
`;
