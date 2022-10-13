import styled, { css } from 'styled-components';

export const Main = styled.main`
  display: flex;
  flex-direction: column;
  max-width: 1296px;
  width: 100%;
  gap: 1rem;
  padding: 1.2rem;

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      margin: 10px auto 82px;
    }
    @media screen and ${device.tablet} {
      margin: 20px auto 82px;
    }
    @media screen and ${device.desktop} {
      margin: 30px auto;
    }
  `}
`;
